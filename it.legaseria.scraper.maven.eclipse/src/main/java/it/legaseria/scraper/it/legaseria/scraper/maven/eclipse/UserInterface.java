package it.legaseria.scraper.it.legaseria.scraper.maven.eclipse;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
public class UserInterface {

	private Scanner scan;
	private Scrapper scrapper;
	
	public UserInterface(Scanner scanner) {
		this.scan = scanner;
		this.scrapper = new Scrapper();
		
	}
	
	public void start() throws FileNotFoundException, DocumentException {
		boolean limits = true;
		
		while (limits) {
			System.out.println("Da qualle giornata? da 1 a 38");
			int daQuesta = Integer.valueOf(scan.nextLine());
			if (daQuesta <= 0 || daQuesta > 38) {
				System.out.println("Giornata Invalida, prova ancora \n \n");
				continue;
			}

			System.out.println("Fino qualle giornata? da " + daQuesta + " a 38");
			int finoQuesta = Integer.valueOf(scan.nextLine());
			if (finoQuesta < daQuesta || finoQuesta > 38) {
				System.out.println("Intervalo invalido, prova ancora \n \n");
				continue;
			}
			
			limits = false;
			
			scrapper.giornate(daQuesta, finoQuesta);
			
			HashMap<Integer, ArrayList<MatchDay>> rounds= scrapper.getRounds();
			
			// Generating PDF
			
			Document pdfDoc = new Document();
			PdfWriter.getInstance(pdfDoc, new FileOutputStream("Partite.pdf"));
			
			Font giornataFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 20, BaseColor.GRAY);
			Font dataFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 20, BaseColor.MAGENTA);
			Font matchFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
			Font highlightMatchFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
			
			pdfDoc.open();
			pdfDoc.addTitle("Partite");
			pdfDoc.addSubject("Serie A");
			pdfDoc.addAuthor("Giampaolo");
			pdfDoc.addCreator("Giampaolo");
			pdfDoc.addHeader("Giornate " + daQuesta + " a " + finoQuesta,"Giornate " + daQuesta + " a " + finoQuesta);
			pdfDoc.add(new Chunk(""));
			
			Paragraph main = new Paragraph();
			Paragraph emptyLine = new Paragraph(" ");
			
			
			for (int i = daQuesta; i <= finoQuesta; i++) {
				Chunk giornataText = new Chunk ("\n" + "Giornata " + i + "\n", giornataFont);
				System.out.println("Adding giornata " + i);
				main.add(emptyLine);
				main.add(giornataText);
				main.add(emptyLine);
				
				for (MatchDay matchday : rounds.get(i)) {
					Chunk dataText = new Chunk (matchday.getDate() ,dataFont);
					
					main.add(dataText);
					main.add(emptyLine);
					
					for (Match match : matchday.getMatches()) {
						String matchString = match.getHomeTeam() + "  X  " + match.getAwayTeam() + " Ore: " + match.getTime() + "   Diretta: " + match.getChannel() + "\n"; 
						
						Chunk matchText = null;
						if (match.isHighlighted()) {
							matchText = new Chunk (matchString, highlightMatchFont);
						} else {
							matchText = new Chunk (matchString, matchFont);
						}
						
						main.add(matchText);
						main.add(emptyLine);
					}
					
					main.add(emptyLine);
				}
				
			}
			
			pdfDoc.add(main);
			pdfDoc.close();
			
			System.out.println("Finito?? ENTER per chiudere");
			scan.nextLine();

			scan.close();
		}
	}
}
