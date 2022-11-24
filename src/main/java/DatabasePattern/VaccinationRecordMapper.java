package DatabasePattern;

import Backend.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VaccinationRecordMapper implements IDataMapper {

    public static boolean insert(Record record) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        String id = null;
        int recipient_id = record.getVaccineRecipient().getId();
        int hcp_id = record.getHealthCareProvider().getId();
        int vaccine_id = record.getVaccine().getId();
        int ts_id = record.getTimeSlot().getId();
        boolean result = false;
        int result_count = 0;

        String sql = "INSERT INTO vaccination_record(timeslotid, recipientid, vaccineid, providerid) " +
                "VALUES (?,?,?,?) " +
                "ON CONFLICT (timeslotid) " +
                "DO NOTHING; " +
                "UPDATE time_slot as ts " +
                "SET vaccinationrecordid = vr.vaccinationrecordid " +
                "FROM vaccination_record as vr " +
                "WHERE vr.timeslotid = ts.timeslotid ";

        try {
            conn = DBConnection.connection();
            conn.setAutoCommit(false);
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, ts_id);
            preparedSQL.setInt(2, recipient_id);
            preparedSQL.setInt(3, vaccine_id);
            preparedSQL.setInt(4, hcp_id);
//            preparedSQL.execute();
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
        }
        if (result_count > 0){
            result = true;
        }
        else{
            result = false;
        }
        return result;
    }

    public static boolean delete(Record record) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        int record_id = record.getId();

        String sql = "DELETE FROM vaccination_record" +
                "WHERE id = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, record_id);
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

    public static boolean update(Record record) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        String id = null;
        int result_count = 0;
        boolean result = false;
        int recipient_id = record.getVaccineRecipient().getId();
        int hcp_id = record.getHealthCareProvider().getId();
        String vaccine = record.getVaccine().getVaccine();
        int ts_id = record.getTimeSlot().getId();
        int record_id = record.getId();

        String sql = "UPDATE time_slot " +
                "SET timeslotid = ?, " +
                "vaccine = ?, " +
                "providerid = ?, " +
                "recipientid = ? " +
                "WHERE vaccinationrecordid = ?";

        try {
            conn = DBConnection.connection();
            conn.setAutoCommit(false);
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, ts_id);
            preparedSQL.setString(2, vaccine);
            preparedSQL.setInt(3, hcp_id);
            preparedSQL.setInt(4, recipient_id);
            preparedSQL.setInt(5, record_id);
//            preparedSQL.execute();
            result_count = preparedSQL.executeUpdate(); //1 if successful, 0 if
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
            }
            if (result_count > 0){
                result = true;
            }
            else{
                result = false;
            }
            return result;
        }

    public static ArrayList<Record> findVaccinationRecordsByRecipientId(int recipientid) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        ArrayList<Record> result = new ArrayList<>();
        String sql = "SELECT * FROM vaccination_record " +
                "WHERE recipientid = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, recipientid);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                int record_id = rs.getInt("vaccinationrecordid");
                int ts_id = rs.getInt("timeslotid");
                int recipient_id = rs.getInt("recipientid");
                VaccineRecipient recipient = (VaccineRecipient) AccountMapper.findAccountById(recipient_id);
                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                Record record = new Record(recipient, TimeslotMapper.findTimeslotById(ts_id), hcp, vaccine);
                result.add(record);
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

    public static Record findVaccinationRecordById(int recordid) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        Record result = null;
        String sql = "SELECT * FROM vaccination_record " +
                "WHERE recipientid = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, recordid);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                //int record_id = rs.getInt("vaccinationrecordid");
                int ts_id = rs.getInt("timeslotid");
                int recipient_id = rs.getInt("recipientid");
                VaccineRecipient recipient = (VaccineRecipient) AccountMapper.findAccountById(recipient_id);
                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                Record record = new Record(recipient, TimeslotMapper.findTimeslotById(ts_id), hcp, vaccine);
                result = record;
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
        return insert((Record) object);
    }

    @Override
    public boolean delete(Object object) {
        delete((Record) object);
        return true;
    }

    @Override
    public boolean update(Object object) {
        update((Record) object);
        return true;
    }
}
