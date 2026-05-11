package com.rmmc.studenttaskhub

import android.app.Application
import com.rmmc.studenttaskhub.ui.common.AppContainer

class StudentTaskHubApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
