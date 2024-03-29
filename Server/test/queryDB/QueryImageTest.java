package queryDB;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import helpers.WithDatabase;

import java.util.List;

import models.Image;

import org.hibernate.Session;
import org.junit.Test;

public class QueryImageTest extends WithDatabase {
	private static final String FILENAME = "filename";
	private static final String FILENAME1 = "filename1";
	private static final String FILENAME2 = "filename2";
	private static final String FILENAME3 = "filename3";
	private static final String FILENAME4 = "filename4";
	private static final String DESCRIPTION = "description";

	private static final int RATING = 5;

	@Test
	public void getImage_add_one_image_expect_one_image() {
		Image image = new Image();
		image.setFilename(FILENAME);

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(image);
		session.getTransaction().commit();

		QueryImage imageQueries = new QueryImage(sessionFactory);

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertNotNull(retrievedImage);
		assertEquals(FILENAME, retrievedImage.getFilename());
	}

	@Test
	public void addImage_expect_one_image_in_db() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertNotNull(retrievedImage);
		assertEquals(FILENAME, retrievedImage.getFilename());
	}

	@Test
	public void getImages_add_two_images_expect_two_images_in_db() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		List<Image> images = imageQueries.getImages();

		assertNotNull(images);
		assertEquals(2, images.size());
		assertEquals(FILENAME1, images.get(0).getFilename());
		assertEquals(FILENAME2, images.get(1).getFilename());
	}

	@Test
	public void getImages_add_4_images_with_offset_2_limit_2_expect_2_images() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		Image image3 = new Image();
		image3.setFilename(FILENAME3);

		Image image4 = new Image();
		image4.setFilename(FILENAME4);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);
		imageQueries.addImage(image3);
		imageQueries.addImage(image4);

		List<Image> images = imageQueries.getImages(2, 2);

		assertNotNull(images);
		assertEquals(2, images.size());
		assertEquals(FILENAME3, images.get(0).getFilename());
		assertEquals(FILENAME4, images.get(1).getFilename());
	}

	@Test
	public void describeImage_expect_image_to_have_description() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);
		imageQueries.describeImage(image.getId(), DESCRIPTION);

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(DESCRIPTION, retrievedImage.getDescription());
	}

	@Test
	public void rateImage_expect_image_to_have_rating() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);
		imageQueries.rateImage(image.getId(), RATING);

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(RATING, (int) retrievedImage.getRating());
	}

	@Test
	public void getFirstImage_expect_first_image() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		Image firstImage = imageQueries.getFirstImage();

		assertThat(firstImage.getId()).isLessThan(image2.getId());
	}

	@Test
	public void getLastImage_expect_last_image() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		Image lastImage = imageQueries.getLastImage();

		assertThat(lastImage.getId()).isGreaterThan(image1.getId());
	}

	@Test
	public void getNextImage_expect_next_image() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		Image nextImage = imageQueries.getNextImage(image1.getId());

		assertEquals(image2.getId(), nextImage.getId());
	}

	@Test
	public void getPreviousImage_expect_next_previous() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		Image previousImage = imageQueries.getPreviousImage(image2.getId());

		assertEquals(image1.getId(), previousImage.getId());
	}

	@Test
	public void getImage_by_filename_expect_one_image() {
		Image image = new Image();
		image.setFilename(FILENAME);

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(image);
		session.getTransaction().commit();

		QueryImage imageQueries = new QueryImage(sessionFactory);

		Image retrievedImage = imageQueries.getImage(FILENAME);

		assertNotNull(retrievedImage);
		assertEquals(FILENAME, retrievedImage.getFilename());
	}
}
