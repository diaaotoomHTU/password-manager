package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewController {

    @FXML
    TextField newPasswordName;

    @FXML
    TextField newPassword;

    @FXML
    TextField newPasswordImage;



    @FXML
    protected void getImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(Application.loadedStage);
        if (file != null) {
            newPasswordImage.insertText(0, file.toString());
        }
    }

    @FXML
    protected void addNewPasswordFXML() throws IOException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        boolean result = addNewPassword(LoginController.currentUserID, newPasswordName.getText(), newPassword.getText(), newPasswordImage.getText());
        if (result) {
            getPasswordManagerScene();
        }
    }

    public boolean addNewPassword(int userID, String newPasswordName, String newPassword, String newPasswordImage) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        if (!validatePassword(newPassword) || !userExists(userID)) {
            return false;
        }
        Connection connection = LoginController.getConnection();
        String masterPass = ManagerSecurity.getMasterPass(userID);
        PreparedStatement insertPassword = connection.prepareStatement("INSERT INTO passwords (pm_user_id, password_name, password, image) VALUES (?, ?, ?, ?)");
        insertPassword.setInt(1, userID);
        insertPassword.setString(2, newPasswordName);
        insertPassword.setString(3, ManagerSecurity.encrypt(masterPass, newPassword));
        insertPassword.setString(4, newPasswordImage);
        insertPassword.execute();
        return true;
    }

    public boolean userExists(int userID) throws SQLException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id = ?");
        userQuery.setInt(1, userID);
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
    }
    public boolean validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 127) {
            return false;
        }
        return true;
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
