// IEqualizerService.aidl
package com.senai.vehicleequalizerapp;

// Declare any non-default types here with import statements

interface IEqualizerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

    void setEqualizerEnabled(boolean enabled);
    void setBassLevel(int level);
    void setMidLevel(int level);
    void setTrebleLevel(int level);
    // Você pode adicionar métodos para obter os valores atuais, se necessário
    // boolean isEqualizerEnabled();
    // int getBassLevel();
}