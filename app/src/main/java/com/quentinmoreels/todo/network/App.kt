package com.quentinmoreels.todo.network

import android.app.Application
import com.quentinmoreels.todo.network.Api.setUpContext

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        setUpContext(this)
    }
}