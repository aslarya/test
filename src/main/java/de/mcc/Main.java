package de.mcc;

import lombok.extern.java.Log;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static Gueter gueter = new Gueter();
    private static Boerse boerse = new Boerse(gueter.getGueter());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        logger.log(Level.INFO, "Willkommen, was möchten Sie tun?");
        logger.log(Level.INFO, "(1) Alle Güter auf der Börse anzeigen");
        logger.log(Level.INFO, "(2) Nach Gütern suchen");
        logger.log(Level.INFO, "(3) Gütern hinzufügen");
        logger.log(Level.INFO, "(4) Angebote anpassen"); //nur möglich für die Verkaufende
        logger.log(Level.INFO, "(5) Gütern kaufen");
        logger.log(Level.INFO, "(6) Kursentwicklung eines Gutes einsehen");
        logger.log(Level.INFO, "(7) EXIT");

        int choice;
        do {
            logger.log(Level.INFO, "Bitte wählen Sie eine Option: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    boerse.alleGueterAnzeigen();
                    break;
                case 2:
                    logger.log(Level.INFO, "Geben Sie den zu suchenden Begriff ein (Güter/Gütereigenschaften) ein:");
                    scanner.nextLine();
                    String searchQuery = scanner.nextLine().toUpperCase();
                    logger.log(Level.INFO, gueter.gueterSuchen(searchQuery));
                    break;
                case 3:
                    boerse.gueterHinzufuegen();
                    break;
                case 4:
                    boerse.login();
                    if (boerse.getAktuellerBenutzer() != null) {
                        logger.log(Level.INFO, "Geben Sie Gütername ein:");
                        scanner.nextLine();
                        String name = scanner.nextLine().toUpperCase();
                        Guetereigenschaften gut = boerse.findeGenauesGut(name);
                        logger.log(Level.INFO, "Aktuelle Menge: " + gut.getMenge() + ", Aktueller Preis: " + gut.getAktuellerPreis());
                        logger.log(Level.INFO, "Hinzugefügte Menge (Nur für die Preisänderung, geben sie 0 ein):");
                        int menge = scanner.nextInt();
                        logger.log(Level.INFO, "Geben Sie neuer Preis ein:");
                        double preis = scanner.nextDouble();
                        scanner.nextLine();
                        boerse.anpassungAngebote(name, menge, preis);
                    }
                    break;
                case 5:
                    logger.log(Level.INFO, "Geben Sie Gütername und Menge ein:");
                    scanner.nextLine();
                    String gut2 = scanner.nextLine().toUpperCase();
                    int m = scanner.nextInt();
                    boerse.gueterKaufen(gut2, m);
                    break;
                case 6:
                    logger.log(Level.INFO, "Geben Sie Gütername ein:");
                    scanner.nextLine();
                    String g = scanner.nextLine().toUpperCase();
                    logger.log(Level.INFO, "Wählen Sie das Intervall für die Kursentwicklung:");
                    logger.log(Level.INFO, "1 = letzter Preis | 2 = letzte 2 Preise | 3 = letzte 3 Preise");
                    int auswahl = scanner.nextInt();
                    scanner.nextLine();

                    if (auswahl >= 1 && auswahl <= 3) {
                        boerse.ausgabeKursentwicklung(g, auswahl);
                    } else {
                        logger.log(Level.WARNING, "Ungültige Auswahl. Bitte 1, 2 oder 3 wählen.");
                    }
                    break;
                case 7:
                    logger.log(Level.INFO, "Programm wird beendet");
                    break;
                default:
                    logger.log(Level.WARNING, "Bitte geben Sie ein Wert von 1 bis 6");
            }
        } while (choice != 7);

        scanner.close();
    }
}