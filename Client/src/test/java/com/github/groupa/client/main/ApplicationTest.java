package com.github.groupa.client.main;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.ImageUploader;
import com.github.groupa.client.gui.frames.MainFrame;
import com.google.common.eventbus.EventBus;

public class ApplicationTest {
	@Test
	public void run_application_expect_mainFrame_to_be_displayed() {
		MainFrame mockMainFrame = mock(MainFrame.class);

		Application application = new Application(new EventBus(),
				mockMainFrame, mock(ImageUploader.class),
				mock(ImageListFetcher.class));
		application.run();

		try {
			// Give thread time to be run
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		verify(mockMainFrame).display();
	}
}
