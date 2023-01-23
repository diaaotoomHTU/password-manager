package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

public class LoginController {

    public static int currentUserID = -1;

    static LoginController loginController;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    public void initialize() {
        loginController = this;
    }

    public static Connection getConnection() {
        String jdbcURL = "jdbc:h2:./h2db";
        String username = "sa";
        String password = "sa";
        Connection connection;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
        return connection;
    }

    protected void authorizeUser() throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE username=? AND master_password=?");
        userQuery.setString(1, username.getText());
        userQuery.setString(2, ManagerSecurity.encrypt("masterpassword12", password.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            currentUserID = resultSet.getInt(1);
        }
        connection.close();
    }



    @FXML
    protected  void getPasswordManagerScene() throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SceneController sceneController = new SceneController();
        sceneController.getPasswordManagerScene();
    }

    @FXML
    protected  void getSignupScene() throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.getSignupScene();
    }


}