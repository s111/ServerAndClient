package queryDB;

import models.Image;
import models.Thumbnail;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class QueryThumbnail {
	private SessionFactory sessionFactory;

	public QueryThumbnail(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addThumbnail(long id, int size, String filename) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);
		Thumbnail thumbnail = getThumbnail(session, id, size);

		if (thumbnail != null) {
			session.delete(thumbnail);
		}

		thumbnail = new Thumbnail();
		thumbnail.setSize(size);
		thumbnail.setFilename(filename);

		session.save(thumbnail);

		image.getThumbnails().add(thumbnail);

		session.getTransaction().commit();
	}

	public Thumbnail getThumbnail(long id, int size) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Thumbnail thumbnail = getThumbnail(session, id, size);

		session.getTransaction().commit();

		return thumbnail;
	}

	private Thumbnail getThumbnail(Session session, long id, int size) {
		return (Thumbnail) session.createCriteria(Thumbnail.class)
				.add(Restrictions.eq("image.id", id))
				.add(Restrictions.eq("size", size)).uniqueResult();
	}
}
