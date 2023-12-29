package evoting.biometricdataperipheral;

import data.SingleBiometricData;
import exceptions.evotingExceptions.HumanBiometricScanningException;

public class HumanBiometricScannerImpl implements HumanBiometricScanner {
    SingleBiometricData facialData;
    SingleBiometricData fingerData;
    boolean validFinger, validFace;

    public HumanBiometricScannerImpl() {
        validFace = true;
        validFinger = false;
        facialData = new SingleBiometricData("face".getBytes());
        fingerData = new SingleBiometricData("finger".getBytes());
    }

    @Override
    public SingleBiometricData scanFaceBiometrics() throws HumanBiometricScanningException {
        if (!validFace) throw new HumanBiometricScanningException("Error on the Biometric Data");
        return facialData;
    }

    @Override
    public SingleBiometricData scanFingerprintBiometrics() throws HumanBiometricScanningException {
        if (!validFinger) throw new HumanBiometricScanningException("Error on the Biometric Data");
        return fingerData;
    }

    //methods to test purposes
    public void setValidFace(boolean validBiometric) {
        this.validFinger = validBiometric;
    }

    public void setValidFinger(boolean validBiometric) {
        this.validFace = validBiometric;
    }
    public void setFinger(SingleBiometricData fingerData) {
        this.fingerData = fingerData;
    }
    public void setFace(SingleBiometricData facialData) {
        this.facialData = facialData;
    }
}
