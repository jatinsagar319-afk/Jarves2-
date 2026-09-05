package com.jarvis.assistant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class JarvisAccessibilityService : AccessibilityService() {

    companion object {
        var instance: JarvisAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {
        // Future AI screen understanding yahan add ki ja sakti hai.
    }

    override fun onInterrupt() {
    }

    override fun onDestroy() {

        instance = null

        super.onDestroy()
    }

    fun goBack(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_BACK
        )
    }

    fun goHome(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_HOME
        )
    }

    fun openRecents(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_RECENTS
        )
    }

    fun openNotifications(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_NOTIFICATIONS
        )
    }

    fun tap(
        x: Float,
        y: Float
    ) {

        val path = Path()

        path.moveTo(x, y)

        val stroke =
            GestureDescription.StrokeDescription(
                path,
                0,
                80
            )

        val gesture =
            GestureDescription.Builder()
                .addStroke(stroke)
                .build()

        dispatchGesture(
            gesture,
            null,
            null
        )
    }

    fun swipeUp() {

        swipe(
            500f,
            1500f,
            500f,
            400f
        )
    }

    fun swipeDown() {

        swipe(
            500f,
            400f,
            500f,
            1500f
        )
    }

    fun swipeLeft() {

        swipe(
            900f,
            900f,
            100f,
            900f
        )
    }

    fun swipeRight() {

        swipe(
            100f,
            900f,
            900f,
            900f
        )
    }

    private fun swipe(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ) {

        val path = Path()

        path.moveTo(x1, y1)

        path.lineTo(x2, y2)

        val stroke =
            GestureDescription.StrokeDescription(
                path,
                0,
                400
            )

        val gesture =
            GestureDescription.Builder()
                .addStroke(stroke)
                .build()

        dispatchGesture(
            gesture,
            null,
            null
        )
    }
}
