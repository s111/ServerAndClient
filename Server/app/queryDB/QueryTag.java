package queryDB;

import java.util.List;

import models.Image;
import models.Tag;

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
		List<Tag> tags = session.createQuery("FROM Tag").list();
		List<Tag> copy = ImmutableList.copyOf(tags);

		session.getTransaction().commit();

		return copy;
	}

	public List<Image> getImages(String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Image> images = session.createQuery("SELECT images FROM Tag")
				.list();
		List<Image> copy = ImmutableList.copyOf(images);

		session.getTransaction().commit();

		return copy;
	}

	public void tagImage(long id, String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);
		Tag tag = (Tag) session.byId(Tag.class).load(name);

		if (tag == null) {
			tag = new Tag();
			tag.setName(name);

			session.save(tag);
		}

		image.getTags().add(tag);

		session.getTransaction().commit();
	}

	public Tag getTag(String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Tag tag = (Tag) session.byId(Tag.class).load(name);

		session.getTransaction().commit();

		return tag;
	}
}
