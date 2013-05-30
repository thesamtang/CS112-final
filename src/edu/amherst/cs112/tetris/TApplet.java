package edu.amherst.cs112.tetris;

/**
 * A Tetris applet written by Sam Tang & Brendan Hsu for COSC112 final project
 * May 2013
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class TApplet extends JApplet {
	
	public void init() {
		try {
			SwingUtilities.invokeAndWait(new MyRunnable());
		}
		catch (Exception e) {
			System.err.println ("createGUI failed");
		}
	}

	class MyRunnable implements Runnable {
		public void run() {
			createGUI();
		}
	}

	void createGUI() {
		setContentPane(new TPanel());
	}
}
