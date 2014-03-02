package com.github.groupa.client.components;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {

	public FileChooser() {
		JFileChooser chooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				".jpg, .jpeg, .gif, .png", "jpg", "jpeg", "gif", "png");

		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: "
					+ chooser.getSelectedFile().getName());
		}
	}

}
