package queryDB;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import metadata.MetadataUtil;
import models.Image;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.common.collect.ImmutableList;

public class QueryImage {
	private SessionFactory sessionFactory;

	public QueryImage(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Image getImage(long id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);

		if (image != null) {
			Hibernate.initialize(image.getTags());
			Hibernate.initialize(image.getThumbnails());
		}

		session.getTransaction().commit();

		return image;
	}

	public Image getImage(String filename) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.createQuery(
				"FROM Image WHERE filename='" + filename + "'").uniqueResult();

		session.getTransaction().commit();

		return image;
	}

	public Image getNextImage(long id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session
				.createQuery("FROM Image WHERE id > " + id + " ORDER BY id ASC")
				.setMaxResults(1).uniqueResult();

		session.getTransaction().commit();

		return image;
	}

	public Image getPreviousImage(long id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session
				.createQuery(
						"FROM Image WHERE id < " + id + " ORDER BY id DESC")
				.setMaxResults(1).uniqueResult();

		session.getTransaction().commit();

		return image;
	}

	public Image getFirstImage() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.createQuery("FROM Image ORDER BY id ASC")
				.setMaxResults(1).uniqueResult();

		session.getTransaction().commit();

		return image;
	}

	public Image getLastImage() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session
				.createQuery("FROM Image ORDER BY id DESC").setMaxResults(1)
				.uniqueResult();

		session.getTransaction().commit();

		return image;
	}

	public List<Image> getImages() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Image> images = session.createQuery("FROM Image").list();
		List<Image> copy = ImmutableList.copyOf(images);

		session.getTransaction().commit();

		return copy;
	}

	public List<Image> getImages(int offset, int limit) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Image> images = session.createQuery("FROM Image ORDER BY id ASC")
				.setFirstResult(offset).setMaxResults(limit).list();
		List<Image> copy = ImmutableList.copyOf(images);

		session.getTransaction().commit();

		return copy;
	}

	public void addImage(Image image) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		session.save(image);

		session.getTransaction().commit();

		MetadataUtil.loadMetadataFromFile(image.getId());
	}

	public void describeImage(long id, String description) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);

		if (image != null) {
			image.setDescription(description);
		}

		session.getTransaction().commit();

		MetadataUtil.saveDescriptionToFile(new File(image.getFilename()),
				description);
	}

	public void rateImage(long id, int rating) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);

		if (image != null) {
			image.setRating(rating);
		}

		session.getTransaction().commit();

		MetadataUtil.saveRatingToFile(new File(image.getFilename()), rating);
	}

	public void setDate(long id, Date date) {
		long time = date.getTime();
		Timestamp timestamp = new Timestamp(time);

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);

		if (image != null) {
			image.setDate(timestamp);
		}

		session.getTransaction().commit();
	}
}