package queryDB;

import java.util.List;

import models.Image;
import models.Thumbnail;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import utils.HibernateStrategy;
import utils.HibernateUtil;

public class QueryThumbnail {
	private SessionFactory sessionFactory;

	public QueryThumbnail(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addThumbnail(final long id, final int size,
			final String filename) {
		HibernateUtil.performAction(new HibernateStrategy<Void>() {
			@Override
			public Void execute(Session session) {
				Image image = (Image) session.byId(Image.class).load(id);
				Thumbnail thumbnail = (Thumbnail) session
						.createCriteria(Thumbnail.class)
						.add(Restrictions.eq("image.id", id))
						.add(Restrictions.eq("size", size)).uniqueResult();

				if (thumbnail != null) {
					return null;
				}

				thumbnail = new Thumbnail();
				thumbnail.setSize(size);
				thumbnail.setFilename(filename);

				session.save(thumbnail);

				image.getThumbnails().add(thumbnail);

				return null;
			}
		}, sessionFactory);
	}

	public Thumbnail getThumbnail(final long id, final int size) {
		HibernateStrategy<Thumbnail> strategy = new HibernateStrategy<Thumbnail>() {
			@Override
			public Thumbnail execute(Session session) {
				Thumbnail thumbnail = (Thumbnail) session
						.createCriteria(Thumbnail.class)
						.add(Restrictions.eq("image.id", id))
						.add(Restrictions.eq("size", size)).uniqueResult();

				return thumbnail;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public void deleteAllThumbnails(final long id) {
		HibernateUtil.performAction(new HibernateStrategy<Void>() {
			@Override
			public Void execute(Session session) {
				@SuppressWarnings("unchecked")
				List<Thumbnail> thumbnails = (List<Thumbnail>) session
						.createCriteria(Thumbnail.class)
						.add(Restrictions.eq("image.id", id)).list();

				for (Thumbnail thumbnail : thumbnails) {
					session.delete(thumbnail);
				}

				return null;
			}
		}, sessionFactory);
	}
}
