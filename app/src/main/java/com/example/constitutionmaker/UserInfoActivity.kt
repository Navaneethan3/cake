package com.example.constitutionmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.constitutionmaker.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val username = binding.editUsername.text.toString().trim()
            val registerNumber = binding.editRegisterNumber.text.toString().trim()

            if (username.isEmpty() || registerNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ConstitutionTypeActivity::class.java).apply {
                    putExtra("username", username)
                    putExtra("register_number", registerNumber)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}