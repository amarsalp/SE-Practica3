package evoting;

import data.VotingOption;
import evoting.VotingKiosk;
import exceptions.dataExceptions.BadFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VotingKioskTest  {

    VotingKiosk votingKiosk;
    List<VotingOption> validParties;

    public void setVotingKiosk(){votingKiosk = new VotingKiosk(validParties);}

    @Test
    @DisplayName("setDocument return char")
    void test_setDocument(){
        char opt = 'c';
        //COmo hacer test de una funcion que devuelve void
        //assertEquals('c',votingKiosk.setDocument(opt));
    }
}
