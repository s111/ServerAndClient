package controllers;

import static org.junit.Assert.assertEquals;
import models.ImageModel;

import org.junit.Test;

public class ImageRaterTest {
	@Test
	public void rate_image_1_with_rating_5_expect_rating_5() {
		assertEquals(5, ImageModel.get(1).rating);
	}
}
