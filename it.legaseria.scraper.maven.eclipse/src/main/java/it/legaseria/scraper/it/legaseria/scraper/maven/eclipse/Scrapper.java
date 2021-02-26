package it.legaseria.scraper.it.legaseria.scraper.maven.eclipse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {

    private HashMap<Integer, ArrayList<MatchDay>> rounds;

    public void giornate(int daQuesta, int finoQuesta) {
        this.rounds = new HashMap<>();

        for (int page = daQuesta; page <= finoQuesta; page++) {
            String url = "http://www.legaseriea.it/it/serie-a/calendario-e-risultati/2020-21/UNICO/UNI/" + String.valueOf(page);

            try {
                // Creating HashMap to store all match of this round;
                ArrayList<MatchDay> daysOfPlay = new ArrayList<>();
                rounds.put(page, daysOfPlay);

                // Using Jsoup to connect to the website
                Document doc = Jsoup.connect(url)
                    .get();

                // Starting each round of matches
                System.out.println("");
                System.out.printf("############## GIORNATA " + page + " ##############");
                System.out.println("");

                // Fetching round info and all matches
                Elements repos = doc.getElementsByClass("box-partita");

                // Call the method that extract and treats the data of each match
                extractData(repos, daysOfPlay, page);

                // In case of any errors we throw an exception;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void extractData(Elements repositories, ArrayList<MatchDay> daysOfPlay, int round) {

        for (Element repo : repositories) {
            String matchDetails = repo.getElementsByClass("datipartita")
                .text();

            // Match date and time
            String[] dateAndTime = matchDetails.substring(0, 16)
                .replaceAll("[a-zA-Z]", "")
                .trim()
                .split(" ");
            String time = "Informazione non disponibile"; // Setting the default time to not available (in case it is available it will be updated accordingly)

            if (dateAndTime.length > 1) {
                time = dateAndTime[1];
            }

            String displayDate = "";
            if (dateAndTime[0].length() > 9) {
                LocalDate dateAsDate = LocalDate.parse(dateAndTime[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                displayDate = dateAsDate.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.ITALIAN));
            }

            // Match Teams
            String teams = repo.getElementsByClass("nomeSquadra")
                .text();
            String[] teamsArray = teams.split(" ");
            String teamOne = teamsArray[0];
            String teamTwo = teamsArray[1];
            if (teamOne.equals("Hellas")) { // When name is Hellas Verona the array is different
                teamOne = teamsArray[0];
                teamTwo = teamsArray[2];
            }

            // Match Channel
            String channel = "Informazione non disponibile";
            if (matchDetails.contains("Diretta")) {
                channel = matchDetails.substring(matchDetails.lastIndexOf(":") + 2);
            }

            if (matchDetails.contains("Vedi")) { // Some matches weren't shown on TV and the scrapped info is different
                channel = channel.substring(0, 4)
                    .trim();
            }

            Match match = new Match(teamOne, teamTwo, round, displayDate, channel, time);
            MatchDay matchDay = new MatchDay(displayDate);
            if (!daysOfPlay.contains(matchDay)) {
                daysOfPlay.add(matchDay);
            }

            daysOfPlay.get(daysOfPlay.size() - 1)
                .addMatch(match);

        }

        for (MatchDay matchday : daysOfPlay) {

            System.out.println("XXXXXXX");
            System.out.println(matchday.getDate());
            System.out.println("XXXXXXX");

            for (Match match : matchday.getMatches()) {

                match.print();
            }

        }
    }

    public HashMap<Integer, ArrayList<MatchDay>> getRounds() {
        return this.rounds;
    }
}
