package evoting.biometricdataperipheral;

import data.BiometricData;
import data.Nif;
import exceptions.evotingExceptions.NotValidPassportException;
import exceptions.evotingExceptions.PassportBiometricReadingException;

public interface PassportBiometricReader {// Perip. for reading passport biometrics
    void validatePassport () throws NotValidPassportException;
    Nif getNifWithOCR ();
    BiometricData getPassportBiometricData ()
            throws PassportBiometricReadingException;
}
