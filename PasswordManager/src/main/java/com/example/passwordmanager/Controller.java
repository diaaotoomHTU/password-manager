package com.example.passwordmanager;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

public class Controller {

    @FXML
    private Label welcomeText;


    @FXML
    VBox passwordBox;
    @FXML
    AnchorPane passwordEntry;

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

    protected void displayAllPasswords(VBox passwordBox) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=?");
        userQuery.setInt(1, currentUserID);
        ResultSet resultSet = userQuery.executeQuery();
        String masterPass = getMasterPass();
        int i = 1;
        while (resultSet.next()) {
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

            Label passwordLabel = new Label(decrypt(masterPass, resultSet.getString(4)));
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
            ++i;
        }
    }


    @FXML
    TextField username;

    static private int currentUserID = -1;

    @FXML
    TextField password;

    @FXML
    protected void getPasswordManagerScene() throws IOException, SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        if (currentUserID == -1) {
            authorizeUser();
        }
        if (currentUserID == -1) {
            return;
        }
        Parent pane = FXMLLoader.load(getClass().getResource("password-manager.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
        ScrollPane scrollPane = (ScrollPane) pane.getChildrenUnmodifiable().get(1);
        AnchorPane anchorPane = (AnchorPane) scrollPane.contentProperty().get();
        VBox vbox = (VBox) anchorPane.getChildren().get(0);
        displayAllPasswords(vbox);
    }

    @FXML
    protected void revealPassword(ActionEvent event) {
        Button revealButton = (Button) event.getSource();
        AnchorPane anchorPane = (AnchorPane) revealButton.getParent();
        Label password = (Label) anchorPane.getChildren().get(2);
        password.setVisible(!password.isVisible());
    }
    protected  void authorizeUser() throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE username=? AND master_password=?");
        userQuery.setString(1, username.getText());
        userQuery.setString(2, encrypt("masterpassword12", password.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            currentUserID = resultSet.getInt(1);
        }
        connection.close();
    }

    @FXML
    protected void getNewPasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("new-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

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
        Connection connection = getConnection();
        String masterPass = getMasterPass();
        PreparedStatement insertPassword = connection.prepareStatement("INSERT INTO passwords (pm_user_id, password_name, password, image) VALUES (?, ?, ?, ?)");
        insertPassword.setInt(1, currentUserID);
        insertPassword.setString(2, newPasswordName.getText());
        insertPassword.setString(3, encrypt(masterPass, newPassword.getText()));
        insertPassword.setString(4, newPasswordImage.getText());
        insertPassword.execute();
        getPasswordManagerScene();
    }


    @FXML
    protected void getDeletePasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("delete-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    TextField confirmPasswordName;

    @FXML
    TextField confirmPassword;
    @FXML
    protected  void deletePassword() throws SQLException, IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        String masterPass = getMasterPass();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=? AND password_name =? AND password=?");
        userQuery.setInt(1, currentUserID);
        userQuery.setString(2, confirmPasswordName.getText());
        userQuery.setString(3, encrypt(masterPass, confirmPassword.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            userQuery = connection.prepareStatement("DELETE FROM passwords WHERE pm_user_id=? AND password_name =? AND password=?");
            userQuery.setInt(1, currentUserID);
            userQuery.setString(2, confirmPasswordName.getText());
            userQuery.setString(3, encrypt(masterPass, confirmPassword.getText()));
            userQuery.execute();
            getPasswordManagerScene();
        }
    }

    @FXML
    TextField confirmDeleteAll;

    @FXML
    protected void deleteAllPasswords() throws SQLException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=? AND master_password=?");
        userQuery.setInt(1, currentUserID);
        userQuery.setString(2, encrypt("masterpassword12", confirmDeleteAll.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            userQuery = connection.prepareStatement("DELETE FROM passwords WHERE pm_user_id=?");
            userQuery.setInt(1, currentUserID);
            userQuery.execute();
            getPasswordManagerScene();
        }
    }

    @FXML
    protected void getUserInfoScene() throws IOException, SQLException {
        Parent pane = FXMLLoader.load(getClass().getResource("user-info.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
        ScrollPane scrollPane = (ScrollPane) pane.getChildrenUnmodifiable().get(1);
        AnchorPane anchorPane = (AnchorPane) scrollPane.contentProperty().get();

        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=?");
        userQuery.setInt(1, currentUserID);
        ResultSet resultSet = userQuery.executeQuery();
        resultSet.next();

        Label currentLabel = (Label) anchorPane.getChildren().get(2);
        currentLabel.setText(resultSet.getString(3));
        currentLabel = (Label) anchorPane.getChildren().get(4);
        currentLabel.setText(resultSet.getString(5));
        currentLabel = (Label) anchorPane.getChildren().get(6);
        currentLabel.setText(resultSet.getString(4));

        userQuery = connection.prepareStatement("SELECT COUNT(id) FROM passwords WHERE pm_user_id=?");
        userQuery.setInt(1, currentUserID);
        resultSet = userQuery.executeQuery();
        resultSet.next();
        currentLabel = (Label) anchorPane.getChildren().get(8);
        currentLabel.setText(Integer.toString(resultSet.getInt(1)));

        connection.close();
    }

    @FXML
    TextField oldMasterPassword;

    @FXML
    TextField newMasterPassword;

    @FXML
    protected void changeMasterPassword() throws SQLException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=? AND master_password=?");
        userQuery.setInt(1, currentUserID);
        userQuery.setString(2, encrypt("masterpassword12", oldMasterPassword.getText()));
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            String oldPassword = getMasterPass();
            userQuery = connection.prepareStatement("UPDATE pm_users SET master_password=? WHERE id=?");
            String newPass = encrypt("masterpassword12", newMasterPassword.getText());
            userQuery.setString(1, newPass);
            userQuery.setInt(2, currentUserID);
            userQuery.execute();
            userQuery = connection.prepareStatement("SELECT * FROM passwords WHERE pm_user_id=?");
            userQuery.setInt(1, currentUserID);
            resultSet = userQuery.executeQuery();
            while (resultSet.next()) {

                PreparedStatement updatePassQuery = connection.prepareStatement("UPDATE passwords SET password=? WHERE pm_user_id=? AND password=?");
                updatePassQuery.setString(1, encrypt(newMasterPassword.getText(), decrypt(oldPassword, resultSet.getString(4))));
                updatePassQuery.setInt(2, currentUserID);
                updatePassQuery.setString(3, resultSet.getString(4));
                updatePassQuery.execute();
            }
            getPasswordManagerScene();
        }
    }


    @FXML
    protected void getMasterPasswordScene() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("master-password.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getAboutScene() throws IOException, SQLException {
        Parent pane = FXMLLoader.load(getClass().getResource("about.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    @FXML
    protected void getLoginScene() throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        currentUserID = -1;
        Parent pane = FXMLLoader.load(getClass().getResource("login.fxml"));
        Application.loadedStage.getScene().setRoot(pane);
        Application.loadedStage.sizeToScene();
        Application.loadedStage.centerOnScreen();
    }

    String getMasterPass() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=?");
        userQuery.setInt(1, currentUserID);
        ResultSet resultSet = userQuery.executeQuery();
        resultSet.next();
        return decrypt("masterpassword12", resultSet.getString(6));
    }


    // Encryption code
    String encrypt(String initialKey, String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        KeySpec spec = new PBEKeySpec(initialKey.toCharArray(), salt, 1024, 128); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        Key aesKey = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return turnToString(encrypted);
    }

    String decrypt(String initialKey, String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        KeySpec spec = new PBEKeySpec(initialKey.toCharArray(), salt, 1024, 128); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        Key aesKey = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(turnToBytes(encrypted)), StandardCharsets.UTF_8);
        return decrypted;
    }

    byte[] turnToBytes(String string) {
        return Base64.getDecoder().decode(string);
    }

    String turnToString(byte[] array) {
        return Base64.getEncoder().withoutPadding().encodeToString(array);
        // return Base64.getEncoder().encodeToString(array);
    }

}