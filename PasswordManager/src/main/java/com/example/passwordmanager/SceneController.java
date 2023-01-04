package com.example.passwordmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

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

public class SceneController {

    protected void getLoginScene() throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        LoginController.currentUserID = -1;
        Parent pane = FXMLLoader.load(getClass().getResource("login.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    protected void getPasswordManagerScene() throws IOException, SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        if (LoginController.currentUserID == -1) {
            LoginController.loginController.authorizeUser();
        }
        if (LoginController.currentUserID == -1) {
            return;
        }
        Parent pane = FXMLLoader.load(getClass().getResource("password-manager.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    protected void getMasterPasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("master-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    protected void getNewPasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("new-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    protected void getDeletePasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("delete-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }


    protected void getAboutScene() throws IOException, SQLException {
        Parent pane = FXMLLoader.load(getClass().getResource("about.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }



    protected void getUserInfoScene() throws IOException, SQLException {
        Parent pane = FXMLLoader.load(getClass().getResource("user-info.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
        ScrollPane scrollPane = (ScrollPane) pane.getChildrenUnmodifiable().get(1);
        AnchorPane anchorPane = (AnchorPane) scrollPane.contentProperty().get();

        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=?");
        userQuery.setInt(1, LoginController.currentUserID);
        ResultSet resultSet = userQuery.executeQuery();
        resultSet.next();

        Label currentLabel = (Label) anchorPane.getChildren().get(2);
        currentLabel.setText(resultSet.getString(3));
        currentLabel = (Label) anchorPane.getChildren().get(4);
        currentLabel.setText(resultSet.getString(5));
        currentLabel = (Label) anchorPane.getChildren().get(6);
        currentLabel.setText(resultSet.getString(4));

        userQuery = connection.prepareStatement("SELECT COUNT(id) FROM passwords WHERE pm_user_id=?");
        userQuery.setInt(1, LoginController.currentUserID);
        resultSet = userQuery.executeQuery();
        resultSet.next();
        currentLabel = (Label) anchorPane.getChildren().get(8);
        currentLabel.setText(Integer.toString(resultSet.getInt(1)));

        connection.close();
    }
}
