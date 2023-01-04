package com.example.passwordmanager;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

public class LoginController {

    public static int currentUserID = -1;

    @FXML
    TextField username;

    @FXML
    TextField password;

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
        ManagerController managerController = new ManagerController();
        managerController.getPasswordManagerScene();
    }



}