package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

public class MasterController {
    @FXML
    TextField oldMasterPassword;

    @FXML
    TextField newMasterPassword;

    @FXML
    TextField confirmDeleteAll;



    @FXML
    protected void changeMasterPassword() throws SQLException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=? AND master_password=?");
        userQuery.setInt(1, LoginController.currentUserID);
        userQuery.setString(2, ManagerSecurity.encrypt("masterpassword12", oldMasterPassword.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            String oldPassword = ManagerSecurity.getMasterPass();
            userQuery = connection.prepareStatement("UPDATE pm_users SET master_password=? WHERE id=?");
            String newPass = ManagerSecurity.encrypt("masterpassword12", newMasterPassword.getText());
            userQuery.setString(1, newPass);
            userQuery.setInt(2, LoginController.currentUserID);
            userQuery.execute();
            userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=?");
            userQuery.setInt(1, LoginController.currentUserID);
            resultSet = userQuery.executeQuery();
            while (resultSet.next()) {

                PreparedStatement updatePassQuery = connection.prepareStatement("UPDATE passwords SET password=? WHERE pm_user_id=? AND password=?");
                updatePassQuery.setString(1, ManagerSecurity.encrypt(newMasterPassword.getText(), ManagerSecurity.decrypt(oldPassword, resultSet.getString(4))));
                updatePassQuery.setInt(2, LoginController.currentUserID);
                updatePassQuery.setString(3, resultSet.getString(4));
                updatePassQuery.execute();
            }
            getPasswordManagerScene();
        }
    }

    @FXML
    protected void deleteAllPasswords() throws SQLException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=? AND master_password=?");
        userQuery.setInt(1, LoginController.currentUserID);
        userQuery.setString(2, ManagerSecurity.encrypt("masterpassword12", confirmDeleteAll.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            userQuery = connection.prepareStatement("DELETE FROM passwords WHERE pm_user_id=?");
            userQuery.setInt(1, LoginController.currentUserID);
            userQuery.execute();
            getPasswordManagerScene();
        }
    }

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
