package Frontend;

import Backend.Account;
import DatabasePattern.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/index")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Subject currentUser = SecurityUtils.getSubject();
        String view = "/index.jsp";
        if(currentUser.hasRole("2")) {
            view = "/admin/adminhome";
        } else if(currentUser.hasRole("1")) {
            view = "/user/userHome";
        } else if(currentUser.hasRole("3")) {
            view = "/user/hcphome";
        }

        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
