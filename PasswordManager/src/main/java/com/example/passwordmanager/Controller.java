package com.example.passwordmanager;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Controller {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private VBox passwordBox;
    @FXML
    private HBox passwordEntry;

    public Connection getConnection() {
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

    @FXML
    protected void onNewButtonClick() {
        HBox newPasswordEntry = new HBox();
        ObservableList<Node> newPasswordEntryChildren = newPasswordEntry.getChildren();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(140);
        imageView.setFitHeight(100);
        newPasswordEntryChildren.add(imageView);
        Label websiteLabel = new Label("Website");
        HBox.setMargin(websiteLabel, new Insets(40, 0, 0, 10));
        newPasswordEntryChildren.add(websiteLabel);
        Label passwordLabel = new Label("Password");
        HBox.setMargin(passwordLabel, new Insets(40, 0, 0, 50));
        newPasswordEntryChildren.add(passwordLabel);
        Button revealButton = new Button("Reveal");
        revealButton.setPrefWidth(55);
        revealButton.setPrefHeight(20);
        revealButton.setMaxWidth(55);
        revealButton.setMaxHeight(20);
        HBox.setMargin(revealButton, new Insets(37, 0, 0, 50));
        newPasswordEntryChildren.add(revealButton);
        passwordBox.getChildren().add(newPasswordEntry);
    }


    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    protected void getPasswordManagerScene() throws IOException, SQLException {
        Connection connection = getConnection();
        System.out.println(username.getText());
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id='Mexico'");
        connection.close();

        Parent pane = FXMLLoader.load(getClass().getResource("password-manager.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getNewPasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("new-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getDeletePasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("delete-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getUserInfoScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("user-info.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getMasterPasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("master-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getAboutScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("about.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getLoginScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("login.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }





}