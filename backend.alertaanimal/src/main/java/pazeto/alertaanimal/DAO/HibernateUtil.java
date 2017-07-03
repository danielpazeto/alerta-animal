package pazeto.alertaanimal.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.DTO.User;

import java.util.List;
import java.util.logging.Logger;


public class HibernateUtil {


    private static SessionFactory sessionFactory ;
	
	static {
//        Logger.getLogger("sad").info("HIBERANDANDAWNDWDAW");
        try {
            Configuration configuration = new Configuration().configure();
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        }catch (Exception e){
            e.printStackTrace();
            Logger.getLogger("sad").info("");
        }
        //Comment here to not create the fake DB
        createFakeDatabase();
     }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void createFakeDatabase()
    {
        if(new UserDAO().getAll(User.class).size() == 0) {
            User user = new User();
            user.setName("Eduardo Cunha");
            new UserDAO().saveOrUpdate(user);
            user = new UserDAO().getAll(User.class).get(0);


            Animal dog = new Animal("Dog");
            Animal capivara = new Animal("Capivara");
            Animal cow = new Animal("Boi");
            new AnimalDAO().saveOrUpdate(dog);
            new AnimalDAO().saveOrUpdate(capivara);
            new AnimalDAO().saveOrUpdate(cow);




            float ampLat = (float) -22.714935;
            float ampLong = (float) -46.778938;

            float campLat = (float) -22.876323;
            float campLong = (float) -47.070698;

            AnimalDAO animalDao = new AnimalDAO();
            List<Animal> animals = animalDao.getAll(Animal.class);
            for (Animal animal :animals) {
                Alert alertAmparo = new Alert(animal, user,ampLat, ampLong, "Amparo",true);
                Alert alertCampinas = new Alert(animal, user, campLat, campLong, "Campinas", false);

                new DAO().saveOrUpdate(alertAmparo);
                new DAO().saveOrUpdate(alertCampinas);

                ampLat+=0.002000;
                ampLong+= 0.002000;

                campLat+=0.002000;
                campLong+= 0.002000;
            }


        }

    }
}



