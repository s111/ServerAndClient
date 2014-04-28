package utils;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private HibernateUtil() {
	}

	private static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration().configure();
		Properties properties = configuration.getProperties();
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(properties).build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * For testing only!
	 * 
	 * @return A completely new instance of SessionFactory
	 */
	public static SessionFactory getNewSessionFactory() {
		return buildSessionFactory();
	}

	public static <T> T performAction(HibernateStrategy<T> strategy) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		try {
			T object = strategy.execute(session);
			session.getTransaction().commit();

			return object;
		} catch (Exception exception) {
			session.getTransaction().rollback();

			throw new RuntimeException(exception);
		}
	}
}