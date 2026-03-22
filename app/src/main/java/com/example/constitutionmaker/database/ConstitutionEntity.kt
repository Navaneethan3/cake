package com.example.constitutionmaker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constitutions")
data class ConstitutionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val jsonData: String,
    val dateCreated: Long
)
