package services;

import data.VotingOption;

import java.util.List;

public class ScrutinyImpl implements Scrutiny{
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
