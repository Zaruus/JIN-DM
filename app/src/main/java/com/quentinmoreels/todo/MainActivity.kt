package com.quentinmoreels.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quentinmoreels.todo.authentication.SHARED_PREF_TOKEN_KEY

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}