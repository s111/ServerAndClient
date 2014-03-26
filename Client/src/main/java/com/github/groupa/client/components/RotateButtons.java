package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class RotateButtons {
	private JButton rotateCW;
	private JButton rotateCCW;

	public RotateButtons() {
		rotateCW = new JButton("CW");
		rotateCCW = new JButton("CCW");
		
		addActionListeners();
	}

	public JButton getRotateCWButton() {
		return rotateCW;
	}

	public JButton getRotateCCWButton() {
		return rotateCCW;
	}
	
	
	private void addActionListeners() {
		rotateCW.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent action) {
			 JOptionPane.showMessageDialog(null, "You clicked the rotate CW button!");
			}
		});
		
		rotateCCW.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent action) {
			JOptionPane.showMessageDialog(null, "You clicked the rotate CCW button!");
			}
		});
	}

}
