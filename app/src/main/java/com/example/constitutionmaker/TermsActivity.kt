package com.example.constitutionmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.constitutionmaker.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkAgree.setOnCheckedChangeListener { _, isChecked ->
            binding.btnAccept.isEnabled = isChecked
        }

        binding.btnAccept.setOnClickListener {
            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("terms_accepted", true).apply()
            
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}