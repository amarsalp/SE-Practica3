package evoting;

import data.Nif;
import data.SingleBiometricData;
import data.VotingOption;
import evoting.biometricdataperipheral.HumanBiometricScannerImpl;
import evoting.biometricdataperipheral.PassportBiometricReaderImpl;
import exceptions.dataExceptions.BadFormatException;
import exceptions.evotingExceptions.*;
import exceptions.serviceExceptions.NotEnabledException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.*;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VotingKioskPassportTest {
    VotingKiosk votingKiosk;
    List<VotingOption> validParties;
    Scrutiny scrutiny;
    PassportBiometricReaderImpl passportBiometricReader;
    HumanBiometricScannerImpl humanBiometricScanner;
    LocalService localService;
    ElectoralOrganism electoralOrganism;

    @BeforeEach
    void set_up() throws BadFormatException {
        validParties = new ArrayList<>(List.of(new VotingOption("Party1"), new VotingOption("Party2")));
        scrutiny = new ScrutinyImpl();
        votingKiosk = new VotingKiosk(validParties, scrutiny);
        passportBiometricReader = new PassportBiometricReaderImpl();
        votingKiosk.setPassportBiometricReader(passportBiometricReader);
        humanBiometricScanner = new HumanBiometricScannerImpl();
        votingKiosk.setHumanBiometricScanner(humanBiometricScanner);
        localService = new LocalServiceImpl();
        votingKiosk.setLocalService(localService);
        electoralOrganism = new ElectoralOrganismImpl();
        votingKiosk.setElectoralOrganism(electoralOrganism);
    }

    @Test
    @DisplayName("Correct voting session")
    void correct_voting_test() throws NoExplicitConsentException, ProceduralException, PassportBiometricReadingException,
            NotValidPassportException, HumanBiometricScanningException, BiometricVerificationFailedException,
            NotEnabledException, ConnectException, BadFormatException, InterruptedException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        votingKiosk.readFaceBiometrics();
        votingKiosk.readFingerPrintBiometrics();
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
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        votingKiosk.readFaceBiometrics();
        assertThrows(NotEnabledException.class, () -> {
            votingKiosk.readFingerPrintBiometrics();
        });
        //Another voter votes, the data on Scrutiny has to be persistent
        humanBiometricScanner.setFinger(new SingleBiometricData("finger1".getBytes()));
        humanBiometricScanner.setFace(new SingleBiometricData("face1".getBytes()));
        passportBiometricReader.setNewVoter(new SingleBiometricData("finger1".getBytes()),
                new SingleBiometricData("face1".getBytes()),
                new Nif("87654321B"));
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        votingKiosk.readFaceBiometrics();
        votingKiosk.readFingerPrintBiometrics();
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
    void BlankVoteTest() throws ProceduralException, ConnectException, NotEnabledException,
            NoExplicitConsentException, PassportBiometricReadingException, NotValidPassportException,
            HumanBiometricScanningException, BiometricVerificationFailedException, InterruptedException {

        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        votingKiosk.readFaceBiometrics();
        votingKiosk.readFingerPrintBiometrics();
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(new VotingOption("blankVote"));
        votingKiosk.vote();
        votingKiosk.confirmVotingOption('c');
        //blank vote has been scrutinized correctly
        assertEquals(1, scrutiny.getBlanks());
        assertEquals(1, scrutiny.getTotal());
    }
}