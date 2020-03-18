package fop.model.player;

import java.io.PrintWriter;
import java.util.Date;

public class ScoreEntry implements Comparable<ScoreEntry> {

	private String name;
	private Date date;
	private int score;

	/**
	 * creates a score entry
	 * 
	 * @param name
	 * @param score
	 * @param date
	 */
	public ScoreEntry(String name, int score, Date date) {
		this.name = name;
		this.score = score;
		this.date = date;
	}

	/**
	 * creates a score entry via player
	 * 
	 * @param player
	 */
	public ScoreEntry(Player player) {
		this.name = player.getName();
		this.score = player.getScore();
		this.date = new Date();
	}

	/**
	 * compares the score
	 */
	@Override
	public int compareTo(ScoreEntry scoreEntry) {
		return Integer.compare(this.score, scoreEntry.score);
	}

	/**
	 * prints a new highscore entry
	 * 
	 * @param printWriter
	 */
	public void write(PrintWriter printWriter) {
        printWriter.write(name + ";" + date.getTime() + ";" + getScore());
    }

	/**
	 * reads a score entry and checks if it is allowed
	 * 
	 * @param line
	 * @return
	 */
	public static ScoreEntry read(String line) {
		
		
		String[] split = line.split(";");
	    if (split.length != 3) return null;
	    try {
	        long timestamp = Long.parseLong(split[1]);
	        int score = Integer.parseInt(split[2]);
            return new ScoreEntry(split[0], score, new Date(timestamp));
        } catch (NumberFormatException ignored) {
	        return null;
        }
	}

	/**
	 * returns the current Date
	 * 
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * returns the name
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * returns the score
	 * 
	 * @return
	 */
	public int getScore() {
		return this.score;
	}

}