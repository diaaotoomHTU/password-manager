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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

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
    protected void getPasswordManagerScene() throws IOException {
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