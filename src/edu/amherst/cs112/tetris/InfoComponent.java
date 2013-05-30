package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 * 
 * An InfoComponent displays text info about the game, namely the number of lines cleared, the 
 * current score, and the high score. The values are manipulated in MatrixComponent and the
 * InfoComponents update accordingly.
 */

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoComponent extends JPanel {

	String message; // Lines cleared; Score; High Score
	int value; // value of above
	JLabel info; 

	InfoComponent(String s) {
		message = s;
		value = 0;
		info = new JLabel();
		info.setText(message + value);
		add(info);
	}

	void updateValue(int i) {
		value = i;
		info.setText(message + value);
	}
}
