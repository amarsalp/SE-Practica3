package evoting;

import data.*;
import evoting.VotingKiosk.Voter;
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


public class VotingKioskTestIncorrect {

    List<VotingOption> validParties;
    VotingKiosk votingKiosk;
    ElectoralOrganism electoralOrganism;
    LocalService localService;
    Scrutiny scrutiny;

    @BeforeEach
    void setUp() throws BadFormatException {
        validParties = new ArrayList(List.of(new VotingOption("Party1"),
                new VotingOption("Party2"),
                new VotingOption("Party3")));
        votingKiosk = new VotingKiosk(validParties);
        electoralOrganism = new ElectoralOrganismImpl();
        localService = new LocalServiceImpl();
        scrutiny = new ScrutinyImpl(validParties);
        votingKiosk.setScrutiny(scrutiny);
        votingKiosk.setElectoralOrganism(electoralOrganism);
        votingKiosk.setLocalService(localService);
    }

    /*
        Test no activeSession
        Test mal documento setDocuement
        Test mal eneterAccount


        Test no confirmIdentif
        assertThrows(InvalidDniException.class, () -> {
            votingKiosk.confirmVotingOption('c');
        });
        Test false nif, not in the dataase enterNif
        Test confultar un vopt falso consultVotingOption

        
        Test no activeSession vote
        Test no activeSession, no confirm vote ConfirmVotingOption
     */

    @Test
    @DisplayName("not a confirmIdentif")
    void confirmIdentTest() {
        assertThrows(InvalidDniException.class, () -> {
            votingKiosk.confirmIdentif('s');
        });
    }

    @Test
    @DisplayName("Error format Nif")
    void NotCorrectNifTest() throws BadFormatException{
        Nif nif = new Nif("A");
        assertThrows(BadFormatException.class, () -> {
            votingKiosk.enterNif(nif);
        });
    }

    @Test
    @DisplayName("Nif not in the db")
    void NotInDBNif() throws BadFormatException{
        Nif nif = new Nif("23456789A");
        assertThrows(NotEnabledException.class, () -> {
            votingKiosk.enterNif(nif);
        });
    }
    @Test
    @DisplayName("Null voting option")
    void NotConfirmedVote() throws ProceduralException, ConnectException {
        VotingOption vO = new VotingOption("Patata");
        votingKiosk.initVoting();
        votingKiosk.consultVotingOption(vO);
        assertEquals(vO,votingKiosk.selectedVO);
        votingKiosk.vote();
        assertEquals(vO, votingKiosk.selectedVO);
        votingKiosk.confirmVotingOption('s');
        assertNull(votingKiosk.toConfirmVO);
        assertNull(votingKiosk.selectedVO);
    }
    @Test
    @DisplayName("No active session")
    void NotActiveSession(){
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.vote();
        });
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.confirmVotingOption('c');
        });
    }
    @Test
    @DisplayName("Null voting option")
    void NullVotingOption() throws ProceduralException, ConnectException {
        VotingOption vO = new VotingOption("Patata");
        votingKiosk.initVoting();
        votingKiosk.consultVotingOption(vO);
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        assertEquals(1, scrutiny.getNulls());
        assertEquals(1, scrutiny.getTotal());
    }

    @Test
    @DisplayName("Test scrutiny blank vote")
    void BlankVoteTest() throws ProceduralException, ConnectException {
        VotingOption vO = new VotingOption("blankVote");
        votingKiosk.initVoting();
        votingKiosk.consultVotingOption(vO);
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        assertEquals(1, scrutiny.getBlanks());
        assertEquals(1, scrutiny.getTotal());
    }

    @Test
    @DisplayName("Test scrutiny Voting Option")
    void VotingOptionTest() throws ProceduralException, ConnectException {
        VotingOption vO = new VotingOption("Party1");
        votingKiosk.initVoting();
        votingKiosk.consultVotingOption(vO);
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        assertEquals(1, scrutiny.getVotesFor(vO));
        assertEquals(1, scrutiny.getTotal());
    }

}
