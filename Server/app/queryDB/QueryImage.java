package queryDB;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import metadata.MetadataUtil;
import metadata.XmpWriter;
import models.Image;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import utils.HibernateStrategy;
import utils.HibernateUtil;

import com.google.common.collect.ImmutableList;

public class QueryImage {
	private SessionFactory sessionFactory;

	public QueryImage(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Image getImage(final long id) {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.byId(Image.class).load(id);

				if (image != null) {
					Hibernate.initialize(image.getTags());
					Hibernate.initialize(image.getThumbnails());
				}

				return image;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public Image getImage(final String filename) {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.createCriteria(Image.class)
						.add(Restrictions.eq("filename", filename))
						.uniqueResult();

				return image;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public Image getNextImage(final long id) {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.createCriteria(Image.class)
						.add(Restrictions.gt("id", id))
						.addOrder(Order.asc("id")).setMaxResults(1)
						.uniqueResult();

				return image;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public Image getPreviousImage(final long id) {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.createCriteria(Image.class)
						.add(Restrictions.lt("id", id))
						.addOrder(Order.desc("id")).setMaxResults(1)
						.uniqueResult();

				return image;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public Image getFirstImage() {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.createCriteria(Image.class)
						.addOrder(Order.asc("id")).setMaxResults(1)
						.uniqueResult();

				return image;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public Image getLastImage() {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.createCriteria(Image.class)
						.addOrder(Order.desc("id")).setMaxResults(1)
						.uniqueResult();

				return image;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public List<Image> getImages() {
		HibernateStrategy<List<Image>> strategy = new HibernateStrategy<List<Image>>() {
			@Override
			public List<Image> execute(Session session) {
				@SuppressWarnings("unchecked")
				List<Image> images = session.createCriteria(Image.class)
						.addOrder(Order.asc("id")).list();
				List<Image> copy = ImmutableList.copyOf(images);

				return copy;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public List<Image> getImages(final int offset, final int limit) {
		HibernateStrategy<List<Image>> strategy = new HibernateStrategy<List<Image>>() {
			@Override
			public List<Image> execute(Session session) {
				@SuppressWarnings("unchecked")
				List<Image> images = session.createCriteria(Image.class)
						.addOrder(Order.asc("id")).setFirstResult(offset)
						.setMaxResults(limit).list();
				List<Image> copy = ImmutableList.copyOf(images);

				return copy;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public void addImage(final Image image) {
		HibernateUtil.performAction(new HibernateStrategy<Void>() {
			@Override
			public Void execute(Session session) {
				session.save(image);

				return null;
			}
		}, sessionFactory);

		Long id = image.getId();

		MetadataUtil.loadExifMetadataFromFile(id);
		MetadataUtil.loadXmpMetadataFromFile(id);
	}

	public void describeImage(final long id, final String description) {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.byId(Image.class).load(id);

				if (image != null) {
					image.setDescription(description);
				}

				return image;
			}
		};

		Image image = HibernateUtil.performAction(strategy, sessionFactory);

		if (image != null) {
			File file = new File(image.getFilename());

			MetadataUtil.saveDescriptionToFile(file, description);

			XmpWriter.setDescription(file, description);
		}
	}

	public void rateImage(final long id, final int rating) {
		HibernateStrategy<Image> strategy = new HibernateStrategy<Image>() {
			@Override
			public Image execute(Session session) {
				Image image = (Image) session.byId(Image.class).load(id);

				if (image != null) {
					image.setRating(rating);
				}

				return image;
			}
		};

		Image image = HibernateUtil.performAction(strategy, sessionFactory);

		if (image != null) {
			File file = new File(image.getFilename());

			MetadataUtil.saveRatingToFile(file, rating);

			XmpWriter.setRating(file, rating);
		}
	}

	public void setDateTaken(final long id, Date date) {
		long time = date.getTime();
		final Timestamp timestamp = new Timestamp(time);

		HibernateUtil.performAction(new HibernateStrategy<Void>() {
			@Override
			public Void execute(Session session) {
				Image image = (Image) session.byId(Image.class).load(id);

				if (image != null) {
					image.setDateTaken(timestamp);
				}

				return null;
			}
		}, sessionFactory);
	}
}