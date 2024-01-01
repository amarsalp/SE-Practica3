package evoting.biometricdataperipheral;

import data.BiometricData;
import data.Nif;
import data.SingleBiometricData;
import exceptions.dataExceptions.BadFormatException;
import exceptions.evotingExceptions.NotValidPassportException;
import exceptions.evotingExceptions.PassportBiometricReadingException;

import java.util.Date;

public class PassportBiometricReaderImpl implements PassportBiometricReader {

    private boolean validPassport, validBiometric;
    private Nif nif;
    private SingleBiometricData faceBiometric;
    private SingleBiometricData dactilarBiometric;
    private BiometricData biometricData;

    public PassportBiometricReaderImpl() throws BadFormatException {
        validPassport = true;
        validBiometric = true;
        faceBiometric = new SingleBiometricData("face".getBytes());
        dactilarBiometric = new SingleBiometricData("finger".getBytes());
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

    public void setNewVoter(SingleBiometricData fingerData, SingleBiometricData faceData, Nif nif) {
        biometricData = new BiometricData(fingerData, faceData);
        this.nif = nif;
    }

}
