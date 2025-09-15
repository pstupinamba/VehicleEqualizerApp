package com.example.vehicleequalizerapp

import android.util.Log

class VehicleAudioSystem {
    private val TAG = "VehicleAudioSystem"
    // Propriedades que simulam o estado do equalizador no sistema de áudio do veículo

    var equalizerEnabled: Boolean = false
    set(value) {
        field = value
        Log.d( TAG, "[Sistema de Áudio] Equalizador ${
                    if (value)
                        "ATIVADO" else "DESATIVADO"
                }" )

        // Em um cenário real, aqui você enviaria este estado para a HAL de áudio
    }

    var bassLevel: Int = 50

    set(value) {
        field = value
        Log.d(TAG, "[Sistema de Áudio] Nível de Graves: $value")
        // Em um cenário real, aqui você enviaria este nível para a HAL de áudio
    }

    var midLevel: Int = 50

    set(value) {
        field = value
        Log.d(TAG, "[Sistema de Áudio] Nível de Médios: $value")
        // Em um cenário real, aqui você enviaria este nível para a HAL de áudio
    }

    var trebleLevel: Int = 50

    set(value) {
        field = value
        Log.d(TAG, "[Sistema de Áudio] Nível de Agudos: $value")
        // Em um cenário real, aqui você enviaria este nível para a HAL de áudio
    }

    /**
     * Simula a aplicação de todas as configurações do equalizador no
    sistema de áudio.
     * Em um sistema real, isso poderia ser um método que agrupa várias
    chamadas à HAL.*/
    fun applyEqualizerSettings() {
        Log.i(TAG, "[Sistema de Áudio] Aplicando configurações do equalizador: Ativado=" +
                "$equalizerEnabled, Graves=$bassLevel, Médios=$midLevel, Agudos=$trebleLevel")
    }

    /**
     * Simula a recepção de um novo volume mestre do veículo via rede
    CAN.
     * @param volume O novo nível de volume (0-100).
     */
    fun setMasterVolumeFromCAN(volume: Int) {
        Log.d(TAG, "[Sistema de Áudio] Volume Mestre recebido via CAN: $volume")
        // Em um cenário real, isso atualizaria o volume do sistema de áudio
    }
}