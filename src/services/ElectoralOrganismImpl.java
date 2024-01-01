package services;

import data.Nif;
import exceptions.dataExceptions.BadFormatException;
import exceptions.serviceExceptions.NotEnabledException;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;


public class ElectoralOrganismImpl implements ElectoralOrganism {
    private Map<Nif, Boolean> db;

    public ElectoralOrganismImpl() throws BadFormatException {
        db = new HashMap<>();
        //nif of a voter that didn't vote
        db.put(new Nif("12345678A"), true);
        db.put(new Nif("87654321B"), true);
    }

    @Override
    public void canVote(Nif nif) throws NotEnabledException, ConnectException {
        Boolean canVote = db.get(nif);
        //if the nif is not on the database (the voter is not in the correct vote center) or if the voter has already vote
        if (canVote == null || !canVote) throw new NotEnabledException("You can't vote");
    }

    @Override
    public void disableVoter(Nif nif) throws ConnectException {
        db.replace(nif, false);
    }
}
