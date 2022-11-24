package Frontend;

import Backend.*;
import DatabasePattern.AccountMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ViewAllUsersServlet", value = "/admin/viewallusers")
public class ViewAllUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/admin/viewallusers.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);

        ArrayList<Account> accounts = null;
        try {
            accounts = AccountMapper.findAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> printableAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account instanceof VaccineRecipient){
                printableAccounts.add(account.getName() + ", Recipient");
            }
            else if (account instanceof Administrator){
                printableAccounts.add(account.getName() + ", Administrator");
            }
            else if (account instanceof HealthCareProvider){
                printableAccounts.add(account.getName() + ", Health Care Provider");
            }
        }
        request.setAttribute("accounts", printableAccounts);

        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
