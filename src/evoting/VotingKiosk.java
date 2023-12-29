package evoting;

import data.*;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.evotingExceptions.*;
import exceptions.serviceExceptions.InvalidAccountException;
import exceptions.serviceExceptions.NotEnabledException;
import services.ElectoralOrganism;
import services.LocalService;
import services.Scrutiny;
import evoting.biometricdataperipheral.PassportBiometricReader;
import evoting.biometricdataperipheral.HumanBiometricScanner;

import java.net.ConnectException;
import java.util.Iterator;
import java.util.List;


/**
 * Internal classes involved in the exercise of the vote
 */
public class VotingKiosk {

    // Class members
    VotingOption selectedVO, toConfirmVO;
    Boolean activeSession;
    Voter voter;
    char opt, cons;
    List<VotingOption> validParties;
    SingleBiometricData faceBiometric, fingerBiometric;
    LocalService localService;
    ElectoralOrganism electoralOrganism;
    Scrutiny scrutiny;
    PassportBiometricReader passportBiometricReader;
    HumanBiometricScanner humanBiometricScanner;

    // Constructor
    public VotingKiosk(List<VotingOption> validParties) {
        activeSession = false;
        selectedVO = null;
        toConfirmVO = null;
        this.validParties = validParties;
        this.validParties.add(new VotingOption("blankVote"));
        this.voter = new Voter();
    }

    // Input events
    public void initVoting() {
        activeSession = true;
        scrutiny.initVoteCount(validParties);
    }

    public void setDocument(char opt) {
        //If the opt is 'N' the user will use the NIF and if the opt is 'P' the user will use the Passport
        this.opt = opt;
    }

    public void enterAccount(String login, Password pssw) throws InvalidAccountException, ProceduralException {
        if (opt != 'N') throw new ProceduralException("Invalid operation");
        localService.verifyAccount(login, pssw);
        System.out.println("Account is valid");
    }

    public void grantExplicitConsent(char cons) throws NoExplicitConsentException, ProceduralException {
        if (opt != 'P') throw new ProceduralException("Invalid Operation");
        //The letter 'c' is used to say that the voter consent the process
        if (cons != 'c') throw new NoExplicitConsentException("The consent was declined");
        this.cons = cons;
    }

    public void confirmIdentif(char conf) throws InvalidDniException, ProceduralException {
        if (opt != 'N') throw new ProceduralException("Invalid operation");
        //The letter 'c' is used to say it's a confirmations from the support staff .
        if (conf != 'c') throw new InvalidDniException("Not valid identification");
    }

    public void enterNif(Nif nif) throws NotEnabledException, ConnectException, BadFormatException, ProceduralException {
        if (opt != 'N') throw new ProceduralException("Invalid operation");
        //Check NIF
        electoralOrganism.canVote(new Nif(nif.getNif()));
        voter.state = Voter.State.enabled;
        voter.nif = nif;
        System.out.println("The nif is able to vote");
    }

    public void readPassport() throws NotValidPassportException, PassportBiometricReadingException, ProceduralException, NoExplicitConsentException {
        if (opt != 'P') throw new ProceduralException("Document not valid here");
        grantExplicitConsent(cons);
        passportBiometricReader.validatePassport();
        voter.biometricData = passportBiometricReader.getPassportBiometricData();
        voter.nif = passportBiometricReader.getNifWithOCR();
        System.out.println("Passport read correctly");
    }

    public void readFaceBiometrics() throws HumanBiometricScanningException, NoExplicitConsentException, ProceduralException {
        if (opt != 'P') throw new ProceduralException("Document not valid here");
        grantExplicitConsent(cons);
        faceBiometric = humanBiometricScanner.scanFaceBiometrics();
    }

    public void readFingerPrintBiometrics()
            throws NotEnabledException, HumanBiometricScanningException,
            BiometricVerificationFailedException, ConnectException, NoExplicitConsentException, ProceduralException {
        if (opt != 'P') throw new ProceduralException("Document not valid here");
        grantExplicitConsent(cons);
        fingerBiometric = humanBiometricScanner.scanFingerprintBiometrics();
        verifiyBiometricData(new BiometricData(fingerBiometric, faceBiometric), voter.biometricData);
        electoralOrganism.canVote(voter.nif);
        voter.state = Voter.State.enabled;
    }

    public void initOptionsNavigation() {
        Iterator<VotingOption> it = validParties.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().getParty());
        }
    }

    public void consultVotingOption(VotingOption vopt) {
        this.selectedVO = new VotingOption(vopt.getParty());
    }

    public void vote() throws ProceduralException {
        if (selectedVO == null || !activeSession)
            throw new ProceduralException("The voter has to select a party or init a session");
        toConfirmVO = selectedVO;
        System.out.println("Party selected, confirmation of the selection needed");
    }

    public void confirmVotingOption(char conf) throws ConnectException, ProceduralException {
        if (toConfirmVO == null || !activeSession)
            throw new ProceduralException("The voter has to select a party or init a session");
        //The letter 'c' is used to say it's a confirmations from the voter
        if (conf == 'c') {
            scrutiny.scrutinize(selectedVO);
            electoralOrganism.disableVoter(voter.nif);
            voter.state = Voter.State.disabled;
            finalizeSession();
        } else {
            toConfirmVO = null;
            selectedVO = null;
        }
    }

    // Internal operations
    private void finalizeSession() {
        activeSession = false;
    }

    private void verifiyBiometricData(BiometricData humanBioD, BiometricData passpBioD)
            throws BiometricVerificationFailedException {
        if (!humanBioD.equals(passpBioD)) {
            removeBiometricData();
            throw new BiometricVerificationFailedException("The passport data and the human data are not the same");
        }
        removeBiometricData();
    }

    private void removeBiometricData() {
        voter.biometricData = null;
        faceBiometric = null;
        fingerBiometric = null;
    }

    //Setter methods for injecting dependencies and additional methods
    public void setLocalService(LocalService localService) {
        this.localService = localService;
    }

    public void setElectoralOrganism(ElectoralOrganism electoralOrganism) {
        this.electoralOrganism = electoralOrganism;
    }

    public void setScrutiny(Scrutiny scrutiny) {
        this.scrutiny = scrutiny;
    }

    public void setPassportBiometricReader(PassportBiometricReader passportBiometricReader) {
        this.passportBiometricReader = passportBiometricReader;
    }

    public void setHumanBiometricScanner(HumanBiometricScanner humanBiometricScanner) {
        this.humanBiometricScanner = humanBiometricScanner;
    }

    //Class to represent the current voter
    protected static class Voter {
        State state;
        Nif nif;
        BiometricData biometricData;

        protected Voter() {
            nif = null;
            biometricData = null;
            state = State.disabled;
        }

        protected enum State {disabled, enabled}
    }
}


