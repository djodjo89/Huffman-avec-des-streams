package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.Dessin;
import modele.Huffman;

import java.io.*;
import java.util.stream.Stream;

public class Lancement extends Application {

    private Huffman huffman;
    private Dessin<Character> dessin;

    // Fichiers
    private String cheminFichierOrigine, cheminFichierDestination, contenuFichierCompresse;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Override
    public void init() throws IOException {

        this.cheminFichierOrigine = "src/modele/Huffman.java";
        this.cheminFichierDestination = "src/modele/HuffmanCode.txt";

        this.bufferedReader = new BufferedReader(new FileReader(this.cheminFichierOrigine));

        // Calculer le codage du texte à l'aide d'un arbre de Huffman
        this.huffman = new Huffman(
                this.bufferedReader.lines()
                  .reduce((car1, car2) -> car1 + car2)
                  .get());
        huffman.afficheTableCodage();

        // Compresser le texte avec le code obtenu
        this.contenuFichierCompresse = this.bufferedReader.lines().map(ligne ->
                Stream.of(ligne.split(""))
                        .map(caractere -> !"".equals(caractere)
                                ? huffman.getArbreHuffman().codage(caractere.charAt(0))
                                : "")
                        .reduce((car1, car2) -> car1 + '\n' + car2).get()).reduce((car1, car2) -> car1 + '\n' + car2).orElse("");

        // Ecrire le texte hashé dans un nouveau fichier
        this.bufferedWriter = new BufferedWriter(new FileWriter(new File(this.cheminFichierDestination)));
        this.bufferedWriter.write(contenuFichierCompresse);
        this.bufferedWriter.close();

        this.dessin = new Dessin<>(huffman.getArbreHuffman());
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 400, 400);
            root.setCenter(dessin);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Arbre");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
