package data;

import data.interfaces.EqualsAndHashCodeTest;
import exceptions.dataExceptions.BadFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VotingOptionTest implements EqualsAndHashCodeTest {
    VotingOption votingOption;
    VotingOption votingOption2;

    @Test
    @DisplayName("getParty should return the party")
    void test_getParty() throws BadFormatException {
        votingOption = new VotingOption("Party1");
        assertEquals("Party1", votingOption.getParty());
    }

    @Test
    @DisplayName("null party should return NPE")
    void test_null_party() {
        assertThrows(NullPointerException.class, () -> {
            votingOption = new VotingOption(null);
        });
    }

    @Override
    @Test
    public void test_equals1() throws BadFormatException {
        votingOption = new VotingOption("party1");
        votingOption2 = new VotingOption("party1");
        assertEquals(votingOption, votingOption2);
        assertEquals(votingOption2, votingOption);
    }

    @Override
    @Test
    public void test_equals2() throws BadFormatException {
        votingOption = new VotingOption("party1");
        votingOption2 = new VotingOption("party2");
        assertNotEquals(votingOption, votingOption2);
        assertNotEquals(votingOption2, votingOption);
    }

    @Override
    @Test
    public void test_hashCode1() throws BadFormatException {
        votingOption = new VotingOption("party1");
        votingOption2 = new VotingOption("party1");
        assertEquals(votingOption.hashCode(), votingOption2.hashCode());
    }

    @Override
    @Test
    public void test_hashCode2() throws BadFormatException {
        votingOption = new VotingOption("party1");
        votingOption2 = new VotingOption("party2");
        assertNotEquals(votingOption.hashCode(), votingOption2.hashCode());
    }
}
