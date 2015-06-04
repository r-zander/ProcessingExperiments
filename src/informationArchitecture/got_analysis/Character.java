package informationArchitecture.got_analysis;

class Character {

    String   name;

    String[] alternativeNames;

    String   house;

    int      colour;

    int      deathSeason;

    int      deathEpisode;

    int      mentions;

    @Override
    public String toString() {
        return name;
    }
}
