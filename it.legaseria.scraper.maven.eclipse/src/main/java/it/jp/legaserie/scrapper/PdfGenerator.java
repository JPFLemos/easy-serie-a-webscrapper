package it.jp.legaserie.scrapper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PdfGenerator {

    private PdfGenerator() {
        throw new IllegalStateException("Static Class");
    }
    public static void generatePdf(int startRound, int endRound, Map<Integer, Map<MatchDay, MatchDay>> rounds) {
        Document pdfDoc = new Document();
        try {
            PdfWriter.getInstance(pdfDoc, Files.newOutputStream(Paths.get("Partite.pdf")));
            writeTitleAndMetaData(startRound, endRound, pdfDoc);
            pdfDoc.add(writeData(startRound, endRound, rounds));
            pdfDoc.close();

        } catch (IOException | DocumentException exception) {
            System.out.print("Errore nella generazione del PDF \n \n");
        }
    }

    private static void writeTitleAndMetaData(int startRound, int endRound, Document pdfDoc) throws DocumentException {
        pdfDoc.open();
        pdfDoc.addTitle("Partite");
        pdfDoc.addSubject("Serie A");
        pdfDoc.addAuthor("Giampaolo");
        pdfDoc.addCreator("Giampaolo");
        pdfDoc.addHeader("Giornate " + startRound + " a " + endRound, "Giornate " + startRound + " a " + endRound);
        pdfDoc.add(new Chunk(""));
    }

    private static Paragraph writeData(int startRound, int endRound, Map<Integer, Map<MatchDay, MatchDay>> rounds) {
        Paragraph main = new Paragraph();
        Paragraph emptyLine = new Paragraph(" ");

        Font giornataFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 20, BaseColor.GRAY);
        Font dataFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 20, BaseColor.MAGENTA);
        Font matchFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font highlightMatchFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);

        for (int i = startRound; i <= endRound; i++) {
            Chunk giornataText = new Chunk("\n" + "Giornata " + i + "\n", giornataFont);
            System.out.print("Adding giornata " + i);
            main.add(emptyLine);
            main.add(giornataText);
            main.add(emptyLine);

            processRounds(rounds, main, emptyLine, dataFont, matchFont, highlightMatchFont, i);

        }
        return main;
    }

    private static void processRounds(Map<Integer, Map<MatchDay, MatchDay>> rounds, Paragraph main, Paragraph emptyLine, Font dataFont, Font matchFont, Font highlightMatchFont, int i) {
        for (MatchDay matchday : rounds.get(i).values()) {
            Chunk dataText = new Chunk(matchday.getDate(), dataFont);
            main.add(dataText);
            main.add(emptyLine);

            processMatches(main, emptyLine, matchFont, highlightMatchFont, matchday);

            main.add(emptyLine);
        }
    }

    private static void processMatches(Paragraph main, Paragraph emptyLine, Font matchFont, Font highlightMatchFont, MatchDay matchday) {
        for (Match match : matchday.getMatches()) {
            String matchString = match.getHomeTeam() + "  X  " + match.getAwayTeam() + " Ore: " + match.getTime() + "   Diretta: " + match.getChannel() + "\n";
            Chunk matchText = match.isHighlighted() ? new Chunk(matchString, highlightMatchFont) : new Chunk(matchString, matchFont);
            main.add(matchText);
            main.add(emptyLine);
        }
    }
}
