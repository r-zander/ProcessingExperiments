package informationArchitecture.got_analysis;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.data.JSONArray;
import processing.data.JSONObject;

class AllSubtitles {

    List<Season> seasons = new ArrayList<Season>();

    class Season {

        final static int COUNT    = 5;

        int              index;

        List<Episode>    episodes = new ArrayList<Episode>(10);

        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            json.setInt("index", index);

            JSONArray array = new JSONArray();

            for (Episode episode : episodes) {
                array.setJSONObject(episode.index - 1, episode.toJSON());
            }

            json.setJSONArray("episodes", array);

            return json;
        }
    }

    class Episode {

        final static int        COUNT    = 10;

        int                     index;

        List<Subtitle>          subtitles;

        Map<Character, Integer> mentions = new HashMap<Character, Integer>();

        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            json.setInt("index", index);

            JSONArray array = new JSONArray();

            for (Subtitle subtitle : subtitles) {
                array.setJSONObject(subtitle.index - 1, subtitle.toJSON());
                break;
            }

            json.setJSONArray("subtitles", array);

            return json;
        }
    }

    void load() {
        File mainFolder;
        try {
            mainFolder =
                    new File(getClass().getClassLoader().getResource("data" + File.separator + "got" + File.separator)
                            .toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        File[] seasonFolders = mainFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        SrtParser srtParser = new SrtParser();

        for (File seasonFolder : seasonFolders) {
            Season season = new Season();
            seasons.add(season);

            season.index = Integer.parseInt(seasonFolder.getName().substring(1));

            File[] episodeFiles = seasonFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return "srt".equals(name.substring(name.lastIndexOf('.') + 1));
                }
            });

            for (File episodeFile : episodeFiles) {
                Episode episode = new Episode();
                episode.index = Integer.parseInt(episodeFile.getName().substring(0, 2));
                episode.subtitles = srtParser.readSrtFile(episodeFile);
                season.episodes.add(episode);
            }
        }
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();

        for (Season season : seasons) {
            array.setJSONObject(season.index - 1, season.toJSON());
        }

        json.setJSONArray("seasons", array);

        return json.toString();
    }
}
