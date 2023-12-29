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

    //el mismo votante intenta votar dos veces
    @Test
    @DisplayName("If no voter has voted all the results of the scrutiny must be zero")
    void zero_votes_test() {
        votingKiosk.initVoting();
        assertEquals(0, scrutiny.getVotesFor(validParties.get(1)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(2)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(3)));
        assertEquals(0, scrutiny.getBlanks());
        assertEquals(0, scrutiny.getNulls());
        assertEquals(0, scrutiny.getTotal());
    }

    @Test
    @DisplayName("Correct voting session")
    void correct_voting_test() throws ProceduralException, InvalidAccountException, InvalidDniException,
            NotEnabledException, ConnectException, BadFormatException {
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
        assertEquals(1, scrutiny.getVotesFor(validParties.get(1)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(2)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(3)));
        assertEquals(1, scrutiny.getTotal());
    }

    @Test
    @DisplayName("Voting wit a different voter to see the persistence of the data on Scrutiny")
    void scrutiny_persistence_test() throws ProceduralException, InvalidAccountException, InvalidDniException,
            NotEnabledException, ConnectException, BadFormatException {
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
        assertEquals(1, scrutiny.getVotesFor(validParties.get(1)));
        assertEquals(1, scrutiny.getVotesFor(validParties.get(2)));
        assertEquals(0, scrutiny.getVotesFor(validParties.get(3)));
        assertEquals(2, scrutiny.getTotal());
    }
}
