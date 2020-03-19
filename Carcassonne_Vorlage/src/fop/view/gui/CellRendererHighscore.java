package fop.view.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class CellRendererHighscore extends DefaultTableCellRenderer{
	
	public CellRendererHighscore() {
		setOpaque(true);
	}
		  
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	
		
		if (row == 0) setBackground(new Color(248, 195, 35));
		else if (row == 1) setBackground(new Color(187, 181, 183));
		else if (row == 2) setBackground(new Color(254, 138, 44));
		else setBackground(Math.floorMod(row, 2) == 0 ? new Color(235, 235, 235) : Color.white);
		setText((String)value);
		setFont(table.getFont());
		return this;
	} 
}