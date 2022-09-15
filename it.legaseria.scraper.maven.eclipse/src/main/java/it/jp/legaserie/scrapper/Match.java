package it.jp.legaserie.scrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Match {

    private String teamOne;
    private String teamTwo;
    private String channel;
    private String date;
    private String time;
    private int round;

    public Match(String teamOne, String teamTwo, int round, String date, String channel, String timeAsString) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.channel = channel;
        this.date = date;
        this.time = timeAsString;
        this.round = round;

    }

    public String getChannel() {
        return channel;
    }

    public String getHomeTeam() {
        return teamOne;
    }

    public String getAwayTeam() {
        return teamTwo;
    }

    public String getTime() {
        return this.time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setHomeTeam(String homeTeam) {
        this.teamOne = homeTeam;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDate() {
        return date;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isHighlighted() {
        return (this.teamOne.equals("Hellas") || this.teamTwo.equals("Hellas"));
    }

    public void print() {
        System.out.print("Ora: " + time +" Diretta: " +channel);
        if (isHighlighted()) {
            System.out.print("XXXXXXXXXXXXXXXX "+ teamOne +" X "+ teamTwo +" XXXXXXXXXXXXXXXX \n");
        } else {
            System.out.print(teamOne + " X " + teamTwo +"\n");
        }
    }
}
