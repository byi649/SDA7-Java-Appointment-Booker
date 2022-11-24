package DatabasePattern;

import Backend.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.*;

public class TimeslotMapper implements IDataMapper {

    public static ArrayList<Timeslot> findAllTimeSlots() {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        ArrayList<Timeslot> resultList = new ArrayList<Timeslot>();
        String sql = "SELECT time_slot.starttime, time_slot.endtime, a1.name as hcp, vaccineid FROM time_slot " +
                "INNER JOIN account a1 on time_slot.providerid = a1.AccountID ";
        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                String hcp_name = rs.getString("hcp");
                HealthCareProvider hcp = new HealthCareProvider(hcp_name);
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                Timeslot ts = new Timeslot(rs.getTimestamp("starttime").toLocalDateTime(),
                        rs.getTimestamp("endtime").toLocalDateTime(),
                        hcp, vaccine);
                resultList.add(ts);
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

    public static ArrayList<Timeslot> findAvailableTimeSlots() {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        ArrayList<Timeslot> resultList = new ArrayList<Timeslot>();
        String sql = "SELECT * FROM time_slot " +
                "WHERE recipientid IS Null"; // TODO: actual SQL statement

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {

                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                VaccineRecipient recipient = (VaccineRecipient) AccountMapper.findAccountById(rs.getInt("recipientid"));
                // Vaccine vaccine = rs.getString("recipientid"));

                Timeslot ts = new Timeslot(rs.getTimestamp("starttime").toLocalDateTime(),
                        rs.getTimestamp("endtime").toLocalDateTime(),
                        hcp,
                        new Vaccine("Pfizer")
                );
                resultList.add(ts);
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

    public static boolean insert(Timeslot ts) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        String id = null;
        ResultSet rs = null;
        Timestamp start_time = Timestamp.valueOf(ts.getStartTime());
        Timestamp end_time = Timestamp.valueOf(ts.getEndTime());
        int hcp_id = ts.getHealthCareProvider().getId();
        int vaccine_id = ts.getVaccine().getId();


        String sql = "INSERT INTO time_slot(starttime, endtime, providerid, vaccineid)" +
                "VALUES (?,?,?,?)";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setTimestamp(1, start_time);
            preparedSQL.setTimestamp(2, end_time);
            preparedSQL.setInt(3, hcp_id);
            preparedSQL.setInt(4, vaccine_id);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
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
        return true;
    }

    public static Timeslot findTimeslotById(int timeslot_id) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        Timeslot result = null;
        String sql = "SELECT * FROM time_slot " +
                "WHERE timeslotid = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, timeslot_id);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                LocalDateTime starttime = rs.getTimestamp("starttime").toLocalDateTime();
                LocalDateTime endtime = rs.getTimestamp("endtime").toLocalDateTime();
                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                int recipientID = rs.getInt("recipientid");
                Timeslot ts = null;
                if (recipientID == 0) {
                    ts = new Timeslot(starttime, endtime, hcp, vaccine);
                } else {
                    ts = new Timeslot(starttime, endtime, hcp, (VaccineRecipient) AccountMapper.findAccountById(recipientID), vaccine);
                }
                ts.setId(timeslot_id);
                result = ts;
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

    public static boolean delete(Timeslot ts) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        int ts_id = ts.getId();

        String sql = "DELETE FROM time_slot" +
                "WHERE id = ?";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, ts_id);
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

    public static boolean update(Timeslot ts) {
        Connection conn = null;
        PreparedStatement preparedSQL = null;
        String id = null;
        ResultSet rs = null;
        int recipient_id = ts.getRecipient().getId();
        int timeslot_id = ts.getId();
        boolean result = false;
        int result_count = 0;
        String sql = "UPDATE time_slot " +
                "SET recipientid = ? " +
                "WHERE timeslotid = ? " +
                "AND recipientid IS Null;";

        try {
            conn = DBConnection.connection();
            conn.setAutoCommit(false);
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setInt(1, recipient_id);
            preparedSQL.setInt(2, timeslot_id);
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

    public static ArrayList<Timeslot> findAvailableTimeSlotsByArea(String area_code) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        ArrayList<Timeslot> resultList = new ArrayList<Timeslot>();
        String sql = "SELECT time_slot.starttime, time_slot.endtime, providerid, timeslotid, recipientid, a1.name as hcp, vaccineid FROM time_slot " +
                "INNER JOIN account a1 on time_slot.providerid = a1.AccountID " +
                "WHERE a1.area_code = ? AND recipientid IS NULL";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, area_code);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                int timeslotID = rs.getInt("timeslotid");
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                Timeslot ts = new Timeslot(rs.getTimestamp("starttime").toLocalDateTime(),
                        rs.getTimestamp("endtime").toLocalDateTime(),
                        hcp,
                        vaccine
                );
                ts.setId(timeslotID);
                resultList.add(ts);
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

    public static ArrayList<Timeslot> findAvailableTimeSlotsByHCP(String hcp_name) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        ArrayList<Timeslot> resultList = new ArrayList<Timeslot>();
        String sql = "SELECT time_slot.starttime, time_slot.endtime, providerid, timeslotid, vaccineid, recipientid, a1.name as hcp, vaccineid FROM time_slot " +
                "INNER JOIN account a1 on time_slot.providerid = a1.AccountID " +
                "WHERE a1.name = ? AND recipientid IS NULL";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, hcp_name);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                int timeslotID = rs.getInt("timeslotid");
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                Timeslot ts = new Timeslot(rs.getTimestamp("starttime").toLocalDateTime(),
                        rs.getTimestamp("endtime").toLocalDateTime(),
                        hcp,
                        vaccine
                );
                ts.setId(timeslotID);
                resultList.add(ts);
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

    public static ArrayList<Timeslot> findBookedSlotsByHCP(String hcp_name) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement preparedSQL = null;

        ArrayList<Timeslot> resultList = new ArrayList<Timeslot>();
        String sql = "SELECT time_slot.starttime, time_slot.endtime, providerid, recipientid, a1.name as hcp, vaccineid, timeslotid FROM time_slot " +
                "INNER JOIN account a1 on time_slot.providerid = a1.AccountID " +
                "WHERE a1.name = ? AND vaccinationrecordid IS NULL AND recipientid IS NOT NULL";

        try {
            conn = DBConnection.connection();
            preparedSQL = conn.prepareStatement(sql);
            preparedSQL.setString(1, hcp_name);
            preparedSQL.execute();
            rs = preparedSQL.getResultSet();
            while (rs.next()) {
                HealthCareProvider hcp = (HealthCareProvider) AccountMapper.findAccountById(rs.getInt("providerid"));
                Vaccine vaccine = VaccinationMapper.findVaccineById(rs.getInt("vaccineid"));
                VaccineRecipient recipient = (VaccineRecipient) AccountMapper.findAccountById(rs.getInt("recipientid"));
                int id = rs.getInt("timeslotid");
                Timeslot ts = new Timeslot(rs.getTimestamp("starttime").toLocalDateTime(),
                        rs.getTimestamp("endtime").toLocalDateTime(),
                        hcp,
                        vaccine
                );
                ts.setVaccineRecipientGhost(recipient);
                ts.setId(id);
                resultList.add(ts);
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

    @Override
    public boolean insert(Object object) {
        return insert((Timeslot) object);
    }

    @Override
    public boolean delete(Object object) {
        delete((Timeslot) object);
        return true;
    }

    @Override
    public boolean update(Object object) {
        return update((Timeslot) object);
    }
}
