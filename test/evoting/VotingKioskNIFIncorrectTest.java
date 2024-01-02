package evoting;

import data.*;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.evotingExceptions.*;
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


public class VotingKioskNIFIncorrectTest {

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
    @DisplayName("the voter should not be able to to user a different identification method than the selected")
    void wrong_identification_method() {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.grantExplicitConsent('c');
            votingKiosk.readPassport();
            votingKiosk.readFaceBiometrics();
            votingKiosk.readFingerPrintBiometrics();
        });
    }

    @Test
    @DisplayName("the support personal enters an incorrect user or password")
    void wrong_enterAccount() {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        //user does not exist
        assertThrows(InvalidAccountException.class, () -> {
            votingKiosk.enterAccount("a", new Password("Password1"));
        });
        //password is incorrect
        assertThrows(InvalidAccountException.class, () -> {
            votingKiosk.enterAccount("user", new Password("WrongPass1"));
        });
    }

    @Test
    @DisplayName("the support personal dos not accept the Nif provided")
    void not_confirmIdent() throws BadFormatException, ProceduralException, InvalidAccountException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        assertThrows(InvalidDniException.class, () -> {
            votingKiosk.confirmIdentif('s');
        });
    }

    @Test
    @DisplayName("the support personal enters a non valid nif")
    void non_valid_nif() throws BadFormatException, ProceduralException, InvalidAccountException, InvalidDniException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        //the support personal enters a nif that is not in correct format
        assertThrows(BadFormatException.class, () -> {
            votingKiosk.enterNif(new Nif("A"));
        });
        //the nif is not on the electoral organism database (the voter is not in the correct vote center)
        assertThrows(NotEnabledException.class, () -> {
            votingKiosk.enterNif(new Nif("23456789A"));
        });
    }

    @Test
    @DisplayName("the voter tries the vote option without selecting consulting one previously")
    void not_selected_VO() throws BadFormatException, ProceduralException, InvalidAccountException, InvalidDniException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        votingKiosk.initOptionsNavigation();
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.vote();
        });
    }

    @Test
    @DisplayName("the voter tries the vote option without starting a session")
    void no_active_session() throws BadFormatException, ProceduralException, InvalidAccountException, InvalidDniException {
        votingKiosk.consultVotingOption(validParties.get(1));
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.vote();
        });
    }

    @Test
    @DisplayName("the voter tries the confirm vote option without selecting vote previously")
    void not_selected_VO_2() throws BadFormatException, ProceduralException, InvalidAccountException, InvalidDniException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(validParties.get(1));
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.confirmVotingOption('c');
        });
    }

    @Test
    @DisplayName("Null voting option")
    void null_voting_option() throws ProceduralException, BadFormatException, InvalidAccountException, InvalidDniException, ConnectException, InterruptedException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(new VotingOption("Patata"));
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        assertEquals(1, scrutiny.getNulls());
        assertEquals(1, scrutiny.getTotal());
    }

    @Test
    @DisplayName("if the voter does not confirm his selection his previous selections are set to null")
    void not_confirmed_vote() throws ProceduralException, BadFormatException, InvalidAccountException, InvalidDniException, ConnectException, InterruptedException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('N');
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(new VotingOption("Patata"));
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('n');
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.vote();
        });
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.confirmVotingOption('c');
        });
    }
}
