package queryDB;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import metadata.MetadataUtil;
import models.Image;
import models.Tag;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.common.collect.ImmutableList;

public class QueryTag {
	private SessionFactory sessionFactory;

	public QueryTag(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Tag> getTags() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Tag> tags = session.createCriteria(Tag.class).list();
		List<Tag> copy = ImmutableList.copyOf(tags);

		session.getTransaction().commit();

		return copy;
	}

	public List<Image> getImages(String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Tag tag = getTag(session, name);

		Set<Image> images = new HashSet<>();

		if (tag != null) {
			images = tag.getImages();

			Hibernate.initialize(images);
		}

		List<Image> copy = ImmutableList.copyOf(images);

		session.getTransaction().commit();

		return copy;
	}

	public void tagImage(long id, String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);
		Tag tag = getTag(session, name);

		if (tag == null) {
			tag = new Tag();
			tag.setName(name);

			session.save(tag);
		}

		if (image != null) {
			image.getTags().add(tag);
		}

		session.getTransaction().commit();

		if (image != null) {
			MetadataUtil.saveTagToFile(new File(image.getFilename()),
					tag.getName());
		}
	}

	public Tag getTag(String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Tag tag = getTag(session, name);

		if (tag != null) {
			Hibernate.initialize(tag.getImages());
		}

		session.getTransaction().commit();

		return tag;
	}

	private Tag getTag(Session session, String name) {
		Tag tag = (Tag) session.byId(Tag.class).load(name);

		return tag;
	}
}
