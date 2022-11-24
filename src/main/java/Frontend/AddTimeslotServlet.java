package Frontend;

import Backend.*;
import DatabasePattern.UnitOfWork;
import DatabasePattern.VaccinationMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@WebServlet(name = "AddTimeslotServlet", value = "/hcp/addtimeslot")
public class AddTimeslotServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/hcp/addtimeslot.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        request.setAttribute("message", "");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        LocalDateTime start = null;
        LocalDateTime end = null;
        start = LocalDateTime.parse(request.getParameter("start"), DateTimeFormatter.ISO_DATE_TIME);
        end = LocalDateTime.parse(request.getParameter("end"), DateTimeFormatter.ISO_DATE_TIME);
        String vaccine = request.getParameter("vaccine");

        String view = "/hcp/addtimeslot.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        if (Duration.between(LocalDateTime.now(), start).toDays() <= 6 * 31 && !Duration.between(start, end).isNegative()) {
            try {
                UnitOfWork.newCurrent();

                HealthCareProvider HCP = (HealthCareProvider) Account.getCurrentAccount();
                HCP.addTimeslots(start, end, VaccinationMapper.findVaccineByName(vaccine));

                if (UnitOfWork.getCurrent().commit()) {
                    request.setAttribute("message", "Timeslot added");
                } else {
                    request.setAttribute("message", "Could not add timeslot");
                }
            } catch (Exception e) {
                request.setAttribute("message", "Could not add timeslot");
            } finally {
                UnitOfWork.setCurrent(null);
            }
        } else {
            request.setAttribute("message", "Timeslot invalid (too far in future, or invalid times)");
        }

        requestDispatcher.forward(request, response);

    }
}
