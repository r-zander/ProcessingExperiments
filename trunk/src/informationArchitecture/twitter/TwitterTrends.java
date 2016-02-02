package informationArchitecture.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterTrends extends PApplet {

    Preferences         preferences;

    AccessToken         accessToken;

    static final String TOKEN_KEY      = "informationArchitecture.TwitterGeoTrends.TOKEN";

    static final String SECRET_KEY     = "informationArchitecture.TwitterGeoTrends.SECRET";

    Twitter             twitter;

    TwitterStream       twitterStream;

    static final String consumerKey    = "ZVs4mHBs21037WAGTy9j0CXp2";

    static final String consumerSecret = "HRYmbYuNBPoc0l9DUMY089oVWCfXzH6V6tWXJUzaJBnmt7P9GV";

    Trend[]             trends;

    PImage              map;

    List<PVector>       newPoints      = new ArrayList<PVector>();

    long                tweetsWithoutLocation;

    long                tweetsWithLocation;

    @Override
    public void setup() {
        size(1350, 675);

        map = loadImage("globeVisualization/world.topo.bathy.200401.3x1350x675.jpg");
        image(map, 0, 0);

        preferences = Preferences.userNodeForPackage(getClass());

        twitterOAuth();

//        GeoLocation berlinLocation = new GeoLocation(52.5243700, 13.4105300);
//        newPoints.add(geoLocationToPoint(berlinLocation));

        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {
                handleStatus(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {}

            @Override
            public void onStallWarning(StallWarning arg0) {}
        };

        twitterStream.addListener(listener);
        twitterStream.filter(new FilterQuery(0, null, new String[] { "#WorldBookDay" }));
        twitterStream.sample();
    }

    private void twitterOAuth() {
        String token = preferences.get(TOKEN_KEY, null);
        String secret = preferences.get(SECRET_KEY, null);

        if (token == null || secret == null) {
            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(consumerKey, consumerSecret);

            try {
                RequestToken requestToken = twitter.getOAuthRequestToken();
                accessToken = null;
                while (accessToken == null) {
                    open(requestToken.getAuthenticationURL());

                    String pin =
                            JOptionPane.showInputDialog(
                                    null,
                                    "Enter the PIN",
                                    "GEO TRENDS 2000",
                                    JOptionPane.QUESTION_MESSAGE);

                    if (pin.length() > 5) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                        accessToken = twitter.getOAuthAccessToken();
                    }
                }

                storeAccessToken(accessToken);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            ConfigurationBuilder configBuilder = new ConfigurationBuilder();
            configBuilder.setDebugEnabled(true);
            configBuilder.setOAuthConsumerKey(consumerKey);
            configBuilder.setOAuthConsumerSecret(consumerSecret);
            configBuilder.setOAuthAccessToken(token);
            configBuilder.setOAuthAccessTokenSecret(secret);

//            twitter = new TwitterFactory(configBuilder.build()).getInstance();
            twitterStream = new TwitterStreamFactory(configBuilder.build()).getInstance();
        }
    }

    private void storeAccessToken(AccessToken accessToken) {
        try {
            preferences.put(TOKEN_KEY, accessToken.getToken());
            preferences.put(SECRET_KEY, accessToken.getTokenSecret());
            preferences.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private Trend[] getTrends(int woeid) {
        try {
            twitter.getAvailableTrends();
            return twitter.getPlaceTrends(woeid).getTrends();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return new Trend[0];
    }

    private List<Status> search(String queryString, int count) {
        try {
            Query query = new Query(queryString);
            query.setCount(count);
            QueryResult result = twitter.search(query);
            return result.getTweets();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getRateLimit() {
        try {
            Map<String, RateLimitStatus> status = twitter.getRateLimitStatus();

            println(status);
        } catch (TwitterException e) {

        }
    }

    @Override
    public void draw() {
//        if (frameCount % (60 * 10) == 0) {
//            thread("newSearch");
//        }

        fill(255, 255, 0);
        stroke(255, 255, 0);
        strokeWeight(5);
        for (int i = newPoints.size() - 1; i >= 0; i--) {
            PVector p = newPoints.get(i);

            point(p.x, p.y);

            newPoints.remove(i);
        }

        noStroke();
        fill(255);
        rect(width - 150, height - 60, 150, 60);
        fill(255, 0, 0);
        if (tweetsWithoutLocation > 0) {
            text(
                    String.format("%.1f%%", (float) tweetsWithLocation / tweetsWithoutLocation * 100),
                    width - 150,
                    height - 50);
        }
        text(Long.toString(tweetsWithoutLocation), width - 100, height - 50);

    }

    public void newSearch() {
        Trend[] trends = getTrends(1);
        List<Status> tweets = search(trends[0].getName(), 100);

        for (Status status : tweets) {
            handleStatus(status);
        }
    }

    protected void handleStatus(Status status) {
        GeoLocation geoLocation = status.getGeoLocation();
        if (geoLocation != null && (geoLocation.getLatitude() != 0 || geoLocation.getLongitude() != 0)) {
            newPoints.add(geoLocationToPoint(geoLocation));
            tweetsWithLocation++;
        } else {
            tweetsWithoutLocation++;
        }
    }

    private PVector geoLocationToPoint(GeoLocation geoLocation) {
        int mapWidth = width;
        int mapHeight = height;

        float x = (float) ((180 + geoLocation.getLongitude()) * (mapWidth / 360f));
        float y = (float) ((90 - geoLocation.getLatitude()) * (mapHeight / 180f));

        return new PVector(x, y);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { TwitterTrends.class.getName() });
    }
}