package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 * 
 * A TPiece is a collection of 4 TCells. It contains the coordinates of each of the 4 TCells comprising the TPiece,
 * a type int used to determine the type of TPiece it is, an orientation it used to determine the block's current 
 * rotation, and the color of the TPiece as a whole. TPieces rotate around the 'root', which always starts at (5,0).
 * 
 *  Whenever an action is performed on a TPiece, either the coordinates of the root or the orientation of the TPiece
 *  are changed, and these properties are used by the updatePosition() method to determine the coordinates of the
 *  TPiece after the action. 
 *  
 * 
 */

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;

public class TPiece extends JPanel{
	int type; // type of block
	int orientation; // rotation
	int rootRow, rootCol; // coords of the root, moving affects the location of the root, and pieces rotate around it
	int ar, ac; // coords of TCell 2
	int br, bc; // coords of TCell 3
	int cr, cc; // coords of TCell 4
	Color color;
	
	TCell[][] board;
	MatrixComponent component;
	
	public TPiece(int t, TCell[][] b, MatrixComponent c) {
		type = t;
		orientation = 0;
		board = b;
		component = c;
		
		// root always starts at (0,5)
		rootRow = 0;
		rootCol = 5;

		// initial position of piece is set according to the type of the piece 
		if(t==0) { //square
			board[0][4].enabled = board[0][5].enabled = board[1][4].enabled = board[1][5].enabled = true;
			board[0][4].color   = board[0][5].color   = board[1][4].color   = board[1][5].color   = color = Color.YELLOW;
			board[0][4].moving  = board[0][5].moving  = board[1][4].moving  = board[1][5].moving  = true;
			ar = 0; ac = 4;	br = 1;	bc = 4;	cr = 1;	cc = 5;
		} else if (t==1) { //z
			board[0][4].enabled = board[0][5].enabled = board[1][5].enabled = board[1][6].enabled = true;
			board[0][4].color   = board[0][5].color   = board[1][5].color   = board[1][6].color   = color = Color.GREEN;
			board[0][4].moving  = board[0][5].moving  = board[1][5].moving  = board[1][6].moving  = true;
			ar = 0;	ac = 4;	br = 1;	bc = 5;	cr = 1;	cc = 6;
		} else if (t==2) { //s
			board[0][5].enabled = board[0][6].enabled = board[1][4].enabled = board[1][5].enabled = true;
			board[0][5].color   = board[0][6].color   = board[1][4].color   = board[1][5].color   = color = Color.RED;
			board[0][5].moving  = board[0][6].moving  = board[1][4].moving  = board[1][5].moving  = true;
			ar = 0;	ac = 6;	br = 1;	bc = 4;	cr = 1;	cc = 5;
		} else if (t==3) { //t
			board[0][5].enabled = board[1][4].enabled = board[1][5].enabled = board[1][6].enabled = true;
			board[0][5].color   = board[1][4].color   = board[1][5].color   = board[1][6].color   = color = Color.MAGENTA;
			board[0][5].moving  = board[1][4].moving  = board[1][5].moving  = board[1][6].moving  = true;
			ar = 1;	ac = 4;	br = 1;	bc = 5;	cr = 1;	cc = 6;
		} else if (t==4) { //j
			board[0][4].enabled = board[0][5].enabled = board[0][6].enabled = board[1][6].enabled = true;
			board[0][4].color   = board[0][5].color   = board[0][6].color   = board[1][6].color   = color = Color.BLUE;
			board[0][4].moving  = board[0][5].moving  = board[0][6].moving  = board[1][6].moving  = true;
			ar = 0;	ac = 4;	br = 0;	bc = 6;	cr = 1;	cc = 6;
		} else if (t==5) { //l
			board[0][4].enabled = board[0][5].enabled = board[0][6].enabled = board[1][4].enabled = true;
			board[0][4].color   = board[0][5].color   = board[0][6].color   = board[1][4].color   = color = Color.ORANGE;
			board[0][4].moving  = board[0][5].moving  = board[0][6].moving  = board[1][4].moving  = true;
			ar = 0;	ac = 4;	br = 0;	bc = 6;	cr = 1;	cc = 4;
		} else if (t==6) { //straight
			board[0][3].enabled = board[0][4].enabled = board[0][5].enabled = board[0][6].enabled = true;
			board[0][3].color   = board[0][4].color   = board[0][5].color   = board[0][6].color   = color = Color.CYAN;
			board[0][3].moving  = board[0][4].moving  = board[0][5].moving  = board[0][6].moving  = true;
			ar = 0;	ac = 3;	br = 0;	bc = 4;	cr = 0;	cc = 6;
		}
	}
	
	/*
	 * To move a piece, the root is moved and the other pieces follow accordingly in updatePosition() 
	 * depending on type and orientation
	 */
	void moveRight() {
		clearPrev();
		rootCol++;
		updatePosition();
	}
	
	void moveLeft() {
		clearPrev();
		rootCol--;
		updatePosition();
	}
	
	void moveDown() {
		clearPrev();
		rootRow++;
		updatePosition();
	}
	
	void rotate() {
		clearPrev();
		orientation++;
		updatePosition();
	}
	
	// when a piece's position is updated, the old spots are cleared before the new ones are painted
	void clearPrev() { // inefficient
		for(int i = 0; i < component.ROWS+1; i++) {
			for(int j = 0; j < component.COLUMNS; j++) {
				if(board[i][j].enabled == true && board[i][j].moving == true) {
					board[i][j].enabled = false;
					board[i][j].moving = false;
					board[i][j].color = Color.BLACK;
					component.paint();
				}
			}
		}
	}
		
	/*
	 * updatePosition() uses the type and orientation to choose the new coordinates of the piece from the following table
	 */
	void updatePosition() {
		if(type == 0) { // square does not rotate; unaffected by orientation
			ar = rootRow;	ac = rootCol-1;	br = rootRow+1;	bc = rootCol-1;	cr = rootRow+1;	cc = rootCol;
		}

		if(type == 1) { // z
			if(orientation%4 == 0) {
				ar = rootRow;	ac = rootCol-1;	br = rootRow+1;	bc = rootCol;	cr = rootRow+1;	cc = rootCol+1;
			}
			if(orientation%4 == 1) {
				ar = rootRow-1;	ac = rootCol;	br = rootRow;	bc = rootCol-1;	cr = rootRow+1;	cc = rootCol-1;
			}
			if(orientation%4 == 2) {
				ar = rootRow-1;	ac = rootCol-1;	br = rootRow-1;	bc = rootCol;	cr = rootRow;	cc = rootCol+1;
			}
			if(orientation%4 == 3) {
				ar = rootRow-1;	ac = rootCol+1;	br = rootRow;	bc = rootCol+1;	cr = rootRow+1;	cc = rootCol;
			}

		} else if(type == 2) {// s
			if(orientation%4 == 0) {
				ar = rootRow;	ac = rootCol+1;	br = rootRow+1;	bc = rootCol-1;	cr = rootRow+1;	cc = rootCol;
			}
			if(orientation%4 == 1) {
				ar = rootRow-1;	ac = rootCol-1;	br = rootRow;	bc = rootCol-1;	cr = rootRow+1;	cc = rootCol;
			}
			if(orientation%4 == 2) {
				ar = rootRow-1;	ac = rootCol;	br = rootRow-1;	bc = rootCol+1;	cr = rootRow;	cc = rootCol-1;
			}
			if(orientation%4 == 3) {
				ar = rootRow-1;	ac = rootCol;	br = rootRow;	bc = rootCol+1;	cr = rootRow+1;	cc = rootCol+1;
			}
		} else if(type == 3) {// t
			if(orientation%4 == 0) {
				ar = rootRow+1;	ac = rootCol-1;	br = rootRow+1;	bc = rootCol;	cr = rootRow+1;	cc = rootCol+1;
			}
			if(orientation%4 == 1) {
				ar = rootRow-1;	ac = rootCol-1;	br = rootRow;	bc = rootCol-1;	cr = rootRow+1;	cc = rootCol-1;
			}
			if(orientation%4 == 2) {
				ar = rootRow-1;	ac = rootCol-1;	br = rootRow-1;	bc = rootCol;	cr = rootRow-1;	cc = rootCol+1;
			}
			if(orientation%4 == 3) {
				ar = rootRow-1;	ac = rootCol+1;	br = rootRow;	bc = rootCol+1;	cr = rootRow+1;	cc = rootCol+1;
			}

		} else if(type == 4) { // j
			if(orientation%4 == 0){
				ar = rootRow;	ac = rootCol-1;	br = rootRow;	bc = rootCol+1;	cr = rootRow+1;	cc = rootCol+1;	
			}
			if(orientation%4 == 1) {
				ar = rootRow-1;	ac = rootCol;   br = rootRow+1; bc = rootCol;	cr = rootRow+1;	cc = rootCol-1;
			}
			if(orientation%4 == 2) {
				ar = rootRow-1;	ac = rootCol-1;	br = rootRow;	bc = rootCol-1;	cr = rootRow;	cc = rootCol+1;
			}
			if(orientation%4 == 3) {
				ar = rootRow-1;	ac = rootCol;	br = rootRow-1;	bc = rootCol+1;	cr = rootRow+1;	cc = rootCol;	
			}				
		} else if(type == 5) { // l
			if(orientation%4 == 0) {
				ar = rootRow+1;	ac = rootCol-1;	br = rootRow;   bc = rootCol-1; cr = rootRow;   cc = rootCol+1;
			}
			if(orientation%4 == 1) {
				ar = rootRow-1;	ac = rootCol-1;	br = rootRow-1; bc = rootCol;   cr = rootRow+1; cc = rootCol;
			}
			if(orientation%4 == 2) {
				ar = rootRow;	ac = rootCol-1;	br = rootRow;   bc = rootCol+1; cr = rootRow-1; cc = rootCol+1;
			}
			if(orientation%4 == 3) {
				ar = rootRow-1;	ac = rootCol;	br = rootRow+1; bc = rootCol;   cr = rootRow+1; cc = rootCol+1;
			}

		} else if(type == 6) { // straight piece only has 2 orientations in our game
			if(orientation%2 == 0) {
				ar = rootRow;	ac = rootCol-2;	br = rootRow;   bc = rootCol-1; cr = rootRow;   cc = rootCol+1;
			}
			if(orientation%2 == 1) {
				ar = rootRow-1;	ac = rootCol;	br = rootRow+1; bc = rootCol;   cr = rootRow+2; cc = rootCol;
			}
		}

		board[rootRow][rootCol].moving = 
				board[ar][ac].moving =
				board[br][bc].moving =
				board[cr][cc].moving = true; 
		
		board[rootRow][rootCol].enabled =
				board[ar][ac].enabled = 
				board[br][bc].enabled = 
				board[cr][cc].enabled = true;

		board[rootRow][rootCol].color =
				board[ar][ac].color = 
				board[br][bc].color = 
				board[cr][cc].color = color;

		component.paint();
	}
	
	/*
	 * the movable booleans check if the wall or any enabled non-moving TCells are touching the current piece.
	 * If any such TCells exist, the methods will return false;
	 */
	boolean movableDown() {
		if (rootRow==component.ROWS||(board[rootRow+1][rootCol].enabled == true && board[rootRow+1][rootCol].moving == false))
			return false;
		if (ar==component.ROWS||(board[ar+1][ac].enabled == true && board[ar+1][ac].moving == false))
			return false;
		if (br==component.ROWS||(board[br+1][bc].enabled == true && board[br+1][bc].moving == false))
			return false;
		if (cr==component.ROWS||(board[cr+1][cc].enabled == true && board[cr+1][cc].moving == false))
			return false;
		return true;
	}
	
	boolean movableLeft() {
		if(board[rootRow][rootCol].moving == false)
			return false;
		if (rootCol==0||(board[rootRow][rootCol-1].enabled == true && board[rootRow][rootCol-1].moving == false))
			return false;
		if (ac==0||(board[ar][ac-1].enabled == true && board[ar][ac-1].moving == false))
			return false;
		if (bc==0||(board[br][bc-1].enabled == true && board[br][bc-1].moving == false))
			return false;
		if (cc==0||(board[cr][cc-1].enabled == true && board[cr][cc-1].moving == false))
			return false;
		return true;
	}
	
	boolean movableRight() {
		if(board[rootRow][rootCol].moving == false)
			return false;
		if (rootCol==component.COLUMNS-1||(board[rootRow][rootCol+1].enabled == true && board[rootRow][rootCol+1].moving == false))
			return false;
		if (ac==component.COLUMNS-1||(board[ar][ac+1].enabled == true && board[ar][ac+1].moving == false))
			return false;
		if (bc==component.COLUMNS-1||(board[br][bc+1].enabled == true && board[br][bc+1].moving == false))
			return false;
		if (cc==component.COLUMNS-1||(board[cr][cc+1].enabled == true && board[cr][cc+1].moving == false))
			return false;
		return true;
	}
	
	/*
	 * rotatable() checks for non-moving enabled blocks in the vicinity of the current piece
	 */
	boolean rotatable() {
		// cannot rotate when a piece is at the top, because the root starts in the 0th row.
		if (rootRow == 0) 
			return false;
		
		// line pieces extend further than other pieces, so they need their own conditions
		if (type == 6 && orientation%2 == 1)
			if (rootCol-2 < 0 || (board[rootRow][rootCol-2].enabled==true && board[rootRow][rootCol-2].moving == false))
				return false;
		if (type == 6 && orientation%2 == 0)
			if (rootRow+2 > component.ROWS || (board[rootRow+2][rootCol].enabled==true && board[rootRow+2][rootCol].moving == false))
				return false;

		// other pieces
		for (int i = rootRow-1; i <= rootRow+1; i++) {
			for (int j = rootCol-1; j <= rootCol+1; j++) {
				if (i > component.ROWS || j < 0 || j >= component.COLUMNS)
					return false;

				if (board[i][j].enabled == true && board[i][j].moving == false)
					return false;
			}
		}
		return true;
	}
	
	/*
	 * stop() is called when a piece can no longer continue moving down
	 */
	void stop() {
		board[rootRow][rootCol].moving =
				board[ar][ac].moving =
				board[br][bc].moving =
				board[cr][cc].moving = false;
		component.clearRows();
		
		// each time a piece stops, the game checks to see if the player has lost.
		if(lose()) {
			int result = JOptionPane.showConfirmDialog
					(this, "You lose. New game?","You can't really win Tetris", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				component.reset();
			if (result == JOptionPane.NO_OPTION)
				System.exit(0);
		}
	}
	
	/*
	 * if a piece is stopped and is still in the 0th row of the matrix, the player loses
	 */
	boolean lose() {
		if (rootRow == 0)
			return true;
		return false;
	}
}
