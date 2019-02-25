package com.daya.android.breakpad

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

// http://pluu.github.io/blog/android/jni/2015/06/12/android-google-breakpad-javacallback/

class MainActivity : AppCompatActivity() {
    private val crashDumpPath: String
        get() = Environment.getExternalStorageDirectory().path + "/daya/dump"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dir = File(crashDumpPath)
        dir.mkdirs()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        } else {
            initializeCrashReporter()
        }

        crashButton.setOnClickListener {
            crash()
        }

        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("breakpad-android", "Uncaught exception.")
            Thread.sleep(5000)
            // Try everything to make sure this process goes away.
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(10);
            defaultUncaughtExceptionHandler.uncaughtException(thread, throwable)
        }
    }

    private fun initializeCrashReporter() {
        val dir = File(crashDumpPath)
        dir.mkdirs()
        initialize(crashDumpPath)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCrashReporter()
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun initialize(path: String)
    external fun crash()
}
