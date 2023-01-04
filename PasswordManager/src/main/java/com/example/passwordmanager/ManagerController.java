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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

public class ManagerController {

    @FXML
    VBox passwordBox;

    void initialize() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        displayAllPasswords();
    }
    protected void displayAllPasswords() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=?");
        userQuery.setInt(1, LoginController.currentUserID);
        ResultSet resultSet = userQuery.executeQuery();
        String masterPass = ManagerSecurity.getMasterPass();
        while (resultSet.next()) {
            // Formatting password entries
            AnchorPane newPasswordEntry = new AnchorPane();
            ObservableList<Node> newPasswordEntryChildren = newPasswordEntry.getChildren();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            newPasswordEntryChildren.add(imageView);
            AnchorPane.setLeftAnchor(imageView, 14.0);
            AnchorPane.setTopAnchor(imageView, 25.0);
            Label websiteLabel = new Label(resultSet.getString(3));
            AnchorPane.setTopAnchor(websiteLabel, 36.0);
            AnchorPane.setLeftAnchor(websiteLabel, 75.0);
            newPasswordEntryChildren.add(websiteLabel);
            Label passwordLabel = new Label(ManagerSecurity.decrypt(masterPass, resultSet.getString(4)));
            AnchorPane.setTopAnchor(passwordLabel, 36.0);
            AnchorPane.setLeftAnchor(passwordLabel, 200.0);
            passwordLabel.setVisible(false);
            newPasswordEntryChildren.add(passwordLabel);
            Button revealButton = new Button("Reveal");
            revealButton.setPrefWidth(70);
            revealButton.setPrefHeight(20);
            revealButton.setOnAction(event -> {
                revealPassword(event);
            });
            AnchorPane.setTopAnchor(revealButton, 30.0);
            AnchorPane.setRightAnchor(revealButton, 20.0);
            newPasswordEntryChildren.add(revealButton);
            passwordBox.getChildren().add(newPasswordEntry);
        }
    }



    @FXML
    protected void revealPassword(ActionEvent event) {
        Button revealButton = (Button) event.getSource();
        AnchorPane anchorPane = (AnchorPane) revealButton.getParent();
        Label password = (Label) anchorPane.getChildren().get(2);
        password.setVisible(!password.isVisible());
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
