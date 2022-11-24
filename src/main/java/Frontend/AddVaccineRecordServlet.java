package Frontend;

import Backend.*;
import DatabasePattern.TimeslotMapper;
import DatabasePattern.UnitOfWork;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "AddVaccineRecordServlet", value = "/hcp/addvaccinerecord")
public class AddVaccineRecordServlet extends HttpServlet {

    private String id = null;
    private Timeslot timeslot = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/hcp/addvaccinerecord.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        id = request.getParameter("id");
        timeslot = Timeslot.getTimeslotByID(Integer.parseInt(id));

        request.setAttribute("message", "");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String view = "/hcp/addvaccinerecord.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        try {
            UnitOfWork.newCurrent();
            HealthCareProvider HCP = (HealthCareProvider) Account.getCurrentAccount();
            HCP.addRecord(timeslot.getRecipient(), timeslot, timeslot.getVaccine());
            if (UnitOfWork.getCurrent().commit()) {
                request.setAttribute("message", "Vaccine record added");
            } else {
                request.setAttribute("message", "Could not add vaccine record");
            }
        } finally {
            UnitOfWork.setCurrent(null);
        }

        requestDispatcher.forward(request, response);
    }
}
