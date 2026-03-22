package com.example.constitutionmaker

import android.app.Application
import android.webkit.WebView

class ConstitutionMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // This must be called as early as possible, before any WebView is initialized.
        // It allows capturing the entire content of a WebView, even parts not currently on screen.
        try {
            WebView.enableSlowWholeDocumentDraw()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
