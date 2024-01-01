package data;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SingleBiometricData {
    private final byte[] biometricData;

    public SingleBiometricData(byte[] biometricData) {
        this.biometricData = biometricData;
    }

    public byte[] getBiometricData() {
        return biometricData;
    }

    @Override
    public String toString() {
        return "Biometric data {" + "BioDat='" + new String(biometricData, StandardCharsets.UTF_8) + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleBiometricData BioDat = (SingleBiometricData) o;
        return Arrays.equals(this.biometricData, BioDat.biometricData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.biometricData);
    }
}
