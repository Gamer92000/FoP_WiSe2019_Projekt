package fop.view.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.Point2D;

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

	/**
	 * @return a fancy gradient paint for some fancy highscores
	 */
	private LinearGradientPaint getPaint() {
		int row = getRow();
		switch (row) {
			case 0: return createPaint(new Color(248, 195, 35), new Color(255, 213, 141));
			case 1: return createPaint(new Color(187, 181, 183), new Color(242, 231, 235));
			case 2: return createPaint(new Color(254, 138, 44), new Color(255, 172, 108));
			default: return row % 2 == 1 ? createPaint(Color.WHITE, Color.WHITE) : createPaint(new Color(235, 235, 235), new Color(235, 235, 235));
		}
	}

	/**
	 * @param color1
	 * @param color2
	 * @return a fancy gradient paint going from color1 to color2 and back
	 */
	private LinearGradientPaint createPaint(Color color1, Color color2) {
		Point2D start = new Point2D.Float(-(getColumn() * getWidth()), 0);
		Point2D end = new Point2D.Float((3 - getColumn()) * getWidth(), getHeight());
		float[] dist = {0.0f, 0.5f, 1.0f};
		Color[] colors = {color1, color2, color1};
		return new LinearGradientPaint(start, end, dist, colors);
	}

	/**
	 * @return the column of the current cell to paint
	 */
	private int getColumn() {
		return getX() / (getWidth()-1);
	}

	/**
	 * @return the row of the current cell to paint
	 */
	private int getRow() {
		Insets border = highscoreView.getBorder().getBorderInsets();
		return (getY() / (border.bottom + border.top + getHeight()));
	}

}