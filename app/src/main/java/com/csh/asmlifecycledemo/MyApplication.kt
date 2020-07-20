package com.csh.asmlifecycledemo

import android.app.Application
import android.content.Context


class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
//        RePlugin.App.onCreate();
//        PluginHelper.getInstance()
//            .applicationOnCreate(baseContext) //must be after super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
//        PluginHelper.getInstance().applicationAttachBaseContext(base)
        super.attachBaseContext(base)
//        RePlugin.App.attachBaseContext(this);
    }


}