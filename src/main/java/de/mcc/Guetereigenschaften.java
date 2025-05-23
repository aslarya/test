package de.mcc;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Guetereigenschaften {
    private String guetername;
    private String gueterkategorie;
    private int menge;
    private double aktuellerPreis; //=Börsenkurs
    private String angebotenVon;
    private List<Double> preisEntwicklung = new ArrayList<>();

    //Konstruktor
    public Guetereigenschaften(String guetername, String gueterkategorie,
                               int menge, double aktuellerPreis, String angebotenVon) {
        this.guetername = guetername;
        this.gueterkategorie = gueterkategorie;
        this.menge = menge;
        this.aktuellerPreis = aktuellerPreis;
        this.preisEntwicklung.add(aktuellerPreis);
        this.angebotenVon = angebotenVon;
    }

    public void addPreisZurHistorie(double preis) {
        preisEntwicklung.add(preis);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Gütername=\"");
        str.append(getGuetername());
        str.append("\", Güterkategorie=\"");
        str.append(getGueterkategorie());
        str.append("\", Menge=\"");
        str.append(getMenge());
        str.append("\", Aktueller Preis=\"");
        str.append(getAktuellerPreis());
        str.append("\", PreisEntwicklung=\"");
        str.append(getPreisEntwicklung());
        str.append("\", AngebotenVon=\"");
        str.append(getAngebotenVon());
        return str.toString();
    }
}
