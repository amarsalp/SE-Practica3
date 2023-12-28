package data;

import data.interfaces.EqualsAndHashCodeTest;
import exceptions.dataExceptions.BadFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NifTest implements EqualsAndHashCodeTest {

    private Nif nif;
    private Nif nif2;

    @Test
    @DisplayName("getNif should return the nif")
    void test_getNif() throws BadFormatException {
        nif = new Nif("12345678A");
        assertEquals("12345678A",nif.getNif());

    }

    @Test
    @DisplayName("null nif should return NPE")
    void test_null_nif() {
        assertThrows(NullPointerException.class, () -> {
            nif = new Nif(null);
        });
    }

    @Test
    @DisplayName("nif with less than 9 characters should return BFE")
    void test_small_nif() {
        assertThrows(BadFormatException.class, () -> {
            nif = new Nif("1");
        });
    }

    @Test
    @DisplayName("nif with more than 9 characters should return BFE")
    void test_large_nif() {
        assertThrows(BadFormatException.class, () -> {
            nif = new Nif("123456789A");
        });
    }

    @Test
    @DisplayName("Digit as last character should return BFE")
    void test_nif_with_digit_last() {
        assertThrows(BadFormatException.class, () -> {
            nif = new Nif("123456781");
        });
    }

    @Test
    @DisplayName("Letter as first character should return BFE")
    void test_nif_with_letter_first() {
        assertThrows(BadFormatException.class, () -> {
            nif = new Nif("A2345678A");
        });
    }

    @Override
    @Test
    public void test_equals1() throws BadFormatException {
        nif = new Nif("12345678A");
        nif2 = new Nif("12345678A");
        assertTrue(nif.equals(nif2));
        assertTrue(nif2.equals(nif));
    }

    @Override
    @Test
    public void test_equals2() throws BadFormatException {
        nif = new Nif("12345678A");
        nif2 = new Nif("87654321B");
        assertFalse(nif.equals(nif2));
        assertFalse(nif2.equals(nif));
    }

    @Override
    @Test
    public void test_hashCode1() throws BadFormatException {
        nif = new Nif("12345678A");
        nif2 = new Nif("12345678A");
        assertEquals(nif.hashCode(), nif2.hashCode());
    }

    @Override
    @Test
    public void test_hashCode2() throws BadFormatException {
        nif = new Nif("12345678A");
        nif2 = new Nif("12345678B");
        assertNotEquals(nif.hashCode(), nif2.hashCode());
    }
}
