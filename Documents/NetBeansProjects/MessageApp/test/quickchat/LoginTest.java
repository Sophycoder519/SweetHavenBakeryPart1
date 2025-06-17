package quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    Login login = new Login();

    @Test
    public void testCorrectUsername() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testIncorrectUsername() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testCorrectPassword() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testIncorrectPassword() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCorrectCellNumber() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testIncorrectCellNumber() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testRegisterUser_Success() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Smith");
        assertTrue(result.contains("Username successfully captured."));
        assertTrue(result.contains("Password successfully captured."));
        assertTrue(result.contains("Cell phone number successfully captured."));
    }

    @Test
    public void testRegisterUser_Failure() {
        String result = login.registerUser("kyle", "password", "08966553", "John", "Smith");
        assertTrue(result.contains("Username is not correctly formatted"));
        assertTrue(result.contains("Password is not correctly formatted"));
        assertTrue(result.contains("Cell number is incorrectly formatted"));
    }

    @Test
    public void testLoginSuccess() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFail() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Smith");
        assertFalse(login.loginUser("wrong", "wrong"));
    }

    @Test
    public void testLoginStatusSuccess() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "John", "Smith");
        assertEquals("Welcome John, Smith it is great to see you again.", login.returnLoginStatus(true));
    }

    @Test
    public void testLoginStatusFail() {
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }
}
