package queryDB;

import models.Image;
import models.Thumbnail;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class QueryThumbnail {
	private SessionFactory sessionFactory;

	public QueryThumbnail(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addThumbnail(long id, int size, String filename) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Image image = (Image) session.byId(Image.class).load(id);
		Thumbnail thumbnail = (Thumbnail) session.createQuery(
				"FROM Thumbnail WHERE size=" + size + " AND image.id=" + id)
				.uniqueResult();

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

		Thumbnail thumbnail = (Thumbnail) session.createQuery(
				"FROM Thumbnail WHERE size=" + size + " AND image.id=" + id)
				.uniqueResult();

		session.getTransaction().commit();

		return thumbnail;
	}
}
