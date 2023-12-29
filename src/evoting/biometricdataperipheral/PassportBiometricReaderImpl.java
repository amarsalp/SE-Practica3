package evoting.biometricdataperipheral;

import data.BiometricData;
import data.Nif;
import data.SingleBiometricData;
import exceptions.dataExceptions.BadFormatException;
import exceptions.evotingExceptions.NotValidPassportException;
import exceptions.evotingExceptions.PassportBiometricReadingException;

import java.util.Date;

public class PassportBiometricReaderImpl implements PassportBiometricReader {

    boolean validPassport, validBiometric;
    Nif nif;
    SingleBiometricData faceBiometric;
    SingleBiometricData dactilarBiometric;
    BiometricData biometricData;

    public PassportBiometricReaderImpl() throws BadFormatException {
        validPassport = true;
        validBiometric = true;
        faceBiometric = new SingleBiometricData("face".getBytes());
        dactilarBiometric = new SingleBiometricData("fingers".getBytes());
        biometricData = new BiometricData(dactilarBiometric, faceBiometric);
        this.nif = new Nif("12345678A");
    }

    @Override
    public void validatePassport() throws NotValidPassportException {
        if (!validPassport) throw new NotValidPassportException("The passport is not valid");
    }

    @Override
    public Nif getNifWithOCR() {
        return nif;
    }

    @Override
    public BiometricData getPassportBiometricData() throws PassportBiometricReadingException {
        if (!validBiometric)
            throw new PassportBiometricReadingException("Error during the reading of the biometric data");
        return biometricData;
    }

    //methods to test purposes
    public void setValidPassport(boolean isValidPassaport) {
        this.validPassport = isValidPassaport;
    }

    public void setValidBiometric(boolean validBiometric) {
        this.validBiometric = validBiometric;
    }
    public void setFinger(SingleBiometricData fingerData) {
        this.dactilarBiometric = fingerData;
    }
    public void setFace(SingleBiometricData facialData) {
        this.faceBiometric = facialData;
    }
}
