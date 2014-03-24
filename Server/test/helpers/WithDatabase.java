package helpers;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;

import utils.HibernateUtil;

public class WithDatabase {

	protected SessionFactory sessionFactory;

	public WithDatabase() {
		super();
	}

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getNewSessionFactory();
	}

	@After
	public void tearDown() {
		sessionFactory.close();
	}

}