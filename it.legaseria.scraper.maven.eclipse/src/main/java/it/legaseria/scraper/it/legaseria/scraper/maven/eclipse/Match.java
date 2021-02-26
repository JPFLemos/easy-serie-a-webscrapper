package it.legaseria.scraper.it.legaseria.scraper.maven.eclipse;

import java.time.LocalTime;

public class Match {

	private String teamOne;
	private String teamTwo;
	private String channel;
	private String date;
	private String time;
	private int round;
	private boolean isHighlighted;

	public Match(String teamOne, String teamTwo, int round, String date, String channel, String timeAsString) {
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		this.channel = channel;
		this.date = date;
		this.time = timeAsString;
		this.round = round;

	}
	
	public Match(String teamOne, String teamTwo, int round) {
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		this.round = round;
		this.time = "Informazione non disponibile";
		this.channel = "Informazione non disponibile";
		this.date = "Informazione non disponibile";
	}
	
	public Match(String teamOne, String teamTwo, int round, String date) {
		this.teamOne = teamOne;
		this.teamTwo = teamTwo;
		this.round = round;
		this.time = "Informazione non disponibile";
		this.channel = "Informazione non disponibile";
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
	
	public boolean isHighlighted() {
		return (this.teamOne.equals("Hellas") || this.teamTwo.equals("Hellas"));
	}
	
	public void print() {
	System.out.println("Ora: " + time + " Diretta: " + channel);
	if (isHighlighted()) {
		System.out.println("XXXXXXXXXXXXXXXX " + teamOne + " X " + teamTwo + " XXXXXXXXXXXXXXXX" + "\n");
	} else {
		System.out.println(teamOne + " X " + teamTwo + "\n");
	}
	}
}
