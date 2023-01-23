package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

import java.io.IOException;

public class Application extends javafx.application.Application {

    static Stage loadedStage;

    @Override
    public void start(Stage stage) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        String jdbcURL = "jdbc:h2:./h2db";
        String username = "sa";
        String password = "sa";
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("connected");
            boolean test = Application.tableExists(connection);
            System.out.println(test);
            if (!Application.tableExists(connection)) {
                Application.createTables(connection);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        System.out.println(loader.getController().toString());
        scene.getStylesheets().add(Application.class.getResource("styles/nord-light.css").toString());
        loadedStage = stage;
        stage.setTitle("Password Manager");
        stage.getIcons().add(new Image(Application.class
                .getResource("images/lock-icon.png").toString()));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    static boolean tableExists(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) FROM information_schema.tables WHERE table_name = 'pm_users' LIMIT 1");
        ResultSet tables = connection.getMetaData().getTables(null, null, "PM_USERS", null);
        return tables.next();
    }


    // 171 char limit for 127 char password limit
    static void createTables(Connection  connection) throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        PreparedStatement pm_usersQuery = connection.prepareStatement("CREATE TABLE pm_users(id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(30), name VARCHAR(30), email VARCHAR(40), role VARCHAR(30), master_password VARCHAR(171));");
        PreparedStatement passwordsQuery = connection.prepareStatement("CREATE TABLE passwords (id INT PRIMARY KEY AUTO_INCREMENT, pm_user_id INT, password_name VARCHAR(30), password VARCHAR(171), image varchar(200), FOREIGN KEY (pm_user_id) REFERENCES pm_users(id));");
        pm_usersQuery.execute();
        passwordsQuery.execute();
        PreparedStatement insertBob = connection.prepareStatement("INSERT INTO pm_users (username, name, email, role, master_password) " +
                "VALUES ('bob', 'Bob', 'bob@email.com', 'Employee', ?);");
        insertBob.setString(1, ManagerSecurity.encrypt("masterpassword12", "12345678"));
        insertBob.execute();
    }

    // local jdbc dir
    // jdbc:h2:C:/Users/diaao/Desktop/Software Development Lifecycles/Assignment/Development/Code/password-manager/PasswordManager/h2db

    public static void main(String[] args) {
        launch();
    }
}