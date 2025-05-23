package de.mcc;

import lombok.Getter;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Boerse {
    private static final Logger logger = Logger.getLogger(Boerse.class.getName());
    public List<Guetereigenschaften> gueter;
    @Getter
    private Benutzer aktuellerBenutzer;

    public Boerse(List<Guetereigenschaften> gueter) {
        this.gueter = gueter;
    }

    public void login() {
        Scanner input = new Scanner(System.in);

        logger.log(Level.WARNING, "Bitte Anmeldedaten eingeben");
        logger.log(Level.WARNING, "Benutzername: ");
        String benutzername = input.nextLine().toUpperCase();
        logger.log(Level.WARNING, "Passwort: ");
        String passwort = input.nextLine();
        logger.log(Level.WARNING, "Unternehmen: ");
        String unternehmen = input.nextLine().toUpperCase();

        List<Benutzer> benutzerListe = List.of(
                new Benutzer("ANGESTELLTE1", "1234", true, "TU BERLIN"),
                new Benutzer("1", "1", true, "TU BERLIN"),
                new Benutzer("ANGESTELLTE2", "5678", true, "TU MÜNCHEN")
        );

        for (Benutzer benutzer : benutzerListe) {
            if (benutzer.getBenutzername().equals(benutzername) &&
                    benutzer.isPasswortKorrekt(passwort)) {
                aktuellerBenutzer = benutzer;
                logger.log(Level.WARNING, "Anmeldung erfolgreich als: " + benutzername);
                return;
            }
        }

        logger.log(Level.SEVERE, "Anmeldung fehlgeschlagen! Benutzername oder Passwort falsch");
    }


    public void gueterHinzufuegen() {
        login();

        if (aktuellerBenutzer == null || !aktuellerBenutzer.isAutorisiert()) {
            logger.log(Level.SEVERE, "Nur autorisierte Benutzer dürfen Güter hinzufügen");
            return;
        }

        Scanner input = new Scanner(System.in);

        logger.log(Level.WARNING, "Gut hinzufügen:");
        logger.log(Level.WARNING, "Gütername: ");
        String guetername = input.nextLine().toUpperCase();
        logger.log(Level.WARNING, "Güterkategorie: ");
        String gueterkategorie = input.nextLine().toUpperCase();
        logger.log(Level.WARNING, "Menge: ");
        int menge = input.nextInt();
        input.nextLine();

        boolean gutVorhanden = false;

        for (Guetereigenschaften g : gueter) {
            if (g.getGuetername().equals(guetername)) {
                g.setMenge(g.getMenge() + menge);
                gutVorhanden = true;
                anpassungNachGueterHinzufuegen(g,menge);
                logger.log(Level.WARNING, "Menge des Gutes wurde aktualisiert: " + g.toString());
                logger.log(Level.WARNING, "Preis des Gutes wurde auch automatisch aktualisiert: " + g.toString());
                break;
            }
        }

        if (!gutVorhanden) {
            logger.log(Level.WARNING, "Aktueller Preis: ");
            double aktuellerPreis = input.nextDouble();
            logger.log(Level.WARNING, "Angeboten Von: ");
            String angebotenVon = input.nextLine().toUpperCase();
            Guetereigenschaften neuesGut = new Guetereigenschaften(guetername, gueterkategorie, menge, aktuellerPreis,angebotenVon);
            gueter.add(neuesGut);
            logger.log(Level.WARNING, "Neues Gut wurde hinzugefügt: " + neuesGut.toString());
            logger.log(Level.WARNING, "Aktuelle Menge von '" + guetername + "': " + neuesGut.getMenge());
        }
    }

    public void anpassungAngebote(String gutName, int geaenderteMenge, double neuerPreis){
        for (Guetereigenschaften g : gueter) {
            if (g.getGuetername().equals(gutName)) {
                if (!g.getAngebotenVon().equals(aktuellerBenutzer.getUnternehmen())) {
                    logger.log(Level.SEVERE, "Sie dürfen nur Güter Ihres eigenen Unternehmens aktualisieren.");
                    return;
                }

                g.setMenge(g.getMenge() + geaenderteMenge);
                g.setAktuellerPreis(neuerPreis);
                logger.log(Level.INFO, "Angebot angepasst: " + g);

                if(geaenderteMenge > 0){
                    anpassungNachGueterHinzufuegen(g,geaenderteMenge);
                } else if (geaenderteMenge < 0){
                    anpassungNachGueterHinzufuegen(g,geaenderteMenge);
                }
                return;
            }
        }
        logger.log(Level.WARNING, "Gut '" + gutName + "' nicht gefunden");
    }

    public void gueterKaufen(String gutName, int gekaufteMenge){
        for (Guetereigenschaften g : gueter) {
            if (g.getGuetername().equals(gutName)) {
                if(g.getMenge() < gekaufteMenge){
                    logger.log(Level.WARNING, "Nicht genügend Bestand für den Kauf");
                    return;
                }

                g.setMenge(g.getMenge() - gekaufteMenge);
                anpassungNachGueterKaufen(g, gekaufteMenge);
                logger.log(Level.INFO, g.getMenge() + " Stück von " + gutName + " ist noch übrig");
                logger.log(Level.WARNING, "Preis des Gutes wurde auch automatisch aktualisiert: " + g.toString());
                return;
            }
        }
    }

    public void anpassungNachGueterHinzufuegen(Guetereigenschaften g, int hinzugefügteMenge){
        double neuerPreis = g.getAktuellerPreis() - (hinzugefügteMenge * 0.25);
        g.setAktuellerPreis(neuerPreis);
        g.addPreisZurHistorie(neuerPreis);
    }

    public void anpassungNachGueterKaufen(Guetereigenschaften g, int verkaufteMenge){
        double neuerPreis = g.getAktuellerPreis() + (verkaufteMenge * 0.25);
        g.setAktuellerPreis(neuerPreis);
        g.addPreisZurHistorie(neuerPreis);
    }

    public void ausgabeKursentwicklung(String gutName, int intervall){
        for (Guetereigenschaften g : gueter) {
            if (g.getGuetername().equals(gutName)) {
                List<Double> entwicklung = g.getPreisEntwicklung();
                int anzahl = Math.min(intervall, entwicklung.size());
                List<Double> letztePreise = entwicklung.subList(entwicklung.size() - anzahl, entwicklung.size());
                logger.log(Level.INFO, "Kursentwicklung (" + anzahl + " letzte Preise) von " + gutName + ": " + letztePreise);
                //logger.log(Level.INFO, "Kursverlauf von " + gutName + ": " + g.getPreisEntwicklung());
                return;
            }
        }
        logger.log(Level.WARNING, "Gut '" + gutName + "' nicht gefunden");
    }

    public void alleGueterAnzeigen(){
        if(gueter.isEmpty()){
            logger.log(Level.INFO, "Es gibt keine Güter an der Börse");
        }else{
            logger.log(Level.INFO, "Alle Güter an der Börse:");
            for (Guetereigenschaften g : gueter) {
                logger.log(Level.INFO, g.toString());
            }
        }
    }

    public Guetereigenschaften findeGenauesGut(String gutName){
        for (Guetereigenschaften g : gueter) {
            if (g.getGuetername().equalsIgnoreCase(gutName)) {
                return g;
            }
        }
        return null;
    }
}
