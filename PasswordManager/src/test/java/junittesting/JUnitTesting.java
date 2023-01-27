package junittesting;

import com.example.passwordmanager.*;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JUnitTesting {

    // Boundary value analysis
    // and equivalence partitioning
    @Test
    void testNewController() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        NewController newController = new NewController();
        assertEquals(newController.validatePassword("1234567"), false);
        assertEquals(newController.validatePassword("12345678"), true);
        assertEquals(newController.validatePassword(null), false);
        // 127 chars
        assertEquals(newController.validatePassword("LgjTaxQIqzkIt3KJFevT4QXWpKkBLByZmmpZWoCKd1X8Y0A0WZY0q93JtgR8XBZf93EoL5GPvkkfmeUgiPgwxDJglgYaTCuG44JjPetYoNpgOjp3bzwY05lbdtuHj5d"), true);
        // 128 chars
        assertEquals(newController.validatePassword("LgjTaxQIqzkIt3KJFevT4QXWpKkBLByZmmpZWoCKd1X8Y0A0WZY0q93JtgR8XBZf93EoL5GPvkkfmeUgiPgwxDJglgYaTCuG44JjPetYoNpgOjp3bzwY05lbdtuHj5dA"), false);
        assertEquals(newController.addNewPassword(1, "New Password", "12345678", null), true);
        // assertEquals(newController.addNewPassword(1, "New Password", "12345678", null), true);
        assertEquals(newController.addNewPassword(3, "New Password", "12345678", ""), false);
    }

    @Test
    void testDeleteController() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        DeleteController deleteController = new DeleteController();
        assertEquals(deleteController.passwordExists(1, "pass", "12345678"), false);
        assertEquals(deleteController.passwordExists(3, "Password", "1234"), false);
        assertEquals(deleteController.deletePassword(1, "New Password", "LgjTaxQIqzkIt3KJFevT4QXWpKkBLByZmmpZWoCKd1X8Y0A0WZY0q93JtgR8XBZf93EoL5GPvkkfmeUgiPgwxDJglgYaTCuG44JjPetYoNpgOjp3bzwY05lbdtuHj5dA"), false);
        // assertEquals(deleteController.passwordExists(1, "pass", "12345678"), true);
    }

    @Test
    void testSignupController() throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SignupController signupController = new SignupController();
        assertEquals(signupController.usernameTaken("bob"), true);
        assertEquals(signupController.usernameTaken("alice"), false);
        // 30 char length
        assertEquals(signupController.validateUsername("PUwh7gHBDMzkbVjOs3y2vIfoFEcTTL"), true);
        // 31 char length
        assertEquals(signupController.validateUsername("PUwh7gHBDMzkbVjOs3y2vIfoFEcTTLA"), false);
        assertEquals(signupController.validateUsername("abc"), true);
        assertEquals(signupController.validateUsername("ab"), false);
        assertEquals(signupController.validateEmail("a@a"), false);
        assertEquals(signupController.validateEmail("a@ab"), true);
        assertEquals(signupController.validateEmail("abab"), false);
        // 40 char length
        assertEquals(signupController.validateEmail("AuY3iBf65eFRcLv1KzvAj@rGCgPRkLxOBdoEXQUu"), true);
        // 41 char length
        assertEquals(signupController.validateEmail("AuY3iBf65eFRcLv1KzvAj@rGCgPRkLxOBdoEXQUua"), false);
        assertEquals(signupController.addNewUser("alice", "123456789", "12345678", "Alice", "alice@example", "Bob's friend"), false);
        assertEquals(signupController.addNewUser("alice", "12345678", "12345678", "Alice", "alice@example", "Bob's friend"), true);
    }

    @Test
    void testLoginController() throws SQLException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException {
        LoginController loginController = new LoginController();
        assertEquals(loginController.authorizeUser("bob", "12345678"), true);
        assertEquals(loginController.authorizeUser("bob", "1234567"), false);
    }

    @Test
    void testManagerSecurity() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        ManagerSecurity managerSecurity = new ManagerSecurity();
        assertEquals(ManagerSecurity.getMasterPass(1), "12345678");
        assertEquals(ManagerSecurity.getMasterPass(3), "none");
    }

}