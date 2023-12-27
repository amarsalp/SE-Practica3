package services;

import data.VotingOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrutinyImpl implements Scrutiny{
    Map<VotingOption,Integer> scrutinyResults = new HashMap<>();

    public ScrutinyImpl(){

    }
    @Override
    public void initVoteCount(List<VotingOption> validParties) {

    }

    @Override
    public void scrutinize(VotingOption vopt) {

    }

    @Override
    public int getVotesFor(VotingOption vopt) {
        return 0;
    }

    @Override
    public int getTotal() {
        return 0;
    }

    @Override
    public int getNulls() {
        return 0;
    }

    @Override
    public int getBlanks() {
        return 0;
    }

    @Override
    public void getScrutinyResults() {

    }
}
