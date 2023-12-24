package data.interfaces;

import exceptions.dataExceptions.BadFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface EqualsAndHashCodeTest {
    @Test
    @DisplayName("Two equal objects should return true")
    void test_equals1() throws BadFormatException;

    @Test
    @DisplayName("Two different objects should return false")
    void test_equals2() throws BadFormatException;

    @Test
    @DisplayName("Two equal objects should return same hashcode")
    void test_hashCode1() throws BadFormatException;

    @Test
    @DisplayName("Two different objects should return same hashcode")
    void test_hashCode2() throws BadFormatException;
}
