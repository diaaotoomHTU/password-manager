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
import java.util.ArrayList;
import java.util.List;

public class MasterController {
    @FXML
    TextField oldMasterPassword;

    @FXML
    TextField newMasterPassword;

    @FXML
    TextField confirmDeleteAll;



    @FXML
    protected void changeMasterPasswordFXML() throws SQLException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        boolean result = changeMasterPassword(LoginController.currentUserID, oldMasterPassword.getText(), newMasterPassword.getText());
        if (result) {
            getPasswordManagerScene();
        }
    }

    private boolean changeMasterPassword(int userID, String inputOldMasterPassword, String newMasterPassword) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String oldMasterPassword = ManagerSecurity.getMasterPass(userID);
        boolean exists = masterPasswordExists(userID, oldMasterPassword);
        if (!exists || !oldMasterPassword.equals(inputOldMasterPassword) || newMasterPassword.length() < 8) {
            return false;
        }
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("UPDATE pm_users SET master_password=? WHERE id=?");
        String newPass = ManagerSecurity.encrypt("masterpassword12", newMasterPassword);
        userQuery.setString(1, newPass);
        userQuery.setInt(2, LoginController.currentUserID);
        userQuery.execute();
        updateAllPasswords(userID, oldMasterPassword, newMasterPassword);
        return true;
    }

    private boolean updateAllPasswords(int userID, String oldMasterPassword, String newMasterPassword) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=?");
        userQuery.setInt(1, userID);
        ResultSet resultSet = userQuery.executeQuery();
        List<String> passwords = new ArrayList<String>();
        while (resultSet.next()) {
            passwords.add(resultSet.getString(4));
        }
        if (passwords.size() == 0) {
            return false;
        }
        for (String password: passwords) {
            PreparedStatement updatePassQuery = connection.prepareStatement("UPDATE passwords SET password=? WHERE pm_user_id=? AND password=?");
            updatePassQuery.setString(1, ManagerSecurity.encrypt(newMasterPassword, ManagerSecurity.decrypt(oldMasterPassword, password)));
            updatePassQuery.setInt(2, userID);
            updatePassQuery.setString(3, resultSet.getString(4));
            updatePassQuery.execute();
        }
        return true;
    }


    private boolean masterPasswordExists(int userID, String oldMasterPassword) throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=? AND master_password=?");
        userQuery.setInt(1, userID);
        userQuery.setString(2, ManagerSecurity.encrypt("masterpassword12", oldMasterPassword));
        ResultSet resultSet = userQuery.executeQuery();
        return  resultSet.next();
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
