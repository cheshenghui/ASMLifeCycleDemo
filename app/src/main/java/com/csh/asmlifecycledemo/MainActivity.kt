package com.csh.asmlifecycledemo

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn?.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        val apkPath = Environment.getExternalStorageDirectory().absolutePath+"/DynamicLoadHost/app-debug.apk"

        btn1?.setOnClickListener {
            // For installation, filepath set to path of the .apk file, and flags set to 0.
//            PluginManager.getInstance().installPackage(apkPath, 0)
        }
        btn2?.setOnClickListener {
            // For upgrade, filepath set to path of the .apk file, and flags set to PackageManagerCompat.INSTALL_REPLACE_EXISTING.
//            PluginManager.getInstance().installPackage(apkPath, PackageManagerCompat.INSTALL_REPLACE_EXISTING)
        }
        btn3?.setOnClickListener {
            // packageName is package name of the plugged app，flags = 0。
//            PluginManager.getInstance().deletePackage("com.oceanus.gcplm",0)
        }

    }

}