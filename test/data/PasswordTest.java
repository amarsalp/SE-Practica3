package data;

import data.interfaces.EqualsAndHashCodeTest;
import exceptions.dataExceptions.BadFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordTest implements EqualsAndHashCodeTest {
    private Password password;
    private Password password2;

    @Test
    @DisplayName("getPassword should return the password")
    void test_getPassword() throws BadFormatException {
        password = new Password("Password12");
        assertEquals("Password12", password.getPassword());
    }

    @Test
    @DisplayName("null password should return NPE")
    void test_null_password() {
        assertThrows(NullPointerException.class, () -> {
            password = new Password(null);
        });
    }

    @Test
    @DisplayName("password with less than 8 characters should return BFE")
    void test_small_password() {
        assertThrows(BadFormatException.class, () -> {
            password = new Password("a");
        });
    }

    @Test
    @DisplayName("password without lower case should return BFE")
    void test_password_without_lower() {
        assertThrows(BadFormatException.class, () -> {
            password = new Password("ABCDEFG1");
        });
    }

    @Test
    @DisplayName("password without upper case should return BFE")
    void test_password_without_upper() {
        assertThrows(BadFormatException.class, () -> {
            password = new Password("abcdefg1");
        });
    }

    @Test
    @DisplayName("password without digit should return BFE")
    void test_password_without_digit() {
        assertThrows(BadFormatException.class, () -> {
            password = new Password("Abcdefgh");
        });
    }

    @Override
    @Test
    public void test_equals1() throws BadFormatException {
        password = new Password("Abcdefg1");
        password2 = new Password("Abcdefg1");
        assertTrue(password.equals(password2));
        assertTrue(password2.equals(password));
    }

    @Override
    @Test
    public void test_equals2() throws BadFormatException {
        password = new Password("Abcdefg1");
        password2 = new Password("Abcdefg2");
        assertFalse(password.equals(password2));
        assertFalse(password2.equals(password));
    }

    @Override
    @Test
    public void test_hashCode1() throws BadFormatException {
        password = new Password("Abcdefg1");
        password2 = new Password("Abcdefg1");
        assertEquals(password.hashCode(), password.hashCode());
    }

    @Override
    @Test
    public void test_hashCode2() throws BadFormatException {
        password = new Password("Abcdefg1");
        password2 = new Password("Abcdefg2");
        assertNotEquals(password.hashCode(), password2.hashCode());
    }
}
