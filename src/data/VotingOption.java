package data;

import exceptions.dataExceptions.BadFormatException;

import java.util.Objects;

/**
 * Essential data classes
 */
final public class VotingOption {
    // The tax identification number in the Spanish state.
    private final String party;

    public VotingOption(String option) {
        if (option == null) throw new NullPointerException("Voting option null");
        this.party = option;
    }

    public String getParty() {
        return this.party;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingOption vO = (VotingOption) o;
        return party.equals(vO.party);
    }

    @Override
    public int hashCode() {
        return this.party.hashCode();
    }

    @Override
    public String toString() {
        return "Vote option {" + "party='" + this.party + '\'' + '}';
    }
}
