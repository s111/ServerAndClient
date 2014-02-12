package com.github.groupa.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Launch the application.
 * 
 * @author Brynjulf Risbakken
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 8268973863538368807L;
	public static MainWindow mainWindow;

	/**
	 * Create the frame.
	 */
	private MainWindow() {
		JPanel content = new JPanel();
		content.setOpaque(true);
		setContentPane(content);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setPreferredSize(new Dimension(800, 600));
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow = new MainWindow();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
