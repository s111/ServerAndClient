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

import utils.HibernateStrategy;
import utils.HibernateUtil;

import com.google.common.collect.ImmutableList;

public class QueryTag {
	private SessionFactory sessionFactory;

	public QueryTag(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Tag> getTags() {
		HibernateStrategy<List<Tag>> strategy = new HibernateStrategy<List<Tag>>() {
			@Override
			public List<Tag> execute(Session session) {
				@SuppressWarnings("unchecked")
				List<Tag> tags = session.createCriteria(Tag.class).list();
				List<Tag> copy = ImmutableList.copyOf(tags);

				return copy;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public List<Image> getImages(final String name) {
		HibernateStrategy<List<Image>> strategy = new HibernateStrategy<List<Image>>() {
			@Override
			public List<Image> execute(Session session) {
				Tag tag = getTag(session, name);

				Set<Image> images = new HashSet<>();

				if (tag != null) {
					images = tag.getImages();

					Hibernate.initialize(images);
				}

				List<Image> copy = ImmutableList.copyOf(images);

				return copy;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	public void removeTag(final long id, final String name) {
		HibernateUtil.performAction(new HibernateStrategy<Void>() {
			@Override
			public Void execute(Session session) {
				Image image = (Image) session.byId(Image.class).load(id);

				Tag tag = new Tag();
				tag.setName(name);

				if (image != null) {
					image.getTags().remove(tag);
				}

				return null;
			}
		}, sessionFactory);
	}

	public void tagImage(final long id, final String name) {
		HibernateStrategy<Object[]> strategy = new HibernateStrategy<Object[]>() {
			@Override
			public Object[] execute(Session session) {
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

				Object[] imageAndTag = { image, tag };

				return imageAndTag;
			}
		};

		// TODO Try to do this without the Object[] workaround
		Object[] imageAndTag = HibernateUtil.performAction(strategy,
				sessionFactory);

		Image image = (Image) imageAndTag[0];
		Tag tag = (Tag) imageAndTag[1];

		if (image != null) {
			MetadataUtil.saveTagToFile(new File(image.getFilename()),
					tag.getName());
		}
	}

	public Tag getTag(final String name) {
		HibernateStrategy<Tag> strategy = new HibernateStrategy<Tag>() {
			@Override
			public Tag execute(Session session) {
				Tag tag = getTag(session, name);

				if (tag != null) {
					Hibernate.initialize(tag.getImages());
				}

				return tag;
			}
		};

		return HibernateUtil.performAction(strategy, sessionFactory);
	}

	private Tag getTag(Session session, String name) {
		Tag tag = (Tag) session.byId(Tag.class).load(name);

		return tag;
	}
}
