package com.jarvis.assistant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.Settings
import java.util.Locale

class CommandEngine(
    private val context: Context
) {

    fun execute(command: String): String {

        val raw = command.trim()

        val cmd =
            raw.lowercase(
                Locale.getDefault()
            )

        val service =
            JarvisAccessibilityService.instance

        // HOME
        if (
            cmd == "home" ||
            cmd.contains("home screen") ||
            cmd.contains("होम")
        ) {

            service?.goHome()

            return "Home screen खोल रहा हूँ."
        }

        // BACK
        if (
            cmd == "back" ||
            cmd.contains("go back") ||
            cmd.contains("वापस")
        ) {

            service?.goBack()

            return "Back."
        }

        // RECENTS
        if (
            cmd.contains("recent") ||
            cmd.contains("recent apps")
        ) {

            service?.openRecents()

            return "Recent apps खोल रहा हूँ."
        }

        // NOTIFICATIONS
        if (
            cmd.contains("notification") ||
            cmd.contains("notifications") ||
            cmd.contains("नोटिफिकेशन")
        ) {

            service?.openNotifications()

            return "Notifications खोल रहा हूँ."
        }

        // SWIPE UP
        if (
            cmd.contains("swipe up") ||
            cmd.contains("ऊपर swipe")
        ) {

            service?.swipeUp()

            return "Swipe up कर रहा हूँ."
        }

        // SWIPE DOWN
        if (
            cmd.contains("swipe down") ||
            cmd.contains("नीचे swipe")
        ) {

            service?.swipeDown()

            return "Swipe down कर रहा हूँ."
        }

        // LEFT
        if (
            cmd.contains("swipe left") ||
            cmd.contains("बाएं swipe")
        ) {

            service?.swipeLeft()

            return "Left swipe कर रहा हूँ."
        }

        // RIGHT
        if (
            cmd.contains("swipe right") ||
            cmd.contains("दाएं swipe")
        ) {

            service?.swipeRight()

            return "Right swipe कर रहा हूँ."
        }

        // YOUTUBE
        if (
            cmd.contains("youtube")
        ) {

            return openApp(
                "com.google.android.youtube",
                "YouTube"
            )
        }

        // WHATSAPP
        if (
            cmd.contains("whatsapp")
        ) {

            return openApp(
                "com.whatsapp",
                "WhatsApp"
            )
        }

        // CHROME
        if (
            cmd.contains("chrome") ||
            cmd.contains("browser")
        ) {

            return openApp(
                "com.android.chrome",
                "Chrome"
            )
        }

        // INSTAGRAM
        if (
            cmd.contains("instagram")
        ) {

            return openApp(
                "com.instagram.android",
                "Instagram"
            )
        }

        // MAPS
        if (
            cmd.contains("maps") ||
            cmd.contains("google map")
        ) {

            return openApp(
                "com.google.android.apps.maps",
                "Google Maps"
            )
        }

        // CAMERA
        if (
            cmd.contains("camera") ||
            cmd.contains("कैमरा")
        ) {

            val intent =
                Intent(
                    "android.media.action.IMAGE_CAPTURE"
                )

            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(intent)

            return "Camera खोल रहा हूँ."
        }

        // SETTINGS
        if (
            cmd.contains("settings") ||
            cmd.contains("सेटिंग")
        ) {

            val intent =
                Intent(
                    Settings.ACTION_SETTINGS
                )

            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(intent)

            return "Settings खोल रहा हूँ."
        }

        // SEARCH
        if (
            cmd.startsWith("search ") ||
            cmd.startsWith("सर्च ")
        ) {

            val query =
                raw.replace(
                    Regex(
                        "(?i)^(search|सर्च)\\s+"
                    ),
                    ""
                )

            val intent =
                Intent(
                    Intent.ACTION_WEB_SEARCH
                )

            intent.putExtra(
                "query",
                query
            )

            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(intent)

            return "Search कर रहा हूँ."
        }

        // CALL
        if (
            cmd.startsWith("call ") ||
            cmd.startsWith("कॉल ")
        ) {

            val number =
                raw.replace(
                    Regex(
                        "(?i)^(call|कॉल)\\s+"
                    ),
                    ""
                ).trim()

            if (
                number.matches(
                    Regex("[+0-9()\\- ]{6,}")
                )
            ) {

                val intent =
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse(
                            "tel:${Uri.encode(number)}"
                        )
                    )

                intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )

                context.startActivity(intent)

                return "Calling screen खोल रहा हूँ."
            }

            return "Number समझ नहीं आया."
        }

        // ALARM
        if (
            cmd.contains("alarm") ||
            cmd.contains("अलार्म")
        ) {

            val intent =
                Intent(
                    AlarmClock.ACTION_SET_ALARM
                )

            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(intent)

            return "Alarm screen खोल रहा हूँ."
        }

        return """
            Command समझ आया:
            "$raw"

            लेकिन इस command का action
            अभी configured नहीं है.
        """.trimIndent()
    }

    private fun openApp(
        packageName: String,
        name: String
    ): String {

        val launchIntent =
            context.packageManager
                .getLaunchIntentForPackage(
                    packageName
                )

        if (
            launchIntent == null
        ) {

            return "$name installed नहीं है."
        }

        launchIntent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(
            launchIntent
        )

        return "$name खोल रहा हूँ."
    }
}
