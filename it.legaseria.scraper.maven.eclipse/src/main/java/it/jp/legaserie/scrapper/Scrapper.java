package it.jp.legaserie.scrapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {

    private static final String NO_INFO_AVAILABLE_IT = "Informazione non disponibile";


    private Scrapper() {
        throw new IllegalStateException("Static Class");
    }

    public static Map<Integer, Map<MatchDay, MatchDay>> getMatches(int daQuesta, int finoQuesta, String season) {
        HashMap<Integer, Map<MatchDay, MatchDay>>  roundMap = new HashMap<>();

        for (int page = daQuesta; page <= finoQuesta; page++) {
            String url = "http://www.legaseriea.it/it/serie-a/calendario-e-risultati/" + season + "/UNICO/UNI/" + page;

            try {
                LinkedHashMap<MatchDay, MatchDay> daysOfPlay = new LinkedHashMap<>();
                roundMap.put(page, daysOfPlay);

                Document doc = Jsoup.connect(url)
                    .get();

                System.out.print("");
                System.out.print("############## GIORNATA "+ page +" ##############");
                System.out.print("");

                // Fetching round info and all matches
                Elements repos = doc.getElementsByClass("box-partita");

                // Call the method that extract and treats the data of each match
                extractData(repos, daysOfPlay, page);

            } catch (IOException e) {
                System.out.print("Error connecting to the website, make sure the season is valid");
            }
        }

        return roundMap;
    }

    private static void extractData(Elements repositories, LinkedHashMap<MatchDay, MatchDay> daysOfPlay, int round) {

        for (Element repo : repositories) {
            String matchDetails = repo.getElementsByClass("datipartita")
                .text();

            // Match date and time
            String[] dateAndTime = matchDetails.substring(0, 16)
                .replaceAll("[a-zA-Z]", "")
                .trim()
                .split(" ");
            String time = NO_INFO_AVAILABLE_IT; // Setting the default time to not available (in case it is available it will be updated accordingly)

            if (dateAndTime.length > 1) {
                time = dateAndTime[1];
            }

            String displayDate = NO_INFO_AVAILABLE_IT;
            displayDate = getDateInformation(dateAndTime, displayDate);

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

            String channel = getChanelInformation(matchDetails);

            Match match = new Match(teamOne, teamTwo, round, displayDate, channel, time);
            MatchDay matchDay = new MatchDay(displayDate);
            daysOfPlay.putIfAbsent(matchDay, matchDay);
            daysOfPlay.get(matchDay).addMatch(match);

        }

        
        for (MatchDay matchday : daysOfPlay.keySet()) {

            System.out.print("XXXXXXX");
            System.out.print(matchday.getDate());
            System.out.print("XXXXXXX");

            for (Match match : matchday.getMatches()) {
                match.print();
            }

        }
    }

    private static String getChanelInformation(String matchDetails) {
        String channel = NO_INFO_AVAILABLE_IT;
        if (matchDetails.contains("Diretta")) {
            channel = matchDetails.substring(matchDetails.lastIndexOf(":") + 2);
        }

        if (matchDetails.contains("Vedi")) { // Some matches weren't shown on TV and the scrapped info is different
            channel = channel.substring(0, 4)
                .trim();
        }
        return channel;
    }

    private static String getDateInformation(String[] dateAndTime, String displayDate) {
        if (dateAndTime[0].length() > 9) {
            LocalDate dateAsDate = LocalDate.parse(dateAndTime[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            displayDate = dateAsDate.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.ITALIAN));
        }
        return displayDate;
    }

}
