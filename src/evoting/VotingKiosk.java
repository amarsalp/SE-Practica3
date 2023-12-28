package evoting;

import data.*;
import exceptions.dataExceptions.BadFormatException;
import exceptions.dataExceptions.InvalidDniException;
import exceptions.serviceExceptions.InvalidAccountException;
import exceptions.serviceExceptions.NotEnabledException;
import services.ElectoralOrganism;
import services.LocalService;
import exceptions.evotingExceptions.ProceduralException;
import services.Scrutiny;
import services.ScrutinyImpl;

import java.net.ConnectException;
import java.util.List;


/**
 * Internal classes involved in the exercise of the vote
 */
public class VotingKiosk {

    // Class members
    char opt;
    VotingOption selectedVO, toConfirmVO;
    Boolean activeSession;
    Nif currentVoter;
    List<VotingOption> validParties;
    LocalService localService;
    ElectoralOrganism electoralOrganism;
    Scrutiny scrutiny;

    // Constructor
    public VotingKiosk(List<VotingOption> validParties) {
        activeSession = false;
        selectedVO = null;
        toConfirmVO = null;
        currentVoter = null;
        opt = '1';
        this.validParties = validParties;
        this.validParties.add(new VotingOption("blankVote"));
    }

    // Input events
    public void initVoting() {
        activeSession = true;
    }

    public void setDocument(char opt) throws ProceduralException {
        if (!activeSession) throw new ProceduralException("The voter has to init an e-vote session");
        this.opt = opt;
    }

    public void enterAccount(String login, Password pssw) throws InvalidAccountException, ProceduralException {
        if (!activeSession) throw new ProceduralException("The voter has to init an e-vote session");
        localService.verifyAccount(login, pssw);
        System.out.println("Account is valid");
    }

    public void confirmIdentif(char conf) throws InvalidDniException {
        //The letter 'c' is used to say it's a confirmations from the support staff .
        if (conf != 'c') throw new InvalidDniException("Not valid identification");
    }

    public void enterNif(Nif nif) throws NotEnabledException, ConnectException, BadFormatException {
        //Check NIF
        electoralOrganism.canVote(new Nif(nif.getNif()));
        currentVoter = nif;
        System.out.println("The nif is able to vote");
    }

    public void initOptionsNavigation() {
        //todo
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
            electoralOrganism.disableVoter(currentVoter);
        } else {
            toConfirmVO = null;
            selectedVO = null;
        }
    }

    // Internal operation, not required
    private void finalizeSession() {
        activeSession = false;
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
}


