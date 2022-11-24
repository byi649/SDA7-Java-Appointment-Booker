package DatabasePattern;

import Backend.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.util.Date;

public class AccountMapper implements IDataMapper{

    public static boolean addToDatabase(Account account) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        int id;
        String name = account.getName();
        String password = account.getPassword();
        DefaultPasswordService security = new DefaultPasswordService();
        String hashed = security.encryptPassword(password);
        int result_count = 0;
        boolean result = false;

        String sql = "";

        try {

            if (account instanceof VaccineRecipient) {
                LocalDate date = ((VaccineRecipient) account).getBirthDate();
                sql = "INSERT INTO account(accounttypeid, name, password, dob, lastlogin) " +
                        "VALUES (1,?,?,?,?) " +
                        "ON CONFLICT (name) " +
                        "DO NOTHING";
                conn = DBConnection.connection();
                conn.setAutoCommit(false);
                preparedSQL = conn.prepareStatement(sql);
                preparedSQL.setDate(3, Date.valueOf(date));
                preparedSQL.setTimestamp(4, new Timestamp(account.getLastLogin().getTime()));
            }
            else if (account instanceof Administrator){
                sql = "INSERT INTO account(accounttypeid, name, password, lastlogin) " +
                        "VALUES (2,?,?,?) " +
                        "ON CONFLICT (name) " +
                        "DO NOTHING";
                conn = DBConnection.connection();
                conn.setAutoCommit(false);
                preparedSQL = conn.prepareStatement(sql);
                preparedSQL.setTimestamp(3, new Timestamp(account.getLastLogin().getTime()));
            }
            else if (account instanceof HealthCareProvider){
                String area_code = ((HealthCareProvider) account).getPostcode();
                sql = "INSERT INTO account(accounttypeid, name, password, area_code, lastlogin) " +
                        "VALUES (3,?,?,?,?) " +
                        "ON CONFLICT (name) " +
                        "DO NOTHING";
                conn = DBConnection.connection();
                conn.setAutoCommit(false);
                preparedSQL = conn.prepareStatement(sql);
                preparedSQL.setString(3, area_code);
                preparedSQL.setTimestamp(4, new Timestamp(account.getLastLogin().getTime()));
            }

            preparedSQL.setString(1, name);
            preparedSQL.setString(2, hashed);
            result_count = preparedSQL.executeUpdate(); //1 if successful, 0 if not
            conn.commit();
        } catch (SQLException e) {
            try {System.out.println("Rolling back transaction...");
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO: throw exception
            }
        }
        if (result_count > 0){
            result = true;
        }
        else{
            result = false;
        }
        return result;
    }

    public static boolean delete(Account account) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        int account_id = account.getId();

        String sql = "DELETE FROM account " +
                "WHERE accountid = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, account_id);
            preparedSQL.execute();
        } catch (SQLException e) {
            // TODO: throw exception
        } finally {
            try {
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO: throw exception
            }
        }
        return true;
    }

    public static boolean update(Account account) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        boolean result = false;
        ResultSet rs = null;
        int account_id = account.getId();
        String name = account.getName();
        String password = account.getPassword();
        int result_count = 0;

        String sql = "UPDATE account " +
                "SET name = ?, " +
                "password = ?, " +
                "lastlogin = ? " +
                "WHERE accountid = ?";

        try {
            conn = DBConnection.connection();
            conn.setAutoCommit(false);
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, name);
            preparedSQL.setString(2, password);
            preparedSQL.setTimestamp(3, new Timestamp(account.getLastLogin().getTime()));
            preparedSQL.setInt(4, account_id);
//            preparedSQL.execute();
            System.out.println(preparedSQL);
            result_count = preparedSQL.executeUpdate(); //1 if successful, 0 if not
            System.out.println(result_count);
            conn.commit();
            rs = preparedSQL.getResultSet();
        } catch (SQLException e) {
            try {System.out.println("Rolling back transaction...");
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        if (result_count > 0){
            result = true;
        }
        else{
            result = false;
        }
        return result;
    }

    public static Account findAccountById(int account_id) throws SQLException {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        Account result = null;
        String sql = "SELECT * FROM account " +
                "WHERE accountid = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, account_id);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                int account_type = rs.getInt("accounttypeid");

                Account account = null;
                if (account_type == 1){
                    LocalDate birthDate = rs.getDate("dob").toLocalDate();
                    account = new VaccineRecipient(name, password, birthDate);
                    account.setId(account_id);
                }
                else if (account_type == 2){
                    account = new Administrator(name, password);
                    account.setId(account_id);
                }
                else if (account_type == 3){
                    String postcode = rs.getString("area_code");
                    account = new HealthCareProvider(name, password, postcode);
                    account.setId(account_id);
                }
                result = account;
            }
        } catch (SQLException e) {
            // TODO: throw exception
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO: throw exception
            }
        }
        return result;
    }

    public static Account findAccountByName(String name) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        Account result = null;
        String sql = "SELECT * FROM account " +
                "WHERE name = ? "; // TODO: actual SQL statement

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, name);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String password = rs.getString("password");
                int account_type = rs.getInt("accounttypeid");
                int account_id = rs.getInt("accountid");
                Account account = null;

                Timestamp timestamp = rs.getTimestamp("lastlogin");
                java.util.Date lastLogin = new java.util.Date(timestamp.getTime());

                if (account_type == 1){
                    LocalDate birthDate = rs.getDate("dob").toLocalDate();
                    account = new VaccineRecipient(name, password, birthDate);
                    account.setId(account_id);
                    account.setLastLogin(lastLogin);
                }
                else if (account_type == 2){
                    account = new Administrator(name, password);
                    account.setLastLogin(lastLogin);
                    account.setId(account_id);
                }
                else if (account_type == 3){
                    String postcode = rs.getString("area_code");
                    account = new HealthCareProvider(name, password, postcode);
                    account.setLastLogin(lastLogin);
                    account.setId(account_id);
                }
                result = account;
            }
        } catch (SQLException e) {
            // TODO: throw exception
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO: throw exception
            }
        }
        return result;
    }

    public static ArrayList<Account> findAllUsers() throws SQLException {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        ArrayList<Account> resultList = new ArrayList<Account>();
        String sql = "SELECT account.name as name, account_type.accounttype as type FROM account " +
                "INNER JOIN account_type on account.accounttypeid = account_type.accounttypeid";
        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                //TODO: do we need other parameters?
                Account account = null;
                if (type.equals("recipient")){
                    account = new VaccineRecipient(name);
                    System.out.println(account.getName());
                }
                else if (type.equals("administrator")){
                    account = new Administrator(name);
                    System.out.println(account.getName());
                }
                else if (type.equals("hcp")){
                    account = new HealthCareProvider(name);
                    System.out.println(account.getName());
                }
                resultList.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO: throw exception
            }
        }
        return resultList;
    }

    public static ArrayList<VaccineRecipient> findAllVaccineRecipients() throws SQLException {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        ArrayList<VaccineRecipient> resultList = new ArrayList<VaccineRecipient>();
        String sql = "SELECT account.name as name, account.accountid as accountid FROM account " +
                "INNER JOIN account_type on account.accounttypeid = account_type.accounttypeid " +
                "WHERE account_type.accounttype = 'recipient'";
        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("accountid");
                VaccineRecipient recipient = new VaccineRecipient(name);
                recipient.setId(id);
                resultList.add(recipient);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedSQL != null) {
                    preparedSQL.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO: throw exception
            }
        }
        return resultList;
    }

    // TODO: polymorphism instead
    @Override
    public boolean insert(Object object) {
        boolean result = true;
        if (object instanceof Administrator) {
            if (!addToDatabase((Administrator) object)) {
                result = false;
            }
        } else if (object instanceof VaccineRecipient) {
            if (!addToDatabase((VaccineRecipient) object)) {
                result = false;
            }
        } else if (object instanceof HealthCareProvider) {
            if (!addToDatabase((HealthCareProvider) object)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean delete(Object object) {
        if (object instanceof Administrator) {
            delete((Administrator) object);
        } else if (object instanceof VaccineRecipient) {
            delete((VaccineRecipient) object);
        } else if (object instanceof HealthCareProvider) {
            delete((HealthCareProvider) object);
        }
        return true;
    }

    @Override
    public boolean update(Object object) {
        boolean result = true;
        if (object instanceof Administrator) {
            if (!update((Administrator) object)) {
                result = false;
            }
        } else if (object instanceof VaccineRecipient) {
            if (!update((VaccineRecipient) object)) {
                result = false;
            }
        } else if (object instanceof HealthCareProvider) {
            if (!update((HealthCareProvider) object)) {
                result = false;
            }
        }
        return result;
    }
}
