package spring.mysql;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;

/**
 * Created by Djowood on 16/10/2016.
 */
public class Connexion {
    private static Connexion instance;

    private final String DBurl = "jdbc:spring.mysql://localhost/geodraw";
    private final String login = "root";
    private final String password = "";

    private final DriverManagerDataSource springDataSource;

    private static Connection con;

    private Connexion() {
        try {
            Class.forName("com.spring.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(DBurl, login, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.springDataSource = new DriverManagerDataSource();
        this.springDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        this.springDataSource.setUrl(DBurl);
        this.springDataSource.setUsername(login);
        this.springDataSource.setPassword(password);
    }

    private void testConnexion() throws SQLException {
        Statement dbStatement = con.createStatement();

        ResultSet rs = dbStatement.executeQuery("SELECT * FROM game");
        if (rs != null) {
            while (rs.next()) {
                System.out.println("Valeur: " + rs.getString(1));
            }
        }
    }

    public static Connexion getInstance() {
        if (instance == null) {
            instance = new Connexion();
        }

        return instance;
    }

    public DriverManagerDataSource getSpringDataSource() {
        return springDataSource;
    }
}
