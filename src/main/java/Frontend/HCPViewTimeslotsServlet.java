package Frontend;

import Backend.Account;
import Backend.HealthCareProvider;
import Backend.Timeslot;
import DatabasePattern.TimeslotMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "HCPViewTimeslotsServlet", value = "/hcp/viewtimeslots")
public class HCPViewTimeslotsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/hcp/viewtimeslots.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        HealthCareProvider HCP = (HealthCareProvider) Account.getCurrentAccount();
        ArrayList<Timeslot> timeslots = TimeslotMapper.findBookedSlotsByHCP(HCP.getName());

        request.setAttribute("timeslots", timeslots);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
