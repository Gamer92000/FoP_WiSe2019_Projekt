package fop.model.interfaces;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import fop.model.player.Player;
import fop.model.player.ScoreEntry;
import fop.view.components.gui.Resources;

public interface MessagesConstants {
	/**
	 * which player has to place a tile 
	 * @param name
	 * @return
	 */
	public static String playerPlacingTile(String name) {
		return "Player "+ name +", please place a tile.";
	}
	/**
	 * which player has to place a meeple
	 * @param name
	 * @return
	 */
	public static String playerPlacingMeeple(String name) {
		return "Player "+ name +", please place a meeple or skip (right mouse button)";
	}
	/**
	 * if player does not have a placing option
	 * @param name
	 * @return
	 */
	public static String playerNoPlacingMeepleOption(String name) {
		return "There was no option for "+ name +" to place a meeple. Going to next round.";
	}
	/**
	 * creates the winner message withthe list of winners
	 * @param winners
	 */
	public static void showWinner(List<Player> winners) {
		if(winners.size()==1) {
			JOptionPane.showMessageDialog(null, "Der Gewinner ist " + winners.get(0).getName(), null,
					JOptionPane.CLOSED_OPTION);
		}else {
			StringBuilder strg = new StringBuilder(); 			
			winners.stream().forEach(x->{ 
					strg.append(x.getName()+", ");
			});
			String nameofwinner = strg.toString();
			nameofwinner=nameofwinner.substring(0, nameofwinner.length()-2);
				JOptionPane.showMessageDialog(null, "Die Gewinner sind " + nameofwinner, null,
						JOptionPane.CLOSED_OPTION);
			
		}
	}
	
	/**
	 * creates winner message of mission1
	 * @param p
	 */
	public static void showBurgenPoints(HashMap<Player, Integer> p) {
		
		HashMap.Entry<Player, Integer> winner = null;
		for(HashMap.Entry<Player, Integer> entry : p.entrySet()) 
			if (winner == null || entry.getValue().compareTo(winner.getValue()) > 0)
		        winner = entry;
		
		
		String message = "Der Gewinner ist " + winner.getKey().getName() + "  Punkte:" + winner.getKey().getScore() + "  Burgen:" + winner.getValue() + "\n";
		int m = winner.getValue();
		StringBuilder strg = new StringBuilder();
		
		strg.append(message);
		p.forEach((x, y) -> {
			if (y != m)
			strg.append("\n" + x.getName() + "   Punkte: " + x.getScore() + " Burgen: " + y);
		});
		
		String list = strg.toString();
		
		
		JOptionPane.showMessageDialog(null, list, null,
				JOptionPane.CLOSED_OPTION);
	}
	
	/**
	 * open a window showing a player one with mission 2
	 * @param p
	 */
	public static void showWinnerMissionTwo(Player p) {
		
		String message = "Der Spieler " + p.getName() + " hat gewonnen, da er die g�ttliche F�gung besitzt!";
		
		JOptionPane.showMessageDialog(null, message, null,
				JOptionPane.CLOSED_OPTION);
	}
	
	/**
	 * Generates a string listing all winners and their score.
	 * 
	 * @return A string listing all winners and their score.
	 */

	public static String getWinnersMessage(List<Player> winners) {
		String message = "Game over! ";
		Iterator<Player> i = winners.iterator();
		StringBuilder strg = new StringBuilder();
		while (i.hasNext()) {
			Player p = i.next();
			message += p.getName();
			if (i.hasNext())
				message += " and ";
			else
				message += " won, scoring " + p.getScore() + " points.";

			strg.append(p.getName() + ", ");
			ScoreEntry scoreEntry = new ScoreEntry(p);
			Resources.getInstance().addScoreEntry(scoreEntry);
		}
		

		return message;
	}
	
	/**
	 * returns if highschore should be deleted 
	 * @return
	 */
	public static int deleteHighScore () {
		return JOptionPane.showConfirmDialog(null, "Should all entries be deleted?", "Deleting highscores", JOptionPane.YES_NO_OPTION);
	}
	/**
	 * shows if all is deleted 
	 */
	public static void sucessFullDeleted() {
		JOptionPane.showMessageDialog(null, "Entries are deleted successfully", "Deleted", JOptionPane.OK_OPTION);
	}
}
