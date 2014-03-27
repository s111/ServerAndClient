package com.github.groupa.client.gui.panels;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

public class GridSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	public GridSidebarPanel() {
		MigLayout layout = new MigLayout("debug");

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		panel.add(new JLabel("Search"), "wrap");

		JTextField searchField = new JTextField(8);

		panel.add(searchField, "split 2, width 128");
		panel.add(new JButton("Search"), "wrap");

		panel.add(new JLabel("Sort"), "wrap");

		DefaultComboBoxModel<Object> defaultComboBoxModel = new DefaultComboBoxModel<>();
		defaultComboBoxModel.addElement("test1");
		defaultComboBoxModel.addElement("test2");

		panel.add(new JComboBox<>(defaultComboBoxModel), "width 128, wrap");

		panel.add(new JLabel("Tags"), "wrap");

		DefaultListModel<Object> defaultListModel = new DefaultListModel<>();
		defaultListModel.addElement("tag1");
		defaultListModel.addElement("tag2");
		defaultListModel.addElement("tag3");

		JScrollPane scrollPane = new JScrollPane(new JList<>(defaultListModel));

		panel.add(scrollPane, "grow, push");
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
