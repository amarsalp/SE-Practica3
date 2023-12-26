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

    // TODO: The class members
    char opt;
    VotingOption selectedVO, toConfirmVO;
    Boolean activeEVoting;

    LocalService localService;
    ElectoralOrganism electoralOrganism;

    // TODO: The constructor/s
    public VotingKiosk() {
        activeEVoting = false;
        selectedVO = null;
        toConfirmVO = null;
    }

    public void setLocalService(LocalService localService) {
        this.localService = localService;
    }

    public void setElectoralOrganism(ElectoralOrganism localService) {
        this.electoralOrganism = localService;
    }

    // Input events
    public void initVoting() {
        activeEVoting = true;
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

    public void vote() throws  ProceduralException{
        if (selectedVO == null || !activeEVoting) throw new ProceduralException("The voter has to select a party or init a session");
        toConfirmVO = selectedVO;
        System.out.println("Selected party, confirmation of the selection needed");
    }

    public void confirmVotingOption(char conf) throws ConnectException, ProceduralException {
        if (toConfirmVO == null || !activeEVoting) throw new ProceduralException("The voter has to select a party or init a session");
        //The letter 'c' is used to say it's a confirmations from the voter
        if (conf == 'c'){
            //TODO
        }else{
            //TODO
        }
    }

    // Internal operation, not required
    private void finalizeSession() {
        //TODO
    }
    //TODO: Setter methods for injecting dependences and additional methods
}

