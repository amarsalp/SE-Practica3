package evoting;

import data.*;
import evoting.biometricdataperipheral.HumanBiometricScanner;
import evoting.biometricdataperipheral.PassportBiometricReader;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.evotingExceptions.BiometricVerificationFailedException;
import exceptions.evotingExceptions.NoExplicitConsentException;
import exceptions.evotingExceptions.PassportBiometricReadingException;
import exceptions.evotingExceptions.ProceduralException;
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

    PassportBiometricReader passportBiometricReader;
    HumanBiometricScanner humanBiometricScanner;

    @BeforeEach
    void setUp() throws BadFormatException {
        validParties = new ArrayList(List.of(new VotingOption("Party1"),
                new VotingOption("Party2"),
                new VotingOption("Party3")));
        scrutiny = new ScrutinyImpl();
        votingKiosk = new VotingKiosk(validParties, scrutiny);
        electoralOrganism = new ElectoralOrganismImpl();
        localService = new LocalServiceImpl();
        votingKiosk.setElectoralOrganism(electoralOrganism);
        votingKiosk.setLocalService(localService);
    }

    @Test
    @DisplayName("Error in Option selected")
    void NotCorrectOptionTest(){
        votingKiosk.opt = 'N';
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.grantExplicitConsent('c');
        });
    }

    @Test
    @DisplayName("Error in consent declined")
    void NotGrantConsentTest(){
        votingKiosk.opt = 'P';
        assertThrows(NoExplicitConsentException.class, () -> {
            votingKiosk.grantExplicitConsent('s');
        });
    }

    @Test
    @DisplayName("Error Option on ReadPasport")
    void NotCorrectOptReadPassportTest(){
        votingKiosk.opt = 'N';
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.readPassport();
        });
    }

    @Test
    @DisplayName("Error Option on readdFaceBiometrics")
    void NotCorrectOptReadFaceBiometricsTest(){
        votingKiosk.opt = 'N';
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.readFaceBiometrics();
        });
    }

    @Test
    @DisplayName("Error Option on readFingerPrintBiometrics")
    void NotCorrectOptReadFingerPrintBiometricsTest(){
        votingKiosk.opt = 'N';
        assertThrows(ProceduralException.class, () -> {
            votingKiosk.readFingerPrintBiometrics();
        });
    }

    @Test
    @DisplayName("Error on verification Biometric Data")
    void ErrorVerificationBioDataTest() throws PassportBiometricReadingException {
        votingKiosk.opt = 'P';
        votingKiosk.faceBiometric = passportBiometricReader.getPassportBiometricData();
        votingKiosk.fingerBiometric = ;
        assertThrows(BiometricVerificationFailedException.class, () -> {
            votingKiosk.readFingerPrintBiometrics("finger", "finger");
        });
    }

}
