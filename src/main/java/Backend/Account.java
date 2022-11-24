package Backend;

import DatabasePattern.AccountMapper;
import DatabasePattern.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;

import java.util.Date;

import java.time.LocalDate;
import java.util.Date;

public abstract class Account {

    private int id;
    private String name;
    private String password;
    private Date lastLogin = null;
    private static Account currentAccount = null;
    /**
     *
     * @return name of accounts, including administrator, Health care provider and vaccine recipient
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return password of accounts, including administrator, Health care provider and vaccine recipient
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return database ID of account
     */
    public int getId(){ return id; }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name set name for accounts
     */
    public void setName(String name) {
        this.name = name;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * @param password set password for accounts
     */
    public void setPassword(String password) {
        this.password = password;
//        UnitOfWork.getCurrent().registerDirty(this);
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin() {
        Subject user = SecurityUtils.getSubject();
        Session currentSession = user.getSession();
        this.lastLogin = currentSession.getStartTimestamp();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setLastLoginGhost() {
        Subject user = SecurityUtils.getSubject();
        Session currentSession = user.getSession();
        this.lastLogin = currentSession.getStartTimestamp();
    }

    public void setLastLogin(Date lastLogin){
        this.lastLogin = lastLogin;
    }

    public Account(String name) {
        this.name = name;
    }

    public static Account getCurrentAccount() {
        Subject currentUser = SecurityUtils.getSubject();
        currentAccount = AccountMapper.findAccountByName((String) currentUser.getPrincipal());
        return currentAccount;
    }

    // Based off https://stackoverflow.com/a/21133092
    public static boolean hasLoggedInSince() {
        Subject user = SecurityUtils.getSubject();
        Session currentSession = user.getSession();
        Date loginDate = currentSession.getStartTimestamp();

        Account account = getCurrentAccount();

        System.out.println("This session login time: " + loginDate);
        System.out.println("Database login time: " + account.getLastLogin());
        return loginDate.before(account.getLastLogin());
    }

}
