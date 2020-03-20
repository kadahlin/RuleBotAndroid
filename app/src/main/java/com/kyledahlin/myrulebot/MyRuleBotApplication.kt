package com.kyledahlin.myrulebot

import android.app.Activity
import android.app.Application

class MyRuleBotApplication : Application() {
    internal val _coreComponent by lazy {
        DaggerCoreComponent.builder().applicationContext(applicationContext).build()
    }

}

fun Activity.coreComponent(): CoreComponent = (application as MyRuleBotApplication)._coreComponent
