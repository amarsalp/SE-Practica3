package data;

import exceptions.dataExceptions.BadFormatException;

/**
 * Essential data classes
 */
final public class Nif {
    // The tax identification number in the Spanish state.
    private final String nif;

    public Nif(String code) throws BadFormatException {
        if (code == null) throw new NullPointerException("Null NIF");

        if (!correctFormat(code)) throw new BadFormatException("Bad nif format");

        this.nif = code;
    }

    public String getNif() {
        return nif;
    }

    private Boolean correctFormat(String code) {
        int nifLength = code.length();

        //Nif must contain only 9 characters
        if (nifLength != 9) return false;

        for (int i = 0; i < nifLength; i++) {
            if (i == nifLength - 1) {
                ///Last character must be a letter
                if (!Character.isLetter(code.charAt(nifLength - 1))) return false;
            } else {
                //The rest of the characters must be numbers
                if (!Character.isDigit(code.charAt(i))) return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nif niff = (Nif) o;
        return nif.equals(niff.nif);
    }

    @Override
    public int hashCode() {
        return nif.hashCode();
    }

    @Override
    public String toString() {
        return "Nif{" + "nif ciudadano='" + nif + '\'' + '}';
    }
}

