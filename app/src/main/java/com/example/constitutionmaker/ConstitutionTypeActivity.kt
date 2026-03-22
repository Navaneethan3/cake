package com.example.constitutionmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.constitutionmaker.databinding.ActivityConstitutionTypeBinding

class ConstitutionTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConstitutionTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstitutionTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        val registerNumber = intent.getStringExtra("register_number")

        binding.cardFederal.setOnClickListener {
            val intent = Intent(this, ConstitutionFormActivity::class.java).apply {
                putExtra("username", username)
                putExtra("register_number", registerNumber)
                putExtra("constitution_type", "Federal")
            }
            startActivity(intent)
            finish()
        }

        // Central and State cards are disabled in XML via android:enabled="false" and alpha="0.5"
    }
}