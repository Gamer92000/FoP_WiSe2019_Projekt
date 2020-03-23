package fop.view.gui;

import fop.model.interfaces.GameMethods;
import fop.model.interfaces.MessagesConstants;
import fop.model.player.ScoreEntry;
import fop.view.components.View;
import fop.view.components.gui.Resources;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

/**
 * HighScore Area
 *
 */
@SuppressWarnings("serial")
public class HighscoreView extends View {

	private JButton btnBack;
	private JButton btnClear;
	private JTable scoreTable;
	private JLabel lblTitle;
	private JScrollPane scrollPane;

	private MatteBorder border = new MatteBorder(0, 0, 1, 0, Color.BLACK);

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
		paintAll(this.getGraphics());
	}

	@Override
	protected void onInit() {
		btnBack = createButton("Back");
		btnClear = createButton("Delete");
		lblTitle = createLabel("Highscores", 25, true);

		Resources resources = Resources.getInstance();

		// creating the table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Date");
		model.addColumn("Name");
		model.addColumn("Points");

		// adding the highscores
		for (ScoreEntry score : resources.getScoreEntries()) {
			Vector<String> vector = new Vector<>();
			vector.add(" " + score.getDate().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
			vector.add(" " + score.getName());
			vector.add(" " + score.getScore());
			model.addRow(vector);
		}
		
		scoreTable = new JTable(model);
		scoreTable.setLocation(0,0);
		scoreTable.setShowHorizontalLines(false);
		scoreTable.setShowVerticalLines(false);
		scoreTable.setEnabled(false);
		scoreTable.setVisible(true);
		scoreTable.setIntercellSpacing(new Dimension(0, 1));
		scoreTable.setAlignmentX(CENTER_ALIGNMENT);
		
		boolean ugly = true;
		scoreTable.setRowHeight(ugly ? 25 : 22);
		if (ugly) scoreTable.setFont(resources.getCelticFont());
		if (ugly) scoreTable.getTableHeader().setFont(resources.getCelticFont());
		if (ugly) lblTitle.setFont(resources.getCelticFont());
		
		scoreTable.getTableHeader().setBorder(border);
		scoreTable.getTableHeader().setReorderingAllowed(false);
		scoreTable.getTableHeader().setResizingAllowed(false);
		scoreTable.getTableHeader().setBackground(Color.WHITE);
		scoreTable.getTableHeader().removeAll();
		scoreTable.setFillsViewportHeight(true);
		scoreTable.setDefaultRenderer(Object.class, new CellRendererHighscore(this));

		scrollPane = new JScrollPane(scoreTable);
		add(scrollPane);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(btnBack))
			GameMethods.GoToMainMenu();
		 else
			GameMethods.deleteHighScoreEntries(MessagesConstants.deleteHighScore());
	}

	public MatteBorder getBorder() {
		return border;
	}
}
