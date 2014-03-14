package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import retrofit.mime.TypedFile;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.Main;
import com.github.groupa.client.MainFrame;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.ImageView;
import com.github.groupa.client.views.View;

public class MenuBar {
	private JMenuBar menuBar;
	private JMenuItem importItem, imageViewItem;

	private MainFrame mainFrame;
	private RESTService restService;
	private SingleLibrary library;
	private ImageView imageView;

	@Inject
	public MenuBar(MainFrame mainFrame, SingleLibrary library, RESTService restService, ImageView imageView) {
		this.mainFrame = mainFrame;
		this.library = library;
		this.restService = restService;
		this.imageView = imageView;
		
		setUpMenuBar();
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	private void setUpMenuBar() {
		menuBar = new JMenuBar();

		importItem = new JMenuItem("Import image");
		imageViewItem = new JMenuItem("Activate ImageView");

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(importItem);
		fileMenu.add(imageViewItem);

		menuBar.add(fileMenu);

		addActionListeners();

	}

	private void addActionListeners() {
		importItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".jpg, .jpeg, .gif, .png", "jpg", "jpeg", "gif", "png");

				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					ImageInfo imageInfo = restService
							.uploadImage(new TypedFile("image/*", chooser
									.getSelectedFile()));

					ImageObjectFactory imageObjectFactory = Main.injector
							.getInstance(ImageObjectFactory.class);
					ImageObject imageObject = imageObjectFactory
							.create(imageInfo.getImage().getId());

					library.add(imageObject);

					JOptionPane.showMessageDialog(mainFrame.getFrame(),
							"Image uploaded!");
				}
			}
		});

		imageViewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mainFrame.showView(View.IMAGE_VIEW);
				imageView.activateImageView();
			}
		});
	}
}