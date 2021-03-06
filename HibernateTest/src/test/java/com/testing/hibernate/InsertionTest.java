package com.testing.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.testing.hibernate.Event;

import junit.framework.TestCase;


/**
 * Illustrates the use of Hibernate native APIs. The code here is unchanged from the {@code basic}
 * example, the only difference being the use of annotations to supply the metadata instead of
 * Hibernate mapping files.
 *
 * @author Steve Ebersole
 */
public class InsertionTest extends TestCase {

  private SessionFactory sessionFactory;

  @Override
  protected void setUp() throws Exception {
    // A SessionFactory is set up once for an application!
    // configures settings from hibernate.cfg.xml
    final StandardServiceRegistry registry =
        new StandardServiceRegistryBuilder().configure().build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      // The registry would be destroyed by the SessionFactory, but we had trouble building the
      // SessionFactory
      // so destroy it manually.
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  @SuppressWarnings({"unchecked"})
  public void testBasicUsage() {

    // create a couple of events...
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.save(new Event("Our very first event!", new Date()));
    session.save(new Event("A follow up event", new Date()));
    session.getTransaction().commit();
    session.close();

    // now lets pull events from the databaset and list them
    session = sessionFactory.openSession();
    session.beginTransaction();
    List result = session.createQuery("from Event").list();

    System.out.println("Testing events");

    for (Event event : (List<Event>) result) {
      System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
    }
    
    session.getTransaction().commit();
    session.close();
    
    assertEquals(result.size(), 2);
  }
}
