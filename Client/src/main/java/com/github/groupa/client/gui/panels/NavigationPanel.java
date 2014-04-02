package com.github.groupa.client.gui.panels;

import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

public class NavigationPanel implements Panel {
	private JPanel panel = new JPanel();
	private JButton previousButton;
	private JButton upButton;
	private JButton nextButton;
	private JButton rotateCWButton;
	private JButton rotateCCWButton;

	public NavigationPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		previousButton = new JButton("<--");
		rotateCCWButton = new JButton("CCW");
		upButton = new JButton("#");
		rotateCWButton = new JButton(" CW ");
		nextButton = new JButton("-->");

		JPanel buttons = new JPanel();

		buttons.add(previousButton);
		buttons.add(rotateCCWButton);
		buttons.add(upButton);
		buttons.add(rotateCWButton);
		buttons.add(nextButton);

		panel.add(buttons, "dock center");
	}

	public void setNextAction(ActionListener actionListener) {
		nextButton.addActionListener(actionListener);
	}

	public void setPreviousAction(ActionListener actionListener) {
		previousButton.addActionListener(actionListener);
	}

	public void setUpAction(ActionListener actionListener) {
		upButton.addActionListener(actionListener);
	}

	public void setCWAction(ActionListener actionListener) {
		rotateCWButton.addActionListener(actionListener);
	}

	public void setCCWAction(ActionListener actionListener) {
		rotateCCWButton.addActionListener(actionListener);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
