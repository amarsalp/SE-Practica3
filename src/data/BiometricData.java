package data;

import java.util.Arrays;

public class BiometricData {
    private final SingleBiometricData facialKey;
    private final SingleBiometricData dactilarKey;

    public BiometricData(SingleBiometricData dactilarKey, SingleBiometricData facialKey) {
        this.dactilarKey = dactilarKey;
        this.facialKey = facialKey;
    }

    public SingleBiometricData getFacialKey() {
        return facialKey;
    }

    public SingleBiometricData getDactilarKey() {
        return dactilarKey;
    }

    @Override
    public String toString() {
        return "BiometricData{" + "BioDat='" + facialKey.toString() + dactilarKey.toString() + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiometricData BioDat = (BiometricData) o;
        return facialKey.equals(BioDat.facialKey) && dactilarKey.equals(BioDat.dactilarKey);
    }

    @Override
    public int hashCode() {
        return facialKey.hashCode() + dactilarKey.hashCode();
    }
}
