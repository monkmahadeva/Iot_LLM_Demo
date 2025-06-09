package com.vfx.rightbrainstudios.smartiotapplication.util

import android.content.Context
import android.util.Log
import android.widget.Toast

class GlobalExceptionHandler(
    private val context: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Log.e("GlobalExceptionHandler", "Uncaught exception: ${throwable.message}", throwable)

        // Optional: show toast or log
        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()

        // Optional: save error logs to file or remote server
        // ...

        // Optional: restart app or go to a fallback activity
        // val intent = Intent(context, CrashActivity::class.java)
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // context.startActivity(intent)

        // Always forward to the default handler (to crash properly or show system dialog)
        defaultHandler?.uncaughtException(thread, throwable)
    }
}