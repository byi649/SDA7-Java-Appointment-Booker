package DatabasePattern;
import Backend.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.shiro.authc.credential.DefaultPasswordService;
public class VaccinationMapper implements IDataMapper {

    public static void insert(Vaccine vaccine) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        int id;
        ResultSet rs = null;
        String name = vaccine.getVaccine();

        String sql = "INSERT INTO vaccine(vaccine)" +
                "VALUES (?)";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, name);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
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
    }

    public static Vaccine findVaccineByName(String name) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        Vaccine result = null;
        String sql = "SELECT vaccine, vaccineid FROM vaccine " +
                "WHERE vaccine = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, name);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String vaccine_name = rs.getString("vaccine");
                int vaccine_id = rs.getInt("vaccineid");
                Vaccine vaccine = new Vaccine(vaccine_name, vaccine_id);
                result = vaccine;
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
        return result;
    }

    public static Vaccine findVaccineById(int vaccination_id) throws SQLException {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        Vaccine result = null;
        String sql = "SELECT vaccine, vaccineid FROM vaccine " +
                "WHERE vaccineid = ?"; // TODO: actual SQL statement

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, vaccination_id);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String name = rs.getString("vaccine");
                int vaccine_id = rs.getInt("vaccineid");
                result = new Vaccine(name, vaccine_id);
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
        return result;
    }

    @Override
    public boolean insert(Object object) {
        insert((Vaccine) object);
        return true;
    }

    @Override
    public boolean delete(Object object) {

        return true;
    }

    @Override
    public boolean update(Object object) {

        return true;
    }
}
