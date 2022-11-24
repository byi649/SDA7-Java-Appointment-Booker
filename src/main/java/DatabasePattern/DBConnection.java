package DatabasePattern;

import java.sql.*;

public class DBConnection {
    private static final String url = "jdbc:postgresql://ec2-44-193-150-214.compute-1.amazonaws.com:5432/d82056buk2tif9";
    private static final String user = "bicmfyjhlclanj";
    private static final String password = "092c489bb378665abfd97c916ea980f10652a05a0f8f188caa342dcde483ba13";

    public static Connection connection() {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            //do something
        }
        return conn;
    }

}

