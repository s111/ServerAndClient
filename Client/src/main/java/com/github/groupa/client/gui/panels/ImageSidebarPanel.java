package com.github.groupa.client.gui.panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

public class ImageSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	public ImageSidebarPanel() {
		MigLayout layout = new MigLayout("debug");

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		panel.add(new JLabel("Description"), "wrap");

		JTextField descriptionField = new JTextField("description");
		descriptionField.setEnabled(false);
		descriptionField.setBorder(BorderFactory.createEmptyBorder());
		descriptionField.setDisabledTextColor(Color.BLACK);
		descriptionField.setBackground(UIManager.getColor("Panel.background"));

		panel.add(descriptionField, "span 2, growx, pushx, wmax 160");
		panel.add(new JButton("Edit"), "wrap");

		panel.add(new JLabel("Rating"), "wrap");

		JRadioButton buttons[] = new JRadioButton[5];
		ButtonGroup buttonGroup = new ButtonGroup();

		JPanel rater = new JPanel();

		for (int i = 0; i < 5; i++) {
			buttons[i] = new JRadioButton();
			buttons[i].setEnabled(false);
			buttonGroup.add(buttons[i]);

			if (i == 3)
				buttons[i].setSelected(true);

			rater.add(buttons[i]);
		}

		panel.add(rater, "span 2");
		panel.add(new JButton("Edit"), "wrap");

		panel.add(new JLabel("Tags"), "wrap");

		DefaultListModel<Object> defaultListModel = new DefaultListModel<>();
		defaultListModel.addElement("tag1");
		defaultListModel.addElement("tag2");
		defaultListModel.addElement("tag3");

		JList<Object> jList = new JList<>(defaultListModel);

		JScrollPane scrollPane = new JScrollPane(jList);

		panel.add(scrollPane, "span 3, grow, push");
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
