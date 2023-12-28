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

public class VotingKioskTest {
    List<VotingOption> validParties;
    VotingKiosk votingKiosk;
    ElectoralOrganism electoralOrganism;
    LocalService localService;
    Scrutiny scrutiny;

    @BeforeEach
    void setUp() throws BadFormatException{
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
    @DisplayName("Correct voting session")
    void test_Voting1() throws ProceduralException, InvalidAccountException, InvalidDniException, NotEnabledException, ConnectException, BadFormatException {
        votingKiosk.initVoting();
        assertTrue(votingKiosk.activeSession);
        votingKiosk.setDocument('N');
        assertEquals('N', votingKiosk.opt);
        votingKiosk.enterAccount("user", new Password("Password1"));
        votingKiosk.confirmIdentif('c');
        Nif nif = new Nif("12345678A");
        votingKiosk.enterNif(nif);
        assertEquals(nif, votingKiosk.voter.nif);
        assertEquals(Voter.State.enabled, votingKiosk.voter.state);
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(validParties.get(1));
        assertEquals(votingKiosk.selectedVO, validParties.get(1));
        votingKiosk.vote();
        assertEquals(votingKiosk.toConfirmVO, validParties.get(1));
        votingKiosk.confirmVotingOption('c');
        assertEquals(1, scrutiny.getVotesFor(validParties.get(1)));
        assertThrows(NotEnabledException.class, () -> {
            electoralOrganism.canVote(nif);
        });
        assertEquals(Voter.State.disabled, votingKiosk.voter.state);

    }

    @Test
    @DisplayName("Correct voting session")
    void test_Voting2()  {
        //
    }
}
