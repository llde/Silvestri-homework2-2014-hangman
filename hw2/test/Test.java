package hw2.test;


import hw2.game.ui.TextUIWGames;
import hw2.games.EvilHangman;
import hw2.games.Hangman;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/** Classe per fare il testing dell'homework. La classe può essere liberamente
 * modificata aggiungendo ad esempio altri test. */
public class Test {
    public static void main(String[] args) throws IOException {
        PartialGrade.testWord = "malvisto";
        PartialGrade.testing = true;
        TextUIWGames.start(new Hangman(Paths.get("files", "parole3.txt"), StandardCharsets.UTF_8, 7),
                new EvilHangman(Paths.get("files", "parole3.txt"), StandardCharsets.UTF_8, 7),
                new PartialGrade.DummyGame(), new PartialGrade.DummyGame2());

    }
}
