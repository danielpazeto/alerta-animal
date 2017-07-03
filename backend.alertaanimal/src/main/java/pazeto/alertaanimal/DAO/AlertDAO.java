package pazeto.alertaanimal.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.model.FilterObject;

import java.util.List;

/**
 * Created by pazeto on 6/5/17.
 */
public class AlertDAO extends DAO{


    /**
     *
     * @param filter
     * @return
     */
    public List searchAlerts(FilterObject filter) {

        double distance = filter.getRange();
        double lon = filter.getLng();
        double lat = filter.getLat();
        Animal animal = filter.getAnimal();


        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(Alert.class);

        //
        // 1 Grad lat = 111 km
        // 1 grad lon = cos(lat) * 111
        //
        final double KM_IN_ONE_LAT = 111.0;

        double t1 = distance / Math.abs(Math.cos(Math.toRadians(lat)) * KM_IN_ONE_LAT);
        double t2 = distance / KM_IN_ONE_LAT;

        double lonA = lon - t1;
        double lonB = lon + t1;

        double latA = lat - t2;
        double latB = lat + t2;

        Criterion c1 = Restrictions.between("lng", lonA, lonB);
        Criterion c2 = Restrictions.between("lat", latA, latB);
        criteria.add(c1);
        criteria.add(c2);

        if(animal != null) {
            //TODO implement the logix for more than one animal id
            Long[] animalsIds = { filter.getAnimal().getId() };

            Criterion animalCrit = Restrictions.in("animal.id", animalsIds);
            criteria.add(animalCrit);
        }

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List list = criteria.list();

        session.getTransaction().commit();
        session.close();

        return list;
    }
}
