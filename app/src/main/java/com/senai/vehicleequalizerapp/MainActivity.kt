package com.senai.vehicleequalizerapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vehicleequalizerapp.VehicleAudioSystem
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var equalizerSwitch: SwitchMaterial
    private lateinit var bassSeekBar: SeekBar
    private lateinit var midSeekBar: SeekBar
    private lateinit var trebleSeekBar: SeekBar

    private val TAG = "VehicleEqualizerApp"

    // Instância do nosso sistema de áudio simulado para o veículo
    //private val vehicleAudioSystem = VehicleAudioSystem()

    // Instância da interface AIDL para se comunicar com o serviço
    private var equalizerService: IEqualizerService? = null

    // Objeto ServiceConnection para gerenciar a conexão com o serviço
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        // Chamado quando a conexão com o serviço é estabelecida
            equalizerService = IEqualizerService.Stub.asInterface(service)
            Log.i(TAG, "Conectado ao EqualizerService.")

            // Opcional: Enviar o estado inicial do equalizador para o serviço após a conexão
            equalizerService?.setEqualizerEnabled(equalizerSwitch.isChecked)
            equalizerService?.setBassLevel(bassSeekBar.progress)
            equalizerService?.setMidLevel(midSeekBar.progress)
            equalizerService?.setTrebleLevel(trebleSeekBar.progress)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        // Chamado quando a conexão com o serviço é perdida inesperadamente
            equalizerService = null
            Log.w(TAG, "Desconectado do EqualizerService.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajuste do padding automático
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa os componentes
        equalizerSwitch = findViewById(R.id.equalizerSwitch)
        bassSeekBar = findViewById(R.id.bassSeekBar)
        midSeekBar = findViewById(R.id.midSeekBar)
        trebleSeekBar = findViewById(R.id.trebleSeekBar)

        // Estado inicial
        setEqualizerControlsEnabled(equalizerSwitch.isChecked)

        // Listener do switch
        equalizerSwitch.setOnCheckedChangeListener { _, isChecked ->
            setEqualizerControlsEnabled(isChecked)
            //vehicleAudioSystem.equalizerEnabled = isChecked // Atualiza o stado no sistema de áudio simulado
            //vehicleAudioSystem.applyEqualizerSettings() // Aplica as configurações
            // Chama o método AIDL no serviço
            equalizerService?.setEqualizerEnabled(isChecked)
            Log.d(TAG, "Equalizador ${if (isChecked) "ativado" else "desativado"}")
        }

        // Listener genérico para os SeekBars
        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    when (seekBar?.id) {
                        R.id.bassSeekBar -> Log.d(TAG, "Graves: $progress")
                        R.id.midSeekBar -> Log.d(TAG, "Médios: $progress")
                        R.id.trebleSeekBar -> Log.d(TAG, "Agudos: $progress")
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                when (seekBar?.id) {
                    //R.id.bassSeekBar -> Log.i(TAG, "Graves final: ${seekBar.progress}")
                    //R.id.midSeekBar -> Log.i(TAG, "Médios final: ${seekBar.progress}")
                    //R.id.trebleSeekBar -> Log.i(TAG, "Agudos final: ${seekBar.progress}")

                    //R.id.bassSeekBar -> vehicleAudioSystem.bassLevel = seekBar.progress
                    //R.id.midSeekBar -> vehicleAudioSystem.midLevel = seekBar.progress
                    //R.id.trebleSeekBar -> vehicleAudioSystem.trebleLevel = seekBar.progress

                    R.id.bassSeekBar -> equalizerService?.setBassLevel(seekBar.progress)
                    R.id.midSeekBar -> equalizerService?.setMidLevel(seekBar.progress)
                    R.id.trebleSeekBar -> equalizerService?.setTrebleLevel(seekBar.progress)
                }

                //vehicleAudioSystem.applyEqualizerSettings() // Aplica as configurações ao soltar o slider
            }
        }

        // Aplica o listener
        bassSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        midSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        trebleSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)

        // Opcional: Simular uma atualização de volume vinda da rede CAN ao iniciar a atividade
        //vehicleAudioSystem.setMasterVolumeFromCAN(75) // Exemplo: volume 75%
    }

    override fun onStart() {
        super.onStart()
        // Inicia o processo de vinculação ao serviço quando a atividade se torna visível
        val intent = Intent(this, EqualizerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Desvincula do serviço quando a atividade não está mais visível
        if (equalizerService != null) {
            unbindService(serviceConnection)
            equalizerService = null
            Log.i(TAG, "Desvinculado do EqualizerService.")
        }
    }

    private fun setEqualizerControlsEnabled(enabled: Boolean) {
        bassSeekBar.isEnabled = enabled
        midSeekBar.isEnabled = enabled
        trebleSeekBar.isEnabled = enabled
    }

}
