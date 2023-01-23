package com.example.passwordmanager;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class ManagerSecurity {

    static String getMasterPass(int userID) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Connection connection = LoginController.getConnection();
        PreparedStatement userQuery = connection.prepareStatement("SELECT * FROM pm_users WHERE id=?");
        userQuery.setInt(1, userID);
        ResultSet resultSet = userQuery.executeQuery();
        if (resultSet.next()) {
            return decrypt("masterpassword12", resultSet.getString(6));
        }
        return "none";
    }


    // Encryption code
    static String encrypt(String initialKey, String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
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

    static String decrypt(String initialKey, String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
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

    static byte[] turnToBytes(String string) {
        return Base64.getDecoder().decode(string);
    }

    static String turnToString(byte[] array) {
        return Base64.getEncoder().withoutPadding().encodeToString(array);
    }
}
