package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 * 
 * A TCell is the basic object that comprises the both the playing board and each TPiece.
 * It contains an enabled boolean, which is true when a TCell is or was part of the current TPiece.
 * TCells at the bottom of the board which have stopped moving are enabled.
 * It contains a moving boolean, which is true when a TCell is part of the currently moving TPiece.
 * It contains a color, which is assigned based on the piece that the TCell is part of. If not part
 * of any TPiece, the TCell is black.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TCell extends JPanel{
	
	boolean enabled;
	boolean moving;
	Color color;

	TCell(int size, Color c) {
		setPreferredSize (new Dimension(size,size));
		enabled = false;
		moving = false;
		color = c;
		setBackground(c);
	}
	
	public void paint(Color c) {
		setBackground(c);
	}
	
	public void reset() {
		enabled = false;
		moving = false;
		color = Color.BLACK;
	}
}
