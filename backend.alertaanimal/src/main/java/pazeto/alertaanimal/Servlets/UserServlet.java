package pazeto.alertaanimal.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import pazeto.alertaanimal.DAO.DAO;
import pazeto.alertaanimal.DAO.UserDAO;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.DTO.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by pazeto.alertaanimal on 5/25/17.
 */
public class UserServlet extends BaseServlet{

    private static final long serialVersionUID = 1L;

    private final Logger log = Logger.getLogger(UserServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userJson = req.getParameter("user");
        log.info("Login Parameters: "+userJson);

        if(userJson != null) {
            try {
                User user = new ObjectMapper().readValue(userJson, User.class);
                UserDAO userDao = new UserDAO();
                User dbUser = userDao.getUserByFacebookId(user.getFacebookId(), true);
                if (dbUser == null) {
                    log.info("User not found, creating the user : "+ user.toString());
                    userDao.save(user);
                    dbUser = user;
                    log.info(String.format("User has been created with id %s and faceId %s",
                            dbUser.getId(), dbUser.getFacebookId()));
                }

                String userJsonResp = new ObjectMapper().writeValueAsString(dbUser);
                log.info("Json res: "+userJsonResp);
                resp.getOutputStream().write(userJsonResp.getBytes());
            }catch (IOException e){
                log.warning("Invalid parameters. Json parsing error: "+e.getMessage());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid parameters. Json parsing error: "+e.getMessage());
            }
        }else{
            log.warning("Invalid parameters. ");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters.");
        }
    }

}
