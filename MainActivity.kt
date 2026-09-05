package com.jarvis.assistant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale

class MainActivity :
    Activity(),
    TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private lateinit var commandEngine:
        CommandEngine

    private lateinit var status:
        TextView

    private lateinit var log:
        TextView

    private var speechRecognizer:
        SpeechRecognizer? = null

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        commandEngine =
            CommandEngine(this)

        requestMicrophone()

        tts =
            TextToSpeech(
                this,
                this
            )

        createInterface()
    }

    private fun requestMicrophone() {

        if (
            android.os.Build.VERSION.SDK_INT >= 23
        ) {

            if (
                checkSelfPermission(
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO
                    ),
                    100
                )
            }
        }
    }

    private fun createInterface() {

        val root =
            LinearLayout(this)

        root.orientation =
            LinearLayout.VERTICAL

        root.gravity =
            Gravity.CENTER_HORIZONTAL

        root.setPadding(
            25,
            45,
            25,
            25
        )

        root.setBackgroundColor(
            Color.rgb(
                7,
                16,
                24
            )
        )

        val title =
            TextView(this)

        title.text =
            "J A R V I S"

        title.textSize =
            30f

        title.gravity =
            Gravity.CENTER

        title.setTextColor(
            Color.rgb(
                98,
                217,
                255
            )
        )

        root.addView(
            title,
            LinearLayout.LayoutParams(
                -1,
                80
            )
        )

        status =
            TextView(this)

        status.text =
            "SYSTEM READY"

        status.textSize =
            17f

        status.gravity =
            Gravity.CENTER

        status.setTextColor(
            Color.WHITE
        )

        root.addView(
            status,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        val micButton =
            Button(this)

        micButton.text =
            "🎙  TALK TO JARVIS"

        micButton.textSize =
            18f

        micButton.setOnClickListener {

            startListening()
        }

        root.addView(
            micButton,
            LinearLayout.LayoutParams(
                -1,
                70
            )
        )

        val controlButton =
            Button(this)

        controlButton.text =
            "ENABLE PHONE CONTROL"

        controlButton.setOnClickListener {

            startActivity(
                Intent(
                    Settings.ACTION_ACCESSIBILITY_SETTINGS
                )
            )
        }

        root.addView(
            controlButton,
            LinearLayout.LayoutParams(
                -1,
                65
            )
        )

        val helpButton =
            Button(this)

        helpButton.text =
            "COMMANDS"

        helpButton.setOnClickListener {

            addLog(
                """
                Try:

                Open YouTube
                Open WhatsApp
                Open Chrome
                Open Instagram
                Open Maps
                Open Camera
                Settings
                Home
                Back
                Recent Apps
                Notifications
                Swipe Up
                Swipe Down
                Search weather today
                Call 9876543210
                Alarm
                """.trimIndent()
            )
        }

        root.addView(
            helpButton,
            LinearLayout.LayoutParams(
                -1,
                60
            )
        )

        log =
            TextView(this)

        log.text =
            "JARVIS LOG\n\n"

        log.textSize =
            15f

        log.setTextColor(
            Color.LTGRAY
        )

        val scroll =
            ScrollView(this)

        scroll.addView(log)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                -1,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun startListening() {

        if (
            !SpeechRecognizer
                .isRecognitionAvailable(this)
        ) {

            speak(
                "Speech recognition available नहीं है."
            )

            return
        }

        speechRecognizer?.destroy()

        speechRecognizer =
            SpeechRecognizer
                .createSpeechRecognizer(
                    this
                )

        speechRecognizer!!
            .setRecognitionListener(

                object :
                    RecognitionListener {

                    override fun onReadyForSpeech(
                        params: Bundle?
                    ) {

                        status.text =
                            "LISTENING..."
                    }

                    override fun onBeginningOfSpeech() {
                    }

                    override fun onRmsChanged(
                        rmsdB: Float
                    ) {
                    }

                    override fun onBufferReceived(
                        buffer: ByteArray?
                    ) {
                    }

                    override fun onEndOfSpeech() {

                        status.text =
                            "PROCESSING..."
                    }

                    override fun onError(
                        error: Int
                    ) {

                        status.text =
                            "SYSTEM READY"

                        speak(
                            "Command सुनाई नहीं दी."
                        )
                    }

                    override fun onResults(
                        results: Bundle?
                    ) {

                        val matches =
                            results?.getStringArrayList(
                                SpeechRecognizer
                                    .RESULTS_RECOGNITION
                            )

                        val command =
                            matches
                                ?.firstOrNull()
                                ?: ""

                        status.text =
                            "SYSTEM READY"

                        addLog(
                            "YOU: $command"
                        )

                        val reply =
                            commandEngine
                                .execute(
                                    command
                                )

                        addLog(
                            "JARVIS: $reply"
                        )

                        speak(
                            reply
                        )
                    }

                    override fun onPartialResults(
                        partialResults: Bundle?
                    ) {
                    }

                    override fun onEvent(
                        eventType: Int,
                        params: Bundle?
                    ) {
                    }
                }
            )

        val intent =
            Intent(
                RecognizerIntent
                    .ACTION_RECOGNIZE_SPEECH
            )

        intent.putExtra(
            RecognizerIntent
                .EXTRA_LANGUAGE_MODEL,
            RecognizerIntent
                .LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_MAX_RESULTS,
            5
        )

        speechRecognizer!!
            .startListening(intent)
    }

    private fun speak(
        text: String
    ) {

        if (
            ::tts.isInitialized
        ) {

            tts.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "jarvis"
            )
        }
    }

    private fun addLog(
        text: String
    ) {

        log.append(
            "$text\n\n"
        )
    }

    override fun onInit(
        statusCode: Int
    ) {

        if (
            statusCode ==
            TextToSpeech.SUCCESS
        ) {

            tts.language =
                Locale(
                    "hi",
                    "IN"
                )
        }
    }

    override fun onDestroy() {

        speechRecognizer?.destroy()

        if (
            ::tts.isInitialized
        ) {

            tts.stop()

            tts.shutdown()
        }

        super.onDestroy()
    }
    }
