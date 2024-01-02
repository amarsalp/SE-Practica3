package evoting;

import data.*;
import evoting.biometricdataperipheral.HumanBiometricScanner;
import evoting.biometricdataperipheral.HumanBiometricScannerImpl;
import evoting.biometricdataperipheral.PassportBiometricReader;
import evoting.biometricdataperipheral.PassportBiometricReaderImpl;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.evotingExceptions.*;
import exceptions.serviceExceptions.NotEnabledException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.*;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VotingKioskPassportIncorrectTest {
    List<VotingOption> validParties;
    VotingKiosk votingKiosk;
    ElectoralOrganism electoralOrganism;
    LocalService localService;
    Scrutiny scrutiny;
    PassportBiometricReaderImpl passportBiometricReader;
    HumanBiometricScannerImpl humanBiometricScanner;

    @BeforeEach
    void setUp() throws BadFormatException {
        validParties = new ArrayList(List.of(new VotingOption("Party1"), new VotingOption("Party2")));
        scrutiny = new ScrutinyImpl();
        votingKiosk = new VotingKiosk(validParties, scrutiny);
        electoralOrganism = new ElectoralOrganismImpl();
        localService = new LocalServiceImpl();
        passportBiometricReader = new PassportBiometricReaderImpl();
        humanBiometricScanner = new HumanBiometricScannerImpl();
        votingKiosk.setElectoralOrganism(electoralOrganism);
        votingKiosk.setLocalService(localService);
        votingKiosk.setPassportBiometricReader(passportBiometricReader);
        votingKiosk.setHumanBiometricScanner(humanBiometricScanner);
    }

    @Test
    @DisplayName("the voter should not be able to to user a different identification method than the selected")
    void wrong_identification_method() {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.enterAccount("user", new Password("Password1"));
        });
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.confirmIdentif('c');
        });
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.enterNif(new Nif("12345678A"));
        });
    }

    @Test
    @DisplayName("if the voter declines the explicit consent, the voter is not able to do the rest of the operations")
    void not_grant_consent() {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        assertThrows(NoExplicitConsentException.class, () -> {
            votingKiosk.grantExplicitConsent('n');
        });
        assertThrows(NoExplicitConsentException.class, () -> {
            votingKiosk.readPassport();
        });
        assertThrows(NoExplicitConsentException.class, () -> {
            votingKiosk.readFaceBiometrics();
        });
        assertThrows(NoExplicitConsentException.class, () -> {
            votingKiosk.readFingerPrintBiometrics();
        });
    }

    @Test
    @DisplayName("the passport provided is not valid")
    void not_valid_passport() throws NoExplicitConsentException, ProceduralException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        passportBiometricReader.setValidPassport(false);
        assertThrows(NotValidPassportException.class, () -> {
            votingKiosk.readPassport();
        });
    }

    @Test
    @DisplayName("votingkiosk can not read the passport biometrics")
    void not_valid_passport_biometrics() throws NoExplicitConsentException, ProceduralException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        passportBiometricReader.setValidBiometric(false);
        assertThrows(PassportBiometricReadingException.class, () -> {
            votingKiosk.readPassport();
        });
    }

    @Test
    @DisplayName("votingkiosk can not read face biometrics")
    void not_valid_face_biometrics() throws NoExplicitConsentException, ProceduralException, PassportBiometricReadingException,
            NotValidPassportException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        humanBiometricScanner.setValidFace(false);
        assertThrows(HumanBiometricScanningException.class, () -> {
            votingKiosk.readFaceBiometrics();
        });
    }

    @Test
    @DisplayName("votingkiosk can not read finger biometrics")
    void not_valid_finger_biometrics() throws NoExplicitConsentException, ProceduralException, PassportBiometricReadingException,
            NotValidPassportException, HumanBiometricScanningException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        humanBiometricScanner.setValidFinger(false);
        votingKiosk.readFaceBiometrics();
        assertThrows(HumanBiometricScanningException.class, () -> {
            votingKiosk.readFingerPrintBiometrics();
        });
    }

    @Test
    @DisplayName("passport and human biometrics does not match")
    void not_matching_biometrics() throws NoExplicitConsentException, ProceduralException, PassportBiometricReadingException,
            NotValidPassportException, HumanBiometricScanningException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('P');
        votingKiosk.grantExplicitConsent('c');
        votingKiosk.readPassport();
        humanBiometricScanner.setFace(new SingleBiometricData("differentFace".getBytes()));
        humanBiometricScanner.setFinger(new SingleBiometricData("differentFinger".getBytes()));
        votingKiosk.readFaceBiometrics();
        assertThrows(BiometricVerificationFailedException.class, () -> {
            votingKiosk.readFingerPrintBiometrics();
        });
    }

}
