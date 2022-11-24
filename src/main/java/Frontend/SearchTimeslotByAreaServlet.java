package Frontend;

import Backend.Account;
import Backend.Timeslot;
import DatabasePattern.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "SearchTimeslotByAreaServlet", value = "/user/searchtimeslotbyarea")
public class SearchTimeslotByAreaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = null;
        ServletContext servletContext = getServletContext();
        view = "/user/searchtimeslotbyarea.jsp";

        if (Account.hasLoggedInSince()) {
            response.sendRedirect("logout");
            return;
        } else {
            ArrayList<Timeslot> timeslots = new ArrayList<>();
            request.setAttribute("timeslots", timeslots);
        }
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = null;
        ServletContext servletContext = getServletContext();

        if (Account.hasLoggedInSince()) {
            response.sendRedirect("logout");
        } else {
            view = "/user/searchtimeslotbyarea.jsp";
            ArrayList<Timeslot> timeslots = new ArrayList<>();
            String postcode = request.getParameter("Postcode");

            timeslots = Timeslot.filterTimeslotByArea(postcode);
            timeslots.removeIf(ts -> ts.getRecipient() != null);

            request.setAttribute("timeslots", timeslots);
        }
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
