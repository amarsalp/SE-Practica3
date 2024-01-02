package evoting;

import data.*;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.evotingExceptions.ProceduralException;
import exceptions.serviceExceptions.InvalidAccountException;
import exceptions.serviceExceptions.NotEnabledException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.*;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VotingKioskNIFTest {
    /*TESTS WHERE THE VOTER FOLLOWS THE CORRECT SEQUENCE FOR VOTING*/
    List<VotingOption> validParties;
    VotingKiosk votingKiosk;
    ElectoralOrganism electoralOrganism;
    LocalService localService;
    Scrutiny scrutiny;

    @BeforeEach
    void setUp() throws BadFormatException {
        validParties = new ArrayList(List.of(new VotingOption("Party1"), new VotingOption("Party2")));
        scrutiny = new ScrutinyImpl();
        votingKiosk = new VotingKiosk(validParties, scrutiny);
        electoralOrganism = new ElectoralOrganismImpl();
        localService = new LocalServiceImpl();
        votingKiosk.setElectoralOrganism(electoralOrganism);
        votingKiosk.setLocalService(localService);
    }

    @Test
    @DisplayName("If no voter has voted, all the results of the scrutiny must be zero")
    void zero_votes_test() {
        votingKiosk.initVoting();
        assertEquals(0, scrutiny.getVotesFor(validParties.get(1)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(2)));
        assertEquals(0, scrutiny.getBlanks());
        assertEquals(0, scrutiny.getNulls());
        assertEquals(0, scrutiny.getTotal());
    }

    @Test
    @DisplayName("Correct voting session")
    void correct_voting_test() throws ProceduralException, InvalidAccountException, InvalidDniException,
            NotEnabledException, ConnectException, BadFormatException, InterruptedException {

        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        Nif nif = new Nif("12345678A");
        votingKiosk.enterNif(nif);
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(validParties.get(1));
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        //The voted party has been correctly scrutinized
        assertEquals(1, scrutiny.getVotesFor(validParties.get(1)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(2)));
        assertEquals(0, scrutiny.getNulls());
        assertEquals(0, scrutiny.getBlanks());
        assertEquals(1, scrutiny.getTotal());
        //The voter is not able to vote again
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        assertThrows(NotEnabledException.class, () -> {
            votingKiosk.enterNif(nif);
        });
        //Another voter votes, the data on Scrutiny has to be persistent
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        Nif nif2 = new Nif("87654321B");
        votingKiosk.enterNif(nif2);
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(validParties.get(1));
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        assertEquals(2, scrutiny.getVotesFor(validParties.get(1)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(2)));
        assertEquals(0, scrutiny.getNulls());
        assertEquals(0, scrutiny.getBlanks());
        assertEquals(2, scrutiny.getTotal());
    }

    @Test
    @DisplayName("Test scrutiny blank vote")
    void blank_vote_test() throws ProceduralException, ConnectException, BadFormatException,
            InvalidAccountException, InvalidDniException, NotEnabledException, InterruptedException {

        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        Nif nif = new Nif("12345678A");
        votingKiosk.enterNif(nif);
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(new VotingOption("blankVote"));
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        //blank vote has been scrutinized correctly
        assertEquals(1, scrutiny.getBlanks());
        assertEquals(1, scrutiny.getTotal());
    }
}
