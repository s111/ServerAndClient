package com.github.groupa.client.gui.panels;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.events.KnownTagsChangedEvent;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.library.TagConstraint;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class TagFilterPanel implements Panel {
	private JPanel panel = new JPanel(new MigLayout());
	DefaultListModel<String> listModel = new DefaultListModel<>();
	private Map<String, TagConstraint> selectedTags = new HashMap<>();

	private Library library;

	@Inject
	public TagFilterPanel(Library library, EventBus eventBus) {
		this.library = library;
		eventBus.register(this);
		setup();
	}

	private void setup() {
		JLabel filterByTagsLabel = new JLabel("Filter by tags");
		filterByTagsLabel.setFont(new Font(filterByTagsLabel.getFont()
				.getName(), Font.BOLD, 13));
		filterByTagsLabel
				.setToolTipText("ctrl click to select and deselect multiple tags");
		panel.add(filterByTagsLabel, "wrap");
		final JList<String> list = new JList<>(listModel);
		JScrollPane scrollPane = new JScrollPane(list);
		panel.add(scrollPane, "grow, push, wrap");

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					setSelected(list.getSelectedValuesList());
				}
			}

		});
	}

	protected void setSelected(List<String> selectedValuesList) {
		List<String> toAdd = new ArrayList<>();
		toAdd.addAll(selectedValuesList);
		toAdd.removeAll(selectedTags.keySet());

		List<String> toRemove = new ArrayList<>();
		toRemove.addAll(selectedTags.keySet());
		toRemove.removeAll(selectedValuesList);

		for (String tag : toRemove) {
			library.removeConstraint(selectedTags.remove(tag));
		}

		for (String tag : toAdd) {
			TagConstraint constraint = new TagConstraint(tag);
			selectedTags.put(tag, constraint);
			library.addConstraint(constraint);
		}
	}

	public JPanel getPanel() {
		return panel;
	}

	@Subscribe
	public void knownTagListener(KnownTagsChangedEvent event) {
		Set<String> tags = event.getTags();

		List<Object> tagList = Arrays.asList(listModel.toArray());

		for (Object tag : tagList) {
			if (!tags.contains(tag)) {
				removeTag(tag.toString());
			}
		}
		for (Object tag : tags) {
			if (!tagList.contains(tag)) {
				addTag(tag.toString());
			}
		}
	}

	private void removeTag(String tag) {
		if (selectedTags.containsKey(tag)) {
			library.removeConstraint(selectedTags.get(tag));
			selectedTags.remove(tag);
		}

		listModel.removeElement(tag);
	}

	private void addTag(String tag) {
		listModel.addElement(tag);
	}
}
