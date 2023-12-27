package evoting;

import data.*;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.serviceExceptions.InvalidAccountException;
import exceptions.serviceExceptions.NotEnabledException;
import services.ElectoralOrganism;
import services.LocalService;
import exceptions.evotingExceptions.ProceduralException;
import services.ScrutinyImpl;

import java.net.ConnectException;


/**
 * Internal classes involved in the exercise of the vote
 */
public class VotingKiosk {

    // Class members
    char opt;
    VotingOption selectedVO;
    Boolean activeSession;
    LocalService localService;
    ElectoralOrganism electoralOrganism;

    // Constructor
    public VotingKiosk() {
        activeSession = false;
        selectedVO = null;
    }

    // Input events
    public void initVoting() {
        activeSession = true;
    }

    public void setDocument(char opt) {
        this.opt = opt;
    }

    public void enterAccount(String login, Password pssw) throws InvalidAccountException {
        localService.verifyAccount(login, pssw);
        System.out.println("Account is valid");
    }

    public void confirmIdentif(char conf) throws InvalidDniException {
        //The letter 'c' is used to say it's a confirmations from the support staff .
        if (conf != 'c') throw new InvalidDniException("Not valid identification");
    }

    public void enterNif(Nif nif) throws NotEnabledException, ConnectException, BadFormatException {
        Nif nifToCheck = new Nif(nif.getNif());
        //Check NIF
        electoralOrganism.canVote(nifToCheck);
        System.out.println("The nif is able to vote");
    }

    public void initOptionsNavigation() {
        //TODO
    }

    public void consultVotingOption(VotingOption vopt) {
        this.selectedVO = new VotingOption(vopt.getParty());
    }

    public void vote() throws ProceduralException {
        if (selectedVO == null || !activeSession)
            throw new ProceduralException("The voter has to select a party or init a session");
        System.out.println("Party selected, confirmation of the selection needed");
    }

    public void confirmVotingOption(char conf) throws ConnectException, ProceduralException {
        if (selectedVO == null || !activeSession)
            throw new ProceduralException("The voter has to select a party or init a session");
        //The letter 'c' is used to say it's a confirmations from the voter
        if (conf == 'c') {
            System.out.println("Processing your vote");

        } else {
            //TODO
        }
    }

    // Internal operation, not required
    private void finalizeSession() {
    }

    //Setter methods for injecting dependencies and additional methods
    public void setLocalService(LocalService localService) {
        this.localService = localService;
    }

    public void setElectoralOrganism(ElectoralOrganism localService) {
        this.electoralOrganism = localService;
    }
}


