package de.mcc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Benutzer {
    private String benutzername;
    private String passwort;
    private boolean autorisiert;
    private String unternehmen;

    public boolean isPasswortKorrekt(String eingabe) {
        return passwort.equals(eingabe);
    }
}

