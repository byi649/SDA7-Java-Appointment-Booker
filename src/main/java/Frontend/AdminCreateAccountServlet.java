package Frontend;

import Backend.Account;
import Backend.Administrator;
import DatabasePattern.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "AdminCreateAccountServlet", value = "/admin/admincreateaccount")
public class AdminCreateAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/admin/admincreateaccount.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        request.setAttribute("message", "");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String user = request.getParameter("username");
        String password = request.getParameter("password");
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate")); // TODO: check
        String postCode = request.getParameter("postCode");
        String type = request.getParameter("type");

        String view = "/admin/admincreateaccount.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        try {
            UnitOfWork.newCurrent();
            Administrator admin = (Administrator) Account.getCurrentAccount();

            if (user.isEmpty() || password.isEmpty()) {
                request.setAttribute("message", "Missing fields required");
            } else if (type.equals("user")) {
                admin.addVaccineRecipient(user, password, birthDate);
                request.setAttribute("message", "Vaccine recipient account created");
            } else if (type.equals("hcp")){
                if (postCode.isEmpty()) {
                    request.setAttribute("message", "Missing fields required");
                } else {
                    admin.addHealthCareProvider(user, password, postCode);

                    request.setAttribute("message", "HCP account created");
                }
            } else {
                request.setAttribute("message", "type needs to be either user or HCP");
            }

            if (!UnitOfWork.getCurrent().commit()) {
                request.setAttribute("message", "Could not create account");
            }
        } finally {
            UnitOfWork.setCurrent(null);
        }

        requestDispatcher.forward(request, response);
    }
}
