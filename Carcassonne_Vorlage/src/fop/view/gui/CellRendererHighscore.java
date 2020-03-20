package fop.view.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

@SuppressWarnings("serial")
public class CellRendererHighscore extends DefaultTableCellRenderer{

	private HighscoreView highscoreView;

	public CellRendererHighscore(HighscoreView view) {
		this.highscoreView = view;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setText((String) value);
		setFont(table.getFont());
		return this;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setPaint(getPaint());
		graphics.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	private GradientPaint getPaint() {
		int row = getRow();
		switch (row) {
			case 0: return createPaint( new Color(238, 182 , 9), new Color(198, 147, 32));
			case 1: return createPaint(new Color(206, 212, 216), new Color(190, 194, 203));
			case 2: return createPaint(new Color(204, 142, 62), new Color(205, 127, 50));
			default: return row % 2 == 0 ? createPaint(Color.WHITE, Color.WHITE) : createPaint(new Color(235, 235, 235), new Color(235, 235, 235));
		}
	}

	private GradientPaint createPaint(Color color1, Color color2) {
		return new GradientPaint(-(getColumn() * getWidth()), 0, color1, (3 - getColumn()) * getWidth(), getHeight(), color2);
	}

	private int getColumn() {
		return getX() / getWidth();
	}

	private int getRow() {
		Insets border = highscoreView.getBorder().getBorderInsets();
		return (getY() / (border.bottom + border.top + getHeight()));
	}

}