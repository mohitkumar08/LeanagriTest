package com.leanagritest

import android.app.Application
import com.leanagritest.core.AppObjectController

class LeanAgriApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppObjectController.initLibrary(this)
    }
}