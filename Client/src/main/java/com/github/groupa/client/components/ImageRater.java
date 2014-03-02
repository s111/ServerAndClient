package com.github.groupa.client.components;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ImageRater {
	
	private JRadioButton rateOne, rateTwo, rateThree, rateFour, rateFive;
	private JRadioButton buttons[] = {rateOne, rateTwo, rateThree, rateFour, rateFive};
	private String names[] = {"1", "2", "3", "4", "5"};
	private JPanel panel;
	public ImageRater() {
		
		for (int i = 0; i < 5; i++) {
			buttons[i] = new JRadioButton(names[i]);
			//buttons[i].addNewActionListener(this);
		}
		
		ButtonGroup ratingGroup = new ButtonGroup();
		for (int i = 0; i < 5; i++) {
			ratingGroup.add(buttons[i]);
		}
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(Box.createRigidArea(new Dimension(10,10)));
		for (int i = 0; i < 5; i++) {
			panel.add(buttons[i]);
			if(i != 4) 
				panel.add(Box.createHorizontalGlue());
		}
		panel.add(Box.createRigidArea(new Dimension(10,10)));	
	}
	
	public JPanel getPanel() {
		return panel;
	}
}