package fop.view.gui;

import java.util.List;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

import javax.naming.LimitExceededException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import fop.Carcassonne;
import fop.controller.GameController;
import fop.model.interfaces.GameConstants;
import fop.model.interfaces.GameMethods;
import fop.model.interfaces.MessagesConstants;
import fop.model.player.ScoreEntry;
import fop.view.components.View;
import fop.view.components.gui.Resources;

/**
 * HighScore Area
 *
 */
public class HighscoreView extends View {

	private JButton btnBack;
	private JButton btnClear;
	private JTable scoreTable;
	private JLabel lblTitle;
	private JScrollPane scrollPane;

	public HighscoreView(GameWindow gameWindow) {
		super(gameWindow);
	}

	@Override
	public void onResize() {
		int offsetY = 25;
		lblTitle.setLocation((getWidth() - lblTitle.getWidth()) / 2, offsetY);
		offsetY += lblTitle.getSize().height + 25;
		scrollPane.setLocation(25, offsetY);
		scrollPane.setSize(getWidth() - 50, getHeight() - 50 - BUTTON_SIZE.height - offsetY);

		btnBack.setLocation((getWidth() / 3) - (BUTTON_SIZE.width / 2), getHeight() - BUTTON_SIZE.height - 25);
		btnClear.setLocation((2 * (getWidth() / 3) - (BUTTON_SIZE.width / 2)), getHeight() - BUTTON_SIZE.height - 25);
	}

	@Override
	protected void onInit() {
		btnBack = createButton("Back");
		btnClear = createButton("Delete");
		lblTitle = createLabel("Highscores", 45, true);
		
		
		Resources resources = Resources.getInstance();
		// TODO
		DefaultTableModel model = new DefaultTableModel(); 
		
		//adding the Colums
		model.addColumn("Date");
		model.addColumn("Name");
		model.addColumn("Points");
		
		//adding the Highscores
		
		List<ScoreEntry> scores = Resources.getInstance().getScoreEntries();
		
		for(int i = 0; i < scores.size(); i++) {
			
			model.addRow(new Object[] {scores.get(i).getDate(), scores.get(i).getName(), scores.get(i).getScore()});
		}
		
		
		scoreTable = new JTable(model); 
		scoreTable.setLocation(0,0);
		scoreTable.setVisible(true);
		
		scrollPane = new JScrollPane(scoreTable);
		
		add(scrollPane);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(btnBack)) {
			GameMethods.GoToMainMenu();

		} else {
				int message = MessagesConstants.deleteHighScore();
				GameMethods.deleteHighScoreEntries(message);
		}
	}
	
	
	
	
}
