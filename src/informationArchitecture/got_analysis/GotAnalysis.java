package informationArchitecture.got_analysis;

import informationArchitecture.got_analysis.AllSubtitles.Episode;
import informationArchitecture.got_analysis.AllSubtitles.Season;

import java.util.ArrayList;
import java.util.List;

import peasy.PeasyCam;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class GotAnalysis extends PApplet {

    static GotAnalysis $;

    List<Character>    characters;

    AllSubtitles       allSubtitles;

    int                maxMentions;

    PeasyCam           cam;

    @Override
    public void setup() {
        $ = this;
        size(displayWidth, displayHeight, P3D);

        allSubtitles = new AllSubtitles();
        allSubtitles.load();

//        println(allSubtitles.toString());

        characters = loadCharacters();
        println(characters.size() + " Characters found.");

        countMentions();

        cam = new PeasyCam(this, 500);
        cam.setMinimumDistance(50);
        cam.setMaximumDistance(1000);

        background(255);

        drawChart();
    }

    @Override
    public void draw() {
//        exit();
    }

    List<Character> loadCharacters() {
        Table table = loadTable("got/characters.tsv", "header");
        List<Character> characters = new ArrayList<Character>();
        for (TableRow row : table.rows()) {
            Character character = new Character();

            character.name = row.getString("name");
            String alternativeNames = row.getString("altName").trim();
            if (!"".equals(alternativeNames)) {
                character.alternativeNames = alternativeNames.split(",");
            } else {
                character.alternativeNames = new String[0];
            }
            character.house = row.getString("house");
            String colour = row.getString("color");
            character.colour = colour == null ? 0 : PApplet.unhex(colour.substring(1));
//            character.colour = color(random(255), random(255), random(255));
//            character.deathSeason = row.getInt("deathSeason");
//            character.deathEpisode = row.getInt("deathEpisode");

            characters.add(character);
        }

        return characters;
    }

    void countMentions() {
        for (Season season : allSubtitles.seasons) {
            for (Episode episode : season.episodes) {
                for (Subtitle subtitle : episode.subtitles) {
                    for (Character character : characters) {
                        if (subtitle.text.contains(character.name)) {
                            addCharacterMention(episode, character);
                        } else {
                            for (String name : character.alternativeNames) {
                                if (subtitle.text.contains(name)) {
                                    addCharacterMention(episode, character);
                                }
                            }
                        }
                    }
                }
                println("S" + season.index + "E" + episode.index + ": " + episode.mentions);
            }
        }
        println("Max mentions: " + maxMentions);
    }

    void addCharacterMention(Episode episode, Character character) {
        character.mentions++;
        Integer mentions = episode.mentions.get(character);
        if (mentions == null) {
            episode.mentions.put(character, 1);
        } else {
            mentions++;
            episode.mentions.put(character, mentions);

            if (mentions > maxMentions) {
                maxMentions = mentions;
            }
        }

    }

    // TODO Linien unterbrechen, wenn Charactere nicht erwähnt werden
    void drawChart() {
        strokeWeight(3);
        float sizeFactor = min(width, height) / 50 * 0.8f;
        int characterIndex = 1;
        for (Character character : characters) {
            stroke(character.colour);
            fill(character.colour);
            beginShape(LINES);
            vertex(0, 0, characterIndex * sizeFactor);

            for (Season season : allSubtitles.seasons) {
                for (Episode episode : season.episodes) {
                    Integer mentions = episode.mentions.get(character);

                    int y = mentions == null ? 0 : mentions;

                    vertex((season.index * Episode.COUNT + episode.index) * sizeFactor, y * sizeFactor, characterIndex
                            * sizeFactor);
                }
            }
            endShape();
            characterIndex++;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", GotAnalysis.class.getName() });
    }
}
