package Frontend;

import Backend.Account;
import Backend.Administrator;
import Backend.HealthCareProvider;
import DatabasePattern.UnitOfWork;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AddVaccineTypeServlet", value = "/admin/addvaccinetype")
public class AddVaccineTypeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/admin/addvaccinetype.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        request.setAttribute("message", "");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String type = request.getParameter("type");
        String view = "/admin/addvaccinetype.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        try {
            UnitOfWork.newCurrent();
            Administrator admin = (Administrator) Account.getCurrentAccount();
            admin.addVaccine(type);
            if (UnitOfWork.getCurrent().commit()) {
                request.setAttribute("message", "Vaccine type added");
            } else {
                request.setAttribute("message", "Could not add vaccine type");
            }
        } finally {
            UnitOfWork.setCurrent(null);
        }

        requestDispatcher.forward(request, response);
    }
}
