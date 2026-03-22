package com.example.constitutionmaker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstitutionDao {
    @Query("SELECT * FROM constitutions ORDER BY dateCreated DESC")
    fun getAllConstitutions(): Flow<List<ConstitutionEntity>>

    @Insert
    suspend fun insert(constitution: ConstitutionEntity)

    @Query("DELETE FROM constitutions WHERE id = :id")
    suspend fun deleteById(id: Long)
}