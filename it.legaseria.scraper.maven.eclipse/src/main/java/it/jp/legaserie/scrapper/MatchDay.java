package it.jp.legaserie.scrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatchDay {

    private final String displayDate;
    private final ArrayList<Match> matches;

    public MatchDay(String displayDate) {
        this.displayDate = displayDate;
        this.matches = new ArrayList<>();

    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    public String getDate() {
        return this.displayDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayDate, matches);
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
        return this.displayDate.equals(matchDayOther.displayDate);

    }
}
