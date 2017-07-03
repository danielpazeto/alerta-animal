package pazeto.alertaanimal.DAO;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DTO.User;

import java.util.List;

/**
 * Created by pazeto on 5/27/17.
 */
public class UserDAO extends DAO {

    public UserDAO(){

    }

    public User getUserByFacebookId(Long facebookId, boolean loadAlerts) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(User.class);

        criteria.add(Restrictions.eq(User.Field.facebookId.name(), facebookId));

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        User user = (User) criteria.uniqueResult();
        if(user!=null){
            Hibernate.initialize(user.getAlerts());
        }
        session.getTransaction().commit();
        session.close();

        return user;
    }
}
