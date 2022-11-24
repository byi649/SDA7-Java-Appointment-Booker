package Frontend;

import Backend.Account;
import Backend.Administrator;
import Backend.VaccineRecipient;
import DatabasePattern.UnitOfWork;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/register.jsp";
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
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));

        String view = "/register.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        try {
            UnitOfWork.newCurrent();

            if (user.isEmpty() || password.isEmpty()) {
                request.setAttribute("message", "Missing fields required");
            } else {
                VaccineRecipient.createAccount(user, password, birthDate);
                request.setAttribute("message", "Vaccine recipient account created");
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
