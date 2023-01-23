package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

public class SignupController {


    @FXML
    TextField newUsername;

    @FXML
    PasswordField newPassword;

    @FXML
    PasswordField newPasswordConfirmation;

    @FXML
    TextField name;

    @FXML
    TextField email;

    @FXML
    TextField role;


    @FXML
    protected void addNewUser() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        if (!newPassword.getText().equals(newPasswordConfirmation.getText())) {
            return;
        }
        Connection connection = LoginController.getConnection();
        String masterPass = ManagerSecurity.encrypt("masterpassword12", newPassword.getText());
        PreparedStatement insertUser = connection.prepareStatement("INSERT INTO pm_users (USERNAME, NAME, EMAIL, ROLE, MASTER_PASSWORD) VALUES (?, ?, ?, ?, ?)");
        insertUser.setString(1, newUsername.getText());
        insertUser.setString(2, name.getText());
        insertUser.setString(3, email.getText());
        insertUser.setString(4, role.getText());
        insertUser.setString(5, masterPass);
        insertUser.execute();
        getLoginScene();
    }


    @FXML
    protected void getLoginScene() throws IllegalBlockSizeException, NoSuchPaddingException, IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SceneController sceneController = new SceneController();
        sceneController.getLoginScene();
    }

}