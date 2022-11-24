package Frontend;

import Backend.Timeslot;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "AdminViewAllTimeslots", value = "/admin/adminviewalltimeslots")
public class AdminViewAllTimeslotsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/admin/adminviewalltimeslots.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        ArrayList<Timeslot> timeslots;
        timeslots = Timeslot.getAllTimeslots();
        request.setAttribute("timeslots", timeslots);

        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
