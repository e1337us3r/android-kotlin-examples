package com.apolets.audioapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : Activity() {

    var recRunning: Boolean = false
    var isPlaying: Boolean = false
    lateinit var recFile: File
    val recFileName = "testkjs.raw"

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        btnRecord.setOnClickListener {
            recRunning = true
            startRecording()
            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show()
        }
        btnStop.setOnClickListener {
            recRunning = false
            Toast.makeText(this, "Recording stopped.", Toast.LENGTH_SHORT).show()
        }

        btnPlay.setOnClickListener {
            playRecording()
            Toast.makeText(this, "Playing recording...", Toast.LENGTH_SHORT).show()
        }
    }

    fun startRecording() {

        Thread {

            val storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            try {
                recFile = File(storageDir.toString() + "/" + recFileName)
                Log.d("MYAPP","recFile initialized")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            try {
                val outputStream = FileOutputStream(recFile)
                val bufferedOutputStream = BufferedOutputStream(outputStream)
                val dataOutputStream = DataOutputStream(bufferedOutputStream)

                val minBufferSize = AudioRecord.getMinBufferSize(44100,
                        AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT)
                val aFormat = AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                val recorder = AudioRecord.Builder()
                        .setAudioSource(MediaRecorder.AudioSource.MIC)
                        .setAudioFormat(aFormat)
                        .setBufferSizeInBytes(minBufferSize)
                        .build()
                val audioData = ByteArray(minBufferSize)
                recorder.startRecording()

                Log.d("MYAPP","Recording starting")
                while (recRunning) {
                    val numofBytes = recorder.read(audioData, 0, minBufferSize)
                    if (numofBytes > 0) {
                        dataOutputStream.write(audioData)
                    }
                }
                recorder.stop()
                dataOutputStream.close()
                Log.d("MYAPP","Recording ended")
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }.start()

    }


    fun playRecording() {
        val inputStream = recFile.inputStream()

        val myRunnable = PlayAudio(inputStream)
        Thread(myRunnable).start()

    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }
}
