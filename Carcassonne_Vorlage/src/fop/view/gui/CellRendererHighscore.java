package fop.view.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRendererHighscore extends DefaultTableCellRenderer{
	
	
	public CellRendererHighscore() {
		setOpaque(true);
		}
		  
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		  
			if (row == 0) {
				setBackground(new Color(255,215,0));
			} else 
			if (row == 1) {
				setBackground(new Color(192,192,192));
			} else
			if (row == 2) {
				setBackground(new Color(210,105,30));
			} else {
				
				if(Math.floorMod(row, 2) == 0)
					setBackground(Color.lightGray);
				if(Math.floorMod(row, 2) == 1)
					setBackground(Color.white);
			}
			
			return this;
		
		} 

}
