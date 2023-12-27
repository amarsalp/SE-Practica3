package services;

import data.VotingOption;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScrutinyImpl implements Scrutiny{
    Map<VotingOption,Integer> scrutinyResults;
    List<VotingOption> validParties;
    int totalVotes;
    VotingOption nullVote;
    VotingOption blankVote;

    public ScrutinyImpl(List<VotingOption> validParties){
        scrutinyResults = new HashMap<>();
        totalVotes = 0;
        initVoteCount(validParties);
    }

    @Override
    public void initVoteCount(List<VotingOption> validParties) {
        this.validParties = validParties;
        Iterator<VotingOption> it = validParties.iterator();
        while (it.hasNext()){
            scrutinyResults.put(it.next(), 0);
        }
        scrutinyResults.put(nullVote,0);
        scrutinyResults.put(blankVote,0);
    }

    @Override
    public void scrutinize(VotingOption vopt) {
        scrutinyResults.replace(vopt, scrutinyResults.get(vopt)+1);
        totalVotes++;
    }

    @Override
    public int getVotesFor(VotingOption vopt) {
        return scrutinyResults.get(vopt);
    }

    @Override
    public int getTotal() {
        return totalVotes;
    }

    @Override
    public int getNulls() {
        return scrutinyResults.get(nullVote);
    }
    @Override
    public int getBlanks() {
        return scrutinyResults.get(blankVote);
    }

    @Override
    public void getScrutinyResults() {
        Iterator<VotingOption> it = validParties.iterator();
        while (it.hasNext()){
            System.out.println(it.next().getParty() + ": " + scrutinyResults.get(it.next()));
        }
    }
}
