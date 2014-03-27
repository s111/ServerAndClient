package com.github.groupa.client.main;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class NewApplicationTest {
	@Test
	public void run_expect_guiThread_to_be_run() {
		Runnable guiThreadMock = mock(Runnable.class);

		NewApplication application = new NewApplication(guiThreadMock);
		application.run();

		verify(guiThreadMock).run();
	}
}
