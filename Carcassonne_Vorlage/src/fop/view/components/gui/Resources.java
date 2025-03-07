package fop.view.components.gui;

import fop.model.interfaces.GameConstants;
import fop.model.player.ScoreEntry;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Resources implements GameConstants {

	private static Resources instance;
	private Font celticFont;
	private List<ScoreEntry> scoreEntries;
	

	private boolean resourcesLoaded;

	/**
	 * Privater Konstruktor, dieser wird normalerweise nur einmal aufgerufen
	 */
	private Resources() {
		this.resourcesLoaded = false;
	}

	/**
	 * Gibt die Instanz des Resourcen Managers zurück oder erzeugt eine neue
	 * 
	 * @return Resourcen Manager
	 */
	public static Resources getInstance() {
		if (instance == null) {
			instance = new Resources();
			instance.load();
		}

		return instance;
	}

	/**
	 * Lädt alle Resourcen
	 * 
	 * @return true, wenn alle Resourcen erfolgreich geladen wurden
	 */
	public boolean load() {
		if (resourcesLoaded)
			return true;

		try {

			// Load font
			celticFont = loadFont("celtic.ttf");

			// Load score entries
			loadScoreEntries();

			resourcesLoaded = true;
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Konnte Resourcen nicht laden: " + ex.getMessage(),
					"Fehler beim Laden der Resourcen", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Speichert bestimmte Resourcen, zurzeit nur den Highscore-Table
	 * 
	 * @return gibt true zurück, wenn erfolgreich gespeichert wurde
	 */
	public boolean save() {
		try {
			saveScoreEntries();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Diese Methode speichert alle Objekte des Typs {@link ScoreEntry} in der
	 * Textdatei "highscores.txt" Jede Zeile stellt dabei einen ScoreEntry dar.
	 * Sollten Probleme auftreten, muss eine {@link IOException} geworfen werden.
	 * Die Einträge sind in der Liste {@link #scoreEntries} zu finden.
	 * 
	 * @see ScoreEntry#write(PrintWriter)
	 * @throws IOException Eine IOException wird geworfen, wenn Probleme beim
	 *                     Schreiben auftreten.
	 */
	private void saveScoreEntries() throws IOException {
		if (!HIGHSCORE_FILE.exists())
			HIGHSCORE_FILE.createNewFile();
		PrintWriter writer = new PrintWriter(new FileWriter(HIGHSCORE_FILE));
		for (ScoreEntry scoreEntry : scoreEntries) {
			scoreEntry.write(writer);
			writer.println();
		}
        writer.close();
    }

	/**
	 * Lädt den Highscore-Table aus der Datei "highscores.txt". Dabei wird die
	 * Liste {@link #scoreEntries} neu erzeugt und befüllt. Beachte dabei, dass die
	 * Liste nach dem Einlen absteigend nach den Punktzahlen sortiert sein muss.
	 * Sollte eine Exception auftreten, kann diese ausgegeben werden, sie sollte
	 * aber nicht weitergegeben werden, da sonst das Laden der restlichen Resourcen
	 * abgebrochen wird ({@link #load()}).
	 * @throws IOException 
	 * 
	 * @see ScoreEntry#read(String)
	 * @see #addScoreEntry(ScoreEntry)
	 */
	private void loadScoreEntries() throws IOException {
		scoreEntries = new ArrayList<>();

		if (!HIGHSCORE_FILE.exists())
			HIGHSCORE_FILE.createNewFile();

		try {
		    Files.lines(Paths.get(HIGHSCORE_FILE.getPath())).forEach(line -> {
		        ScoreEntry entry = ScoreEntry.read(line);
		        if (entry == null) return;
		        scoreEntries.add(entry);
            });
		    
		    scoreEntries.sort(Comparator.reverseOrder());
		} catch (Exception e) {
			System.err.println("Beim laden der Highscores ist ein Fehler aufgetreten: " + e.getMessage());
		}
	}

	/**
	 * Fügt ein {@link ScoreEntry}-Objekt der Liste von Einträgen hinzu. Beachte:
	 * Nach dem Einfügen muss die Liste nach den Punktzahlen absteigend sortiert
	 * bleiben.
	 * 
	 * @param scoreEntry Der einzufügende Eintrag
	 * @see ScoreEntry#compareTo(ScoreEntry)
	 */
	public void addScoreEntry(ScoreEntry scoreEntry) {
		scoreEntries.add(scoreEntry);
		scoreEntries.sort(Comparator.comparing(ScoreEntry::getScore));
		scoreEntries.sort(Comparator.reverseOrder());
	}

	/**
	 * removes every highscore
	 * @throws IOException
	 */
	public void clearEntries() throws IOException {
		PrintWriter pw = new PrintWriter(HIGHSCORE_FILE);
		pw.print("");
		pw.close();
		loadScoreEntries();
	}

	/**
	 * @return scoreEntries
	 */
	public List<ScoreEntry> getScoreEntries() {
		return scoreEntries;
	}

	/**
	 * @param name
	 * @return the font thats name's given a little more fancy
	 * @throws IOException
	 * @throws FontFormatException
	 */
	private Font loadFont(String name) throws IOException, FontFormatException {
		InputStream is = Resources.class.getClassLoader().getResourceAsStream(name);
		Font f = Font.createFont(Font.TRUETYPE_FONT, is);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(f);
		return f.deriveFont(20f);
	}

	/**
	 * @return celticFont
	 */
	public Font getCelticFont() {
		return this.celticFont;
	}
}