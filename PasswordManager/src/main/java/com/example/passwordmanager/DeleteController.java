package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteController {
    @FXML
    TextField confirmPasswordName;

    @FXML
    TextField confirmPassword;

    @FXML
    protected  void deletePassword() throws SQLException, IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = LoginController.getConnection();
        String masterPass = ManagerSecurity.getMasterPass();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=? AND password_name =? AND password=?");
        userQuery.setInt(1, LoginController.currentUserID);
        userQuery.setString(2, confirmPasswordName.getText());
        userQuery.setString(3, ManagerSecurity.encrypt(masterPass, confirmPassword.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            userQuery = connection.prepareStatement("DELETE FROM passwords WHERE pm_user_id=? AND password_name =? AND password=?");
            userQuery.setInt(1, LoginController.currentUserID);
            userQuery.setString(2, confirmPasswordName.getText());
            userQuery.setString(3, ManagerSecurity.encrypt(masterPass, confirmPassword.getText()));
            userQuery.execute();
            getPasswordManagerScene();
        }
    }

    @FXML
    protected void getPasswordManagerScene() throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SceneController sceneController = new SceneController();
        sceneController.getPasswordManagerScene();
    }

    @FXML
    protected void getUserInfoScene() throws SQLException, IOException {
        SceneController sceneController = new SceneController();
        sceneController.getUserInfoScene();
    }

    @FXML
    protected void getMasterPasswordScene() throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.getMasterPasswordScene();
    }

    @FXML
    protected void getAboutScene() throws SQLException, IOException {
        SceneController sceneController = new SceneController();
        sceneController.getAboutScene();
    }

    @FXML
    protected void getLoginScene() throws IllegalBlockSizeException, NoSuchPaddingException, IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SceneController sceneController = new SceneController();
        sceneController.getLoginScene();
    }




}
