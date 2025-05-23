package de.mcc;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class Gueter {
    private static final Logger logger = Logger.getLogger(Gueter.class.getName());
    private List<Guetereigenschaften> gueter;

    public Gueter() {
        this.gueter = new ArrayList<>();
        this.gueter.add(new Guetereigenschaften("ABC", "WERTPAPIERBÖRSEN", 20, 3000, "TU BERLIN"));
        this.gueter.add(new Guetereigenschaften("XYZ", "WERTPAPIERBÖRSEN", 40, 5000,"TU BERLIN"));
        this.gueter.add(new Guetereigenschaften("KLM", "WERTPAPIERBÖRSEN", 60, 7000,"TU BERLIN"));
        this.gueter.add(new Guetereigenschaften("TK", "KRYPTOBÖRSEN", 30, 90000,"TU MÜNCHEN"));
        this.gueter.add(new Guetereigenschaften("PR", "KRYPTOBÖRSEN", 50, 110000,"TU MÜNCHEN"));
        this.gueter.add(new Guetereigenschaften("MD", "KRYPTOBÖRSEN", 70, 130000,"TU MÜNCHEN"));
    }

    public String gueterSuchen(String gesuchtesGut){
        StringBuilder str = new StringBuilder();
        for (Guetereigenschaften g : gueter) {
            if (g.getGuetername().contains(gesuchtesGut) || g.getGueterkategorie().contains(gesuchtesGut)) {
                str.append(g).append("\n");
            }
        }

        if (str.length() == 0) {
            String message = "Keine Produkte an der Börse gefunden.";
            logger.log(Level.WARNING, "Suche nach '" + gesuchtesGut + "' ergab keine Treffer.");
            return message;
        }

        return str.toString();
    }
}
