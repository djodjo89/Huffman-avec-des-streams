package modele;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ArbreCodage extends Arbre<Character> implements Comparable {
    private int poids;

    public ArbreCodage(ArbreCodage filsG, ArbreCodage filsD) {
        super(filsG, (char) 0, filsD);
        this.poids = filsG.poids + filsD.poids;
    }

    public ArbreCodage(Character caractere, int frequence) {
        super(caractere);
        this.poids = frequence;
    }

    public String trouve(Arbre arbreCodage, Character caractere) {
        return (((ArbreCodage) arbreCodage).codage(caractere).equals("trouve!") ? "" : ((ArbreCodage) arbreCodage).codage(caractere));
    }

    public String codage(Character caractere) {
        if (!this.estFeuille()) {
            if (!((ArbreCodage) this.filsG).codage(caractere).equals("")) {
                return "0" + this.trouve(this.filsG, caractere);
            }
            if (!((ArbreCodage) this.filsD).codage(caractere).equals("")) {
                return "1" + this.trouve(this.filsD, caractere);
            }
        } else if (this.contenu == caractere) {
            return "trouve!";
        }
        return "";
    }

    public Map<Character, String> codage(Set<Character> caracteres) {
        return caracteres.stream()
                .collect(Collectors.toMap(
                        caractere -> caractere,
                        caractere -> this.codage(caractere),
                        (e1, e2) -> e2,
                        LinkedHashMap::new
                ));
    }

    @Override
    public int compareTo(Object arbreCodage) {
        if (0 == this.poids - ((ArbreCodage) arbreCodage).poids) {
            return this.contenu.compareTo(((ArbreCodage) arbreCodage).contenu);
        } else {
            return this.poids - ((ArbreCodage) arbreCodage).poids;
        }
    }

    @Override
    public String toString() {
        return "ArbreCodage{" +
                "poids=" + poids +
                ", contenu=" + contenu +
                ", filsG=" + filsG +
                ", filsD=" + filsD +
                '}';
    }
}
