package com.example.passwordmanager;

import javafx.fxml.FXML;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class InfoController {
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
