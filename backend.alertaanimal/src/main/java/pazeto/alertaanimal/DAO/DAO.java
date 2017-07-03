package pazeto.alertaanimal.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by pazeto on 5/29/17.
 */
public class DAO {

    public <T> Long save(final T o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Long savedId = (Long) session.save(o);
        session.getTransaction().commit();
        session.close();
        return savedId;
    }

    public <T> void update(final T o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(o);
        session.getTransaction().commit();
        session.close();
    }


    public void delete(final Object object){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();
        session.close();
    }

    /***/
    public <T> T get(final Class<T> type, final Long id){

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        T object = (T) session.get(type, id);
        session.getTransaction().commit();
        session.close();
        return object;
    }

    /***/
    public <T> T merge(final T o)   {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        T object = (T) session.merge(o);
        session.getTransaction().commit();
        session.close();
        return object;
    }

    /***/
    public <T> void saveOrUpdate(final T o){

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(o);
        session.getTransaction().commit();
        session.close();
    }

    public <T> List<T> getAll(final Class<T> type) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<T> list = session.createCriteria(type).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public <T> List<T> query(final String query) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<T> list = session.createQuery(query).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

}
