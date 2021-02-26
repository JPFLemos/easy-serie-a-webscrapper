package it.legaseria.scraper.it.legaseria.scraper.maven.eclipse;

import java.util.ArrayList;

public class MatchDay {

    private String displayDate;
    private ArrayList<Match> matches;

    public MatchDay(String displayDate) {
        this.displayDate = displayDate;
        this.matches = new ArrayList<>();

    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public ArrayList<Match> getMatches() {
        return this.matches;
    }

    public String getDate() {
        return this.displayDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        MatchDay matchDayOther = (MatchDay) other;
        if (this.displayDate.equals(matchDayOther.displayDate)) {
            return true;
        } else {
            return false;
        }

    }
}
