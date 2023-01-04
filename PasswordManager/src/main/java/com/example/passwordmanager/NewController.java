package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewController {

    @FXML
    TextField newPasswordName;

    @FXML
    TextField newPassword;

    @FXML
    TextField newPasswordImage;



    @FXML
    protected  void getImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        String selectedFile = fileChooser.showOpenDialog(Application.loadedStage).toString();
        newPasswordImage.insertText(0, selectedFile);
    }

    @FXML
    protected void addNewPassword() throws IOException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = LoginController.getConnection();
        String masterPass = ManagerSecurity.getMasterPass();
        PreparedStatement insertPassword = connection.prepareStatement("INSERT INTO passwords (pm_user_id, password_name, password, image) VALUES (?, ?, ?, ?)");
        insertPassword.setInt(1, LoginController.currentUserID);
        insertPassword.setString(2, newPasswordName.getText());
        insertPassword.setString(3, ManagerSecurity.encrypt(masterPass, newPassword.getText()));
        insertPassword.setString(4, newPasswordImage.getText());
        insertPassword.execute();
        getPasswordManagerScene();
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
