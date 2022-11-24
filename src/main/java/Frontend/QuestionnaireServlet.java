package Frontend;

import Backend.*;
import DatabasePattern.LockManager;
import DatabasePattern.UnitOfWork;
import com.oracle.wls.shaded.org.apache.xpath.operations.Bool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "QuestionnaireServlet", value = "/user/questionnaire")
public class QuestionnaireServlet extends HttpServlet {

    private Timeslot timeslot = null;
    private Vaccine vaccine = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = null;
        ServletContext servletContext = getServletContext();

        String timeslotID = request.getParameter("timeslot");
        timeslot = Timeslot.getTimeslotByID(Integer.parseInt(timeslotID));
        System.out.println(timeslotID);
        System.out.println(timeslot);

        if (Account.hasLoggedInSince()) {
            response.sendRedirect("logout");
            return;
        } else if (!LockManager.getInstance().acquireLock(timeslotID)) {
            response.sendRedirect("userhome.jsp");
            return;
        } else {
            view = "/user/questionnaire.jsp";
            vaccine = timeslot.getVaccine();
            request.setAttribute("message", "");
        }
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String q1 = request.getParameter("q1");
        String q2 = request.getParameter("q2");

        String view = null;
        ServletContext servletContext = getServletContext();

        if (Account.hasLoggedInSince()) {
            response.sendRedirect("logout");
            return;
        } else {
            view = "/user/questionnaire.jsp";
            if (timeslot.getEligible(q1, q2)) {
                try {
                    UnitOfWork.newCurrent();
                    VaccineRecipient vaccineRecipient = (VaccineRecipient) Account.getCurrentAccount();
                    timeslot.setVaccineRecipient(vaccineRecipient);

                    if (UnitOfWork.getCurrent().commit()) {
                        request.setAttribute("message", "Timeslot enrollment successful - vaccine: " + vaccine.getVaccine() + ".");
                    } else {
                        request.setAttribute("message", "Timeslot enrollment unsuccessful");
                    }
                } catch (Exception e){
                    System.out.println(e);
                } finally {
                    UnitOfWork.setCurrent(null);
                }
            } else {
                request.setAttribute("message", "Timeslot enrollment unsuccessful - ineligible for vaccine");
            }
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        }
    }
}
