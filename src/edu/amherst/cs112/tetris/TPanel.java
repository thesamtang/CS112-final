package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 * 
 * The TPanel contains all the Components and displays them in a FlowLayout.
 * It also contains the ActionListener for the Pause button.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TPanel extends JPanel implements ActionListener{
	
	MatrixComponent TMatrix; 
	NextPieceComponent nextPiece; 
	InfoComponent linesCleared;
	InfoComponent score;
	InfoComponent highScore;
	JButton pause;

	TPanel() {
		super(new FlowLayout(FlowLayout.CENTER));
		nextPiece    = new NextPieceComponent();
		linesCleared = new InfoComponent("Lines Cleared: ");
		score        = new InfoComponent("Score: ");
		highScore    = new InfoComponent("High Score: ");
		TMatrix      = new MatrixComponent(nextPiece, linesCleared, score, highScore);
		pause        = new JButton("Pause");
		
		/*
		 * We attempted to use different layouts, but they ended up being really buggy or they turned
		 * off the keyboard for some unknown reason, so we decided to just stick with using a flow layout
		 * for all components.
		 */
		add(TMatrix);
		add(nextPiece);
		add(linesCleared);
		add(score);
		add(highScore);
		add(pause);
		
		pause.addActionListener(this);
	}

	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(pause)) {
			TMatrix.animator.stopClock();
			int result = JOptionPane.showConfirmDialog
					(this, "Game Paused. Resume?","DON'T USE PAUSE TO CHEAT", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				TMatrix.animator.startClock();
				TMatrix.requestFocusInWindow();
				notify();
			}
			if (result == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}	
	}
}
