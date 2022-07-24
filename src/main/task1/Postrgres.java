package main.task1;

import java.sql.*;

public class Postrgres {
    public static void main(String[] args) {
        if (args.length != 0)
            new Postrgres().DataBase(args[0], args[1], args[2]);
    }

    private void DataBase(String login, String password, String url) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection(url, login, password)) {

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM dq_rule");
                rs.next();

                PreparedStatement st = con.prepareStatement("INSERT INTO log (datetime, result) VALUES (?, ?)");
                st.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                st.setInt(2, rs.getInt(1));
                st.executeUpdate();
                st.close();

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

