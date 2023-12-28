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

}
