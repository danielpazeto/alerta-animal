package pazeto.alertaanimal.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import pazeto.alertaanimal.DAO.DAO;
import pazeto.alertaanimal.DTO.Animal;
import pazeto.alertaanimal.DAO.AnimalDAO;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pazeto.alertaanimal on 5/25/17.
 */
public class AnimalServlet extends BaseServlet{

    private static final long serialVersionUID = 1L;

    private final Logger log = Logger.getLogger(AnimalServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String animalListJson = new ObjectMapper().writeValueAsString(new DAO().getAll(Animal.class));

        resp.getOutputStream().write(animalListJson.getBytes());
    }

}
