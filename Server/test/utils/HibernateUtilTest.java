package utils;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.hibernate.SessionFactory;
import org.junit.Test;

public class HibernateUtilTest {
	@Test
	public void getSessionFactory_twice_expect_same_object() {
		SessionFactory factory1 = HibernateUtil.getSessionFactory();
		SessionFactory factory2 = HibernateUtil.getSessionFactory();

		assertSame(factory1, factory2);
	}

	@Test
	public void getNewSessionFactory_twice_expect_different_objects() {
		SessionFactory factory1 = HibernateUtil.getNewSessionFactory();
		SessionFactory factory2 = HibernateUtil.getNewSessionFactory();

		assertNotSame(factory1, factory2);
	}
}
