package it.jp.legaserie.scrapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

public class UserInterface {

    private static final Integer START_ROUND_KEY = 2;

    private static final Integer END_ROUND_KEY = 1;

    public UserInterface() {
        //no args constructor
    }

    public void start() {

        Scanner scan = new Scanner(System.in);
        boolean limits = true;

        String season = getSeasonFromInput(scan);

        HashMap<Integer, Integer> limitRounds = defineLimitRounds(scan, limits);
        Map<Integer, Map<MatchDay, MatchDay>> rounds = Scrapper.getMatches(limitRounds.get(START_ROUND_KEY), limitRounds.get(END_ROUND_KEY), season);
        PdfGenerator.generatePdf(limitRounds.get(START_ROUND_KEY), limitRounds.get(END_ROUND_KEY), rounds);

        System.out.print("Finito?? ENTER per chiudere");
        scan.nextLine();

        scan.close();
    }

    private HashMap<Integer, Integer> defineLimitRounds(Scanner scan, boolean limits) {
        HashMap<Integer, Integer> limitRounds = new HashMap<>();
        limitRounds.put(START_ROUND_KEY, 1);
        limitRounds.put(END_ROUND_KEY, 38);

        while (limits) {
            try {
                System.out.print("Da qualle giornata? da 1 a 38");
                int startRound = Integer.parseInt(scan.nextLine().trim());

                System.out.print("Fino qualle giornata? da "  + startRound +" a 38");
                int endRound = Integer.parseInt(scan.nextLine().trim());
                if (startRound <= 0 || endRound < startRound || endRound > 38) {
                    System.out.print("Intervalo invalido, prova ancora \n \n");
                    continue;
                }
                limitRounds.put(START_ROUND_KEY, startRound);
                limitRounds.put(END_ROUND_KEY, endRound);
            } catch (NumberFormatException e) {
                System.out.print("dato invalido, prova ancora \n \n");
                continue;
            }

            limits = false;
        }
        return limitRounds;
    }

    private String getSeasonFromInput(Scanner scan) {

        String pattern = "\\d\\d\\d\\d-\\d\\d";
        Pattern r = Pattern.compile(pattern);
        
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        String firstYear = "";
        String secondYear = "";
        if (currentMonth > 5) {
            firstYear = String.valueOf(currentYear);
            secondYear = String.valueOf(currentYear + 1);
        } else {
        	firstYear = String.valueOf(currentYear - 1);
        	secondYear = String.valueOf(currentYear);
        }
        String defaultSeason = firstYear + "-" + StringUtils.substring(secondYear, 2);

        String season = "";
        while (true) {
            System.out.print("Quale staggione? (es: 2021-22), premi invio per la staggione " + defaultSeason);
            season = scan.nextLine().trim();
            Matcher m = r.matcher(season);

            boolean valid = season.equals("") ||
                (m.matches() && season.charAt(2) == season.charAt(5)
                    && Integer.parseInt(String.valueOf(season.charAt(6))) - Integer.parseInt(String.valueOf(season.charAt(3))) == 1);
            if (valid){
                break;
            } else {
                System.out.print("Dato invalido, prova ancora");
            }
        }


        season = !StringUtils.isBlank(season) ? season : defaultSeason;
        
        System.out.print("Stagione " + season + "\n");
        return season;
    }

}
