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

	public NavigationPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		previousButton = new JButton("<--");
		upButton = new JButton("#");
		nextButton = new JButton("-->");

		JPanel buttons = new JPanel();

		buttons.add(previousButton);
		buttons.add(upButton);
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

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
