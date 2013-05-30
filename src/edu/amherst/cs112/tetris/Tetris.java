package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 */

import javax.swing.JFrame;

public class Tetris extends JFrame {
	
	Tetris() {
		super("Tetris!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new TPanel());
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new Tetris();
	}
}