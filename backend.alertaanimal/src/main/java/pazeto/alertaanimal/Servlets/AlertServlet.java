package pazeto.alertaanimal.Servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.tools.web.BadHttpRequest;
import pazeto.alertaanimal.DAO.AlertDAO;
import pazeto.alertaanimal.DAO.DAO;
import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.DAO.AnimalDAO;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.model.FilterObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by pazeto.alertaanimal on 5/25/17.
 */
public class AlertServlet extends BaseServlet{

    private static final long serialVersionUID = 1L;

    private final Logger log = Logger.getLogger(AlertServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info(req.getParameterNames().toString());
//        alert/list?filter={%22range%22:10.0,%22animal%22:{%22id%22:0,%22name%22:%22Gorilla%22},%22lat%22:-22.876109,%22lng%22:-47.070388}
//        alert/list?filter={"range":0.3,"animal":{"id":2,"name":""},"lat":-22.876109,"lng":-47.070388}

        if(req.getParameter("filter") != null) {
            String filterJson = req.getParameter("filter");

            log.info(filterJson);
            try {
                FilterObject filter = new ObjectMapper().readValue(filterJson, FilterObject.class);

                List<Alert> alertList = new AlertDAO().searchAlerts(filter);
//                for (Alert a :
//                        alertList) {
//                    log.info("ALert lat: "+a.getLat());
//                    log.info("ALert animal: "+a.getAnimal().getName());
//                    log.info("ALert user: "+a.getUser().getName());
//                }

                String alertListJson = new ObjectMapper().writeValueAsString(alertList);
                log.info("alertListJson: "+alertListJson);
                resp.getOutputStream().write(alertListJson.getBytes());

            } catch (JsonMappingException e){
                log.warning(e.getMessage());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error " + e);
            } catch (IOException e){
                log.warning(e.getMessage());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid filter: " + e);
            }

        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("alert") != null) {
            String alertJsonString = req.getParameter("alert");
            log.info("Alert to save: " + alertJsonString);
            Alert alertToSaveOrUpdate = new ObjectMapper().readValue(alertJsonString, Alert.class);

            if(alertToSaveOrUpdate.getId() != null ){
                new DAO().update(alertToSaveOrUpdate);
            }else {
                new DAO().save(alertToSaveOrUpdate);
            }

            String alert = new ObjectMapper().writeValueAsString(alertToSaveOrUpdate);
            log.info("passouu1");
            resp.getOutputStream().write(alert.getBytes());
            log.info("passouu2");
            resp.setStatus(HttpServletResponse.SC_OK);
            log.info("passouu3");
        }else{
            throw new IOException("Invalid parameters sent to PUT method");
        }



    }
}
