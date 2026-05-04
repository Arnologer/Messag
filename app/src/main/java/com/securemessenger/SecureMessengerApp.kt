package com.securemessenger

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class SecureMessengerApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        // Initialize security components
        initSecurity()
    }

    private fun initSecurity() {
        // Initialize Android Keystore and encryption components
        // This will be implemented in the security module
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
