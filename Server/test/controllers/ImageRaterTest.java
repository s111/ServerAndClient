package controllers;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import models.ImageModel;

import org.junit.Before;
import org.junit.Test;

public class ImageRaterTest {
	@Before
	public void startApp() {
		start(fakeApplication());
	}
	
	@Test
	public void rate_image_1_with_rating_5_expect_rating_5() {
		callAction(controllers.routes.ref.ImageRater.rate(1));
		
		assertEquals(5, ImageModel.get(1).rating);
	}
}
