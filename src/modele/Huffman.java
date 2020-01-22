package modele;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class Huffman {
    private String texte;
    private Map<Character, Integer> frequences;
    private Map<Character, String> lesCodes;
    private ArbreCodage arbreHuffman;

    public Huffman(String texte) {
        this.texte = texte;
        this.frequences = this.litTexte();
        this.arbreHuffman = this.construitArbre();
    }

    private HashMap<Character, Integer> litTexte() {
        HashMap<Character, Integer> lesFrequences = new HashMap<>();
        Stream.of(this.texte.split(""))
                .map(str -> str.charAt(0))
                .map(caractere -> {
                            lesFrequences.put(caractere, lesFrequences.containsKey(caractere) ? lesFrequences.get(caractere) + 1 : 1);
                            return 1;
                        }
                )
                .count();

        return lesFrequences
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private Collection<ArbreCodage> triParPoids(Collection<ArbreCodage> arbresCodage) {
        return arbresCodage
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public ArbreCodage construitArbre() {
        ArbreCodage arbreCodage;
        Collection<ArbreCodage> arbresCodage;
        arbresCodage = this.frequences
                .entrySet()
                .stream()
                .map(frequence -> new ArbreCodage(frequence.getKey(), frequence.getValue()))
                .collect(Collectors.toList());

        while (1 != arbresCodage.size()) {
            // On prend les deux plus petits éléments et on en fait un ArbreCodage
            arbreCodage = arbresCodage
                    .stream()
                    .limit(2)
                    .reduce(ArbreCodage::new)
                    .get();
            // On ôte ces éléments de la lits
            arbresCodage = arbresCodage
                    .parallelStream()
                    .skip(2)
                    .collect(Collectors.toList());
            // Et on ajoute le nouvel élément à la liste
            arbresCodage.add(arbreCodage);
            // On trie à nouveau la liste des arbres
            arbresCodage = this.triParPoids(arbresCodage);
        }

        arbreCodage = arbresCodage.stream()
                .findFirst()
                .get();

        return arbreCodage;
    }

    public ArbreCodage getArbreHuffman() {
        return this.arbreHuffman;
    }

    public void afficheTableCodage() {
        this.lesCodes = this.arbreHuffman
                .codage(
                        Stream.of(this.texte.split(""))
                                .map(str -> str.charAt(0))
                                .collect(Collectors.toSet())
                );
        System.out.println(this.lesCodes);
    }
}
