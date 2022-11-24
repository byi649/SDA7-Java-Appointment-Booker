package Frontend;

import Backend.Account;
import Backend.Administrator;
import Backend.HealthCareProvider;
import Backend.VaccineRecipient;
import DatabasePattern.AccountMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ViewAllVaccineRecipientsServlet", value = "/admin/viewallvaccinerecipients")
public class ViewAllVaccineRecipientsServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/admin/viewallvaccinerecipients.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        ArrayList<VaccineRecipient> accounts = null;
        try {
            accounts = AccountMapper.findAllVaccineRecipients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("accounts", accounts);

        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/admin/viewallvaccinerecipients.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        String vaccine = request.getParameter("vaccine");

        ArrayList<VaccineRecipient> accounts = null;
        try {
            accounts = AccountMapper.findAllVaccineRecipients();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        accounts.removeIf(account -> account.getVaccine() == null || !account.getVaccine().getVaccine().equals(vaccine));

        request.setAttribute("accounts", accounts);

        requestDispatcher.forward(request, response);

    }
}
