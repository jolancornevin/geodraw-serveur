package fr.insa.ot3.mysql;

import java.sql.*;

/**
 * Created by Djowood on 16/10/2016.
 */
public class Connexion {
    private static Connexion instance;

    private final String DBurl = "jdbc:mysql://localhost/geodraw";
    private final String login = "root";
    private final String password = "";

    private static Connection con;

    private Connexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(DBurl, login, password);
            Statement dbStatement = con.createStatement();
            ResultSet rs = dbStatement.executeQuery("SELECT * FROM game");
            if (rs != null) {
                while (rs.next()) {
                    System.out.println("Valeur: " + rs.getString(1));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connexion getInstance() {
        if (instance == null) {
            instance = new Connexion();
        }

        return instance;
    }
}
