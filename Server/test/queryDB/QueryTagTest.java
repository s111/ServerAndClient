package queryDB;

import static org.junit.Assert.assertEquals;
import helpers.WithDatabase;

import java.util.List;

import models.Image;
import models.Tag;

import org.hibernate.Session;
import org.junit.Test;

public class QueryTagTest extends WithDatabase {
	private static final String FILENAME = "filename";
	private static final String FILENAME1 = "filename1";
	private static final String FILENAME2 = "filename2";

	@Test
	public void getTags_create_two_tags_expect_two_tags() {
		Tag tag1 = new Tag();
		tag1.setName("tagname1");

		Tag tag2 = new Tag();
		tag2.setName("tagname2");

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(tag1);
		session.save(tag2);
		session.getTransaction().commit();

		QueryTag tagQueries = new QueryTag(sessionFactory);

		List<Tag> retrievedTags = tagQueries.getTags();

		assertEquals(2, retrievedTags.size());
	}

	@Test
	public void tagImage_expect_image_to_have_one_tag() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryTag tagQueries = new QueryTag(sessionFactory);
		tagQueries.tagImage(image.getId(), "tag");

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(1, retrievedImage.getTags().size());
	}

	@Test
	public void tagImage_twice_different_tags_expect_image_to_have_two_tags() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryTag tagQueries = new QueryTag(sessionFactory);
		tagQueries.tagImage(image.getId(), "tag1");
		tagQueries.tagImage(image.getId(), "tag2");

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(2, retrievedImage.getTags().size());
	}

	@Test
	public void tagImage_twice_same_tag_expect_image_to_have_one_tag() {
		Image image = new Image();
		image.setFilename(FILENAME);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image);

		QueryTag tagQueries = new QueryTag(sessionFactory);
		tagQueries.tagImage(image.getId(), "tag");
		tagQueries.tagImage(image.getId(), "tag");

		Image retrievedImage = imageQueries.getImage(image.getId());

		assertEquals(1, retrievedImage.getTags().size());
	}

	@Test
	public void getImages_tag_two_images_with_same_tag_expect_two_images() {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		QueryTag tagQueries = new QueryTag(sessionFactory);
		tagQueries.tagImage(image1.getId(), "tag");
		tagQueries.tagImage(image2.getId(), "tag");

		List<Image> images = tagQueries.getImages("tag");

		assertEquals(2, images.size());
	}
}
