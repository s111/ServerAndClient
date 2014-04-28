package utils;

import org.hibernate.Session;

public interface HibernateStrategy<T> {
	T execute(Session session);
}