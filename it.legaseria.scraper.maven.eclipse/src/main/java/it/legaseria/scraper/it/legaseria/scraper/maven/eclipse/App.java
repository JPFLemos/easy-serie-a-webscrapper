package it.legaseria.scraper.it.legaseria.scraper.maven.eclipse;

import java.io.FileNotFoundException;
import java.util.Scanner;

import com.itextpdf.text.DocumentException;

public class App {
	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		Scanner scan = new Scanner(System.in);
		UserInterface ui = new UserInterface(scan);
		
		ui.start();
	}
}
