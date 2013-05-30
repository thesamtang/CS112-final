package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 * 
 * MatrixComponent holds an 10x21 array of TCells. The first row of TCells is hidden, as Tetris TPieces spawn
 * in this row, and the player loses when a a TPiece stops while it still in this hidden row. 
 * 
 * The MatrixComponent contains the methods which handle multiple TPieces and TCells, and keeps track of 
 * values like the number of cleared lines, score, and high score. The animator and KeyListener classes which
 * affect the TPieces are also located here.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class MatrixComponent extends JPanel {
	
	final static int LINE_WIDTH = 1;
	final static int ROWS = 20;
	final static int COLUMNS = 10;
	final static int CELL_SIZE = 15;
	
	static int clearedRows;
	static int points;
	static int highScore;
	
	static TCell[][] board; 
	LineBorder border;
	TPiece currentPiece;
	int nextPiece; // the next piece's type
	
	NextPieceComponent n;
	InfoComponent linesCleared;
	InfoComponent score;
	InfoComponent record;
	ButtonListener listener;
	TAnimator animator;
	Thread thread;
	
	MatrixComponent(NextPieceComponent n, InfoComponent lc, InfoComponent s, InfoComponent r) {
		this.n = n;
		linesCleared = lc;
		score = s;
		record = r;
		
		clearedRows = 0;
		points = 0;
		highScore = 0;
		
		setLayout(new GridLayout(ROWS, COLUMNS, LINE_WIDTH, LINE_WIDTH));
		board = new TCell[ROWS+1][COLUMNS];
		border = new LineBorder(Color.gray, 1);
		
		// hidden row
		for (int j = 0; j<COLUMNS; j++) {
			board[0][j] = new TCell(CELL_SIZE, Color.BLACK);
		}
		// visible matrix
		for (int i=1; i<ROWS+1; ++i) {
			for (int j=0; j<COLUMNS; ++j) {
				board[i][j] = new TCell(CELL_SIZE, Color.BLACK);
				board[i][j].setBorder(border);
				add(board[i][j]);
			}
		}	
		
		addPiece(nextPiece());	
		listener = new ButtonListener();
		addKeyListener(listener);
		requestFocusInWindow();
		setFocusable(true);			
		
		animator = new TAnimator();
		thread = new Thread(animator);
		thread.start();
		animator.startClock();
	}
	
	/*
	 * addPiece() generates new piece at the top of the board
	 */
	void addPiece() {
		addPiece(nextPiece);
	}
	
	void addPiece(int type) {
		nextPiece = nextPiece();
		n.update(nextPiece);
		currentPiece = new TPiece(type, board, this);
		
		//requestFocus(true);
		paint();
	}
	
	/*
	 * Generates a number 0-6 to use as the type for a TPiece
	 */
	public static int nextPiece() {
		int piece = (int)(Math.random() * 7);
		return piece;
	}
	
	public void paint() {
		for (int i = 1; i < ROWS+1; i++)
			for (int j = 0; j < COLUMNS; j++) {
				if(board[i][j].enabled == true)
					board[i][j].setBackground(board[i][j].color);
				else
					board[i][j].setBackground(Color.black);
			}
	}
	
	/*
	 * reset() clears the board for a new game
	 */
	void reset() {
		for(int i = 0; i < ROWS+1; i++)
			for(int j = 0; j < COLUMNS; j++)
				board[i][j].reset();
		score.updateValue(points=0);
		linesCleared.updateValue(clearedRows=0);
		animator.delay = 1000;
	}
	
	/*
	 * clearRows() will clear a row when a full row is detected. The code repeats 4 times because each time a piece falls,
	 * up to 4 lines can be cleared. Each time a line is cleared, the info from the line above the cleared line is copied
	 * down into the cleared line, and each line above is in turn copied down into the line underneath. At the same time,
	 * the clearedRows int is incremented and the score is updated.
	 */
	void clearRows() { 
		for (int n = 0; n < 4; n++) {
			for(int m = ROWS; m > 0; m--) {
				if(checkRow(m)) {
					clearedRows++;
					linesCleared.updateValue(clearedRows);
					points += 40*(clearedRows/10+1); // 40 for each line, multiplied by the 'level' 
					score.updateValue(points);
					speedUp();
					updateHighScore();
					for(int i = m; i > 0; i--) {
						for(int j = 0; j < COLUMNS; j++) {
							board[i][j].enabled = board[i-1][j].enabled;
							board[i][j].color = board[i-1][j].color;
						}
					}
				}
			}
		}
		paint(); 
	}
	
	/*
	 * checkRow() returns true of the row is full
	 */
	boolean checkRow(int row) {
		for(int j = 1; j < COLUMNS; j++) {
			if(board[row][j].enabled != board[row][j-1].enabled || board[row][j].enabled == false)
				return false;
		}
		return true;
	}
	
	/*
	 * The pieces fall faster each time 10 lines are cleared
	 */
	void speedUp() {
		if (animator.delay > 100 && clearedRows%10 == 0)
			animator.delay -= 100;
	}
	
	void updateHighScore() {
		if(points > highScore) {
			highScore = points;
			record.updateValue(highScore);
		}
	}
	
	public class ButtonListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_RIGHT) {
				if (currentPiece.movableRight()) {
					currentPiece.moveRight();
				}
			} else if (keyCode == KeyEvent.VK_LEFT) {
				if (currentPiece.movableLeft()) {
					currentPiece.moveLeft();
				}
			} else if (keyCode == KeyEvent.VK_UP) {
				if (currentPiece.rotatable()) {
					currentPiece.rotate();
				}
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (currentPiece.movableDown() == true) {
					currentPiece.moveDown();
				}else{
					currentPiece.stop();
					addPiece(nextPiece);
				}
			} else if (keyCode == KeyEvent.VK_SPACE) {
				while (currentPiece.movableDown() == true)
					currentPiece.moveDown();
				currentPiece.stop();
				addPiece(nextPiece);
			}
		}
	}
	
	public class TAnimator implements Runnable {

		private long clockTime1;  
	    private long elapsedTime1;
	    private boolean running;
	    int delay;

	    public TAnimator() {
	    	delay = 1000;
	    }

	    public synchronized void run() {
	    	try {
	    		while (true) {
					if (running) {
						wait(delay);
					} else {
						wait();
					}
					if (currentPiece.movableDown() == true) {
						currentPiece.moveDown();
					}else{
						currentPiece.stop();
						addPiece();
					}
			    }
	    	}
	    	catch (InterruptedException e) {
			    // run() must catch these
	    	}
	    }

	    public synchronized void startClock() {
	    	clockTime1 = System.nanoTime();
	    	running = true;
	    	notify();
	    }
	    
	    public synchronized void stopClock() {
			elapsedTime1 = elapsedTime();
			running = false;
			notify();
	    }

	    public synchronized long elapsedTime() {
	    	if (!running) return elapsedTime1;
	    	else return elapsedTime1 + System.nanoTime() - clockTime1;
	    }
	}
}