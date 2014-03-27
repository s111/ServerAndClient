package com.github.groupa.client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

public class GridContentPanel implements ContentPanel {
	private JPanel panel = new JPanel();

	public GridContentPanel() {
		MigLayout layout = new MigLayout("debug");

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		final Square square = new Square();

		panel.add(square, "grow, push, wrap");

		NavigationPanel navigationPanel = new NavigationPanel();

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				square.changeColor();
			}
		};

		navigationPanel.setNextAction(actionListener);
		navigationPanel.setPreviousAction(actionListener);

		panel.add(navigationPanel.getPanel(), "growx");
	}

	@SuppressWarnings("serial")
	private class Square extends JPanel {
		private Color color;

		public Square() {
			color = Color.BLUE;

			setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

			setPreferredSize(new Dimension(257, 257));
		}

		public void changeColor() {
			color = Color.getHSBColor((float) Math.random(),
					(float) Math.random(), (float) Math.random());

			repaint();
		}

		@Override
		protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			Graphics2D graphics2D = (Graphics2D) graphics;

			graphics2D.setColor(color);
			graphics2D.fillRect((getWidth() - 256) / 2,
					(getHeight() - 256) / 2, 256, 256);
		}
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
