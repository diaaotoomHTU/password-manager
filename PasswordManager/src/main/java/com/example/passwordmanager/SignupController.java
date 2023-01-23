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
    protected void addNewUserFXML() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        boolean result = addNewUser(newUsername.getText(), newPassword.getText(), newPasswordConfirmation.getText(), name.getText(), email.getText(), role.getText());
        if (result) {
            getLoginScene();
        }
    }

    public boolean validateUsername(String username) throws SQLException {
        if (username == null) {
            return false;
        }
        int length = username.length();
        return !usernameTaken(username) && length < 31 && length > 2;
    }

    public boolean usernameTaken(String username) throws SQLException {
        Connection connection = LoginController.getConnection();
        PreparedStatement usernameQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE username = ?");
        usernameQuery.setString(1, username);
        ResultSet resultSet = usernameQuery.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
    }

    public boolean validatePassword(String password) {
        if (password == null) {
            return false;
        }
        int length = password.length();
        return length < 128 && length > 7;
    }

    public boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        int length = email.length();
        return length > 3 && email.contains("@");
    }

    public boolean addNewUser(String username, String password, String confirmPassword, String name, String email, String role) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SQLException {
        boolean valid = validateUsername(username) && validatePassword(password) && validateEmail(email) && password.equals(confirmPassword);
        if (!valid) {
            return false;
        }
        Connection connection = LoginController.getConnection();
        String masterPass = ManagerSecurity.encrypt("masterpassword12", password);
        PreparedStatement insertUser = connection.prepareStatement("INSERT INTO pm_users (USERNAME, NAME, EMAIL, ROLE, MASTER_PASSWORD) VALUES (?, ?, ?, ?, ?)");
        insertUser.setString(1, username);
        insertUser.setString(2, name);
        insertUser.setString(3, email);
        insertUser.setString(4, role);
        insertUser.setString(5, masterPass);
        insertUser.execute();
        return true;
    }

    @FXML
    protected void getLoginScene() throws IllegalBlockSizeException, NoSuchPaddingException, IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SceneController sceneController = new SceneController();
        sceneController.getLoginScene();
    }

}