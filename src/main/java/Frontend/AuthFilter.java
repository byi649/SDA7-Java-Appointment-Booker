package Frontend;

import Backend.Account;
import DatabasePattern.UnitOfWork;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AuthFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("Filtered");
        try {
            UnitOfWork.newCurrent();
            Account.getCurrentAccount().setLastLogin();
            UnitOfWork.getCurrent().commit();
        } finally {
            UnitOfWork.setCurrent(null);
        }
       return super.onLoginSuccess(token, subject, request, response);
    }
}
