package Frontend;

import Backend.Account;
import Backend.Record;
import Backend.VaccineRecipient;
import DatabasePattern.UnitOfWork;
import DatabasePattern.VaccinationRecordMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ViewCertificateServlet", value = "/user/viewcertificate")
public class ViewCertificateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = null;
        ServletContext servletContext = getServletContext();

        if (Account.hasLoggedInSince()) {
            response.sendRedirect("logout");
            return;
        } else {
            view = "/user/viewcertificate.jsp";
            VaccineRecipient currentAccount = (VaccineRecipient) VaccineRecipient.getCurrentAccount();

            Record record = null;
            ArrayList<Record> recordList = null;
            recordList = VaccinationRecordMapper.findVaccinationRecordsByRecipientId(currentAccount.getId());

            if (recordList.size() == 0) {
                request.setAttribute("message", "No vaccination recorded.");
            } else {
                record = recordList.get(0);
                request.setAttribute("message", record.getDate().toString());
            }
        }
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
