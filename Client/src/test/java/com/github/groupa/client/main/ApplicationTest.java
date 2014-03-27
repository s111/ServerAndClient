package com.github.groupa.client.main;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.github.groupa.client.Library;
import com.github.groupa.client.gui.MainFrame;

public class ApplicationTest {
	@Test
	public void run_application_expect_mainFrame_to_be_displayed() {
		MainFrame mockMainFrame = mock(MainFrame.class);

		Application application = new Application(mock(Library.class),
				mockMainFrame);
		application.run();

		verify(mockMainFrame).display();
	}
}
