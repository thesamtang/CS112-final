package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 * 
 * NextPieceComponent takes in the type of the next TPiece and displays the piece 
 * next to the board.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class NextPieceComponent extends JPanel{

	MatrixComponent c;
	int type;
	
	NextPieceComponent() {
		setPreferredSize(new Dimension(70,70));
		repaint();
	}
	
	void update(int t) {
		type = t;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 70, 70);
		
		if(type == 0) { // square
			g.setColor(Color.YELLOW);
			g.fillRect(19, 19, 31, 31);
			g.setColor(Color.BLACK);
			g.drawLine(34,19,34,50);
			g.drawLine(19,34,50,34);
		}
		if(type == 1) { // z
			g.setColor(Color.GREEN);
			g.fillRect(11, 19, 31, 15);
			g.fillRect(27, 35, 31, 15);
			g.setColor(Color.BLACK);
			g.drawLine(26, 0, 26, 50);
			g.drawLine(42, 0, 42, 50);
		}
		if(type == 2) { // s
			g.setColor(Color.RED);
			g.fillRect(27, 19, 31, 15);
			g.fillRect(11, 35, 31, 15);
			g.setColor(Color.BLACK);
			g.drawLine(26, 0, 26, 50);
			g.drawLine(42, 0, 42, 50);
		}
		if(type == 3) { // t
			g.setColor(Color.MAGENTA);
			g.fillRect(27, 19, 15, 15);
			g.fillRect(11, 35, 47, 15);
			g.setColor(Color.BLACK);
			g.drawLine(26, 0, 26, 50);
			g.drawLine(42, 0, 42, 50);
		}
		if(type == 4) { // j
			g.setColor(Color.BLUE);
			g.fillRect(11, 19, 47, 15);
			g.fillRect(43, 35, 15, 15);
			g.setColor(Color.BLACK);
			g.drawLine(26, 0, 26, 50);
			g.drawLine(42, 0, 42, 50);
		}
		if(type == 5) { // l
			g.setColor(Color.ORANGE);
			g.fillRect(11, 19, 47, 15);
			g.fillRect(11, 35, 15, 15);
			g.setColor(Color.BLACK);
			g.drawLine(26, 0, 26, 50);
			g.drawLine(42, 0, 42, 50);
		}
		if(type == 6) { // straight
			g.setColor(Color.CYAN);
			g.fillRect(4, 28, 62, 15);
			g.setColor(Color.BLACK);
			g.drawLine(19, 0, 19, 50);
			g.drawLine(35, 0, 35, 50);
			g.drawLine(51, 0, 51, 50);
		}
	}
}
