package com.example.constitutionmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.constitutionmaker.databinding.ActivityMainBinding
import com.example.constitutionmaker.database.ConstitutionDatabase
import com.example.constitutionmaker.database.ConstitutionEntity
import com.example.constitutionmaker.models.ConstitutionData
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ConstitutionHistoryAdapter
    private val database by lazy { ConstitutionDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable full document draw for WebView before any WebView is created
        // This is required for PDF generation to capture the entire page content.
        try {
            WebView.enableSlowWholeDocumentDraw()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val termsAccepted = prefs.getBoolean("terms_accepted", false)
        
        if (!termsAccepted) {
            startActivity(Intent(this, TermsActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        loadConstitutions()
    }

    private fun setupRecyclerView() {
        adapter = ConstitutionHistoryAdapter { constitution ->
            val intent = Intent(this, PreviewActivity::class.java).apply {
                putExtra("constitution_id", constitution.id)
                putExtra("constitution_name", constitution.name)
                putExtra("constitution_json", constitution.jsonData)
            }
            startActivity(intent)
        }
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistory.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.fabCreate.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
    }

    private fun loadConstitutions() {
        lifecycleScope.launch {
            database.constitutionDao().getAllConstitutions().collect { constitutions ->
                adapter.submitList(constitutions)
                binding.textEmpty.visibility = if (constitutions.isEmpty()) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
            }
        }
    }
}

class ConstitutionHistoryAdapter(
    private val onClick: (ConstitutionEntity) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<ConstitutionHistoryAdapter.ViewHolder>() {

    private var items = listOf<ConstitutionEntity>()
    private val gson = Gson()

    fun submitList(list: List<ConstitutionEntity>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_constitution_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: android.view.View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(item: ConstitutionEntity) {
            val data = try {
                gson.fromJson(item.jsonData, ConstitutionData::class.java)
            } catch (e: Exception) {
                null
            }
            
            val displayName = if (data != null && data.registerNumber.isNotBlank()) {
                data.registerNumber
            } else {
                item.name
            }

            itemView.findViewById<android.widget.TextView>(R.id.textName).text = displayName
            itemView.findViewById<android.widget.TextView>(R.id.textDate).text =
                java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
                    .format(java.util.Date(item.dateCreated))
            itemView.setOnClickListener { onClick(item) }
        }
    }
}