package queryDB;

import static org.junit.Assert.assertEquals;
import helpers.WithDatabase;

import java.util.Set;

import models.Image;
import models.Thumbnail;

import org.junit.Test;

public class QueryThumbnailTest extends WithDatabase {
	private static final String FILENAME = "filename";
	private static final String FILENAME1 = "filename1";
	private static final String FILENAME2 = "filename2";

	@Test
	public void addThumbnail_expect_image_to_have_one_thumbnail() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryThumbnail thumbnailQueries = new QueryThumbnail(sessionFactory);
		thumbnailQueries.addThumbnail(image.getId(), 0, FILENAME1);

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(1, retrievedImage.getThumbnails().size());
	}

	@Test
	public void addThumbnail_twice_different_size_expect_image_to_have_two_thumbnails() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryThumbnail thumbnailQueries = new QueryThumbnail(sessionFactory);
		thumbnailQueries.addThumbnail(image.getId(), 0, FILENAME1);
		thumbnailQueries.addThumbnail(image.getId(), 1, FILENAME2);

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(2, retrievedImage.getThumbnails().size());
	}

	@Test
	public void addThumbnail_twice_same_size_same_filename_expect_image_to_have_one_thumbnail() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryThumbnail thumbnailQueries = new QueryThumbnail(sessionFactory);
		thumbnailQueries.addThumbnail(image.getId(), 0, FILENAME1);
		thumbnailQueries.addThumbnail(image.getId(), 0, FILENAME1);

		Image retrievedImage = imageQueries.getImage(image.getId());

		Set<Thumbnail> thumbnails = retrievedImage.getThumbnails();
		Thumbnail retrievedThumbnail = thumbnails.iterator().next();

		assertEquals(1, retrievedImage.getThumbnails().size());
		assertEquals(FILENAME1, retrievedThumbnail.getFilename());
	}

	@Test
	public void getThumbnail_add_one_thumbnail_expect_one_thumbnail() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryThumbnail thumbnailQueries = new QueryThumbnail(sessionFactory);
		thumbnailQueries.addThumbnail(image.getId(), 0, FILENAME1);

		Thumbnail retrievedThumbnail = thumbnailQueries.getThumbnail(
				image.getId(), 0);

		assertEquals(FILENAME1, retrievedThumbnail.getFilename());
	}
}
