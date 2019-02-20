package com.daya.android.breakpad

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

// http://pluu.github.io/blog/android/jni/2015/06/12/android-google-breakpad-javacallback/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButton.setOnClickListener {
            initialize()
        }

        crashButton.setOnClickListener {
            crash()
        }
    }

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun initialize()
    external fun crash()
}
