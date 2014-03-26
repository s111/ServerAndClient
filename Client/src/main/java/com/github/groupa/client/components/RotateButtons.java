package com.github.groupa.client.components;

import javax.swing.JButton;

public class RotateButtons {
	private JButton rotateCW;
	private JButton rotateCCW;

	public RotateButtons() {
		rotateCW = new JButton("CW");
		rotateCCW = new JButton("CCW");

	}

	public JButton getRotateCWButton() {
		return rotateCW;
	}

	public JButton getRotateCCWButton() {
		return rotateCCW;
	}
}
