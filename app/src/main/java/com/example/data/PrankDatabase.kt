package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "prank_history")
data class PrankRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerId: String,
    val ucAmount: Int = 600,
    val funnyBadge: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface PrankDao {
    @Query("SELECT * FROM prank_history ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<PrankRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: PrankRecord)

    @Query("DELETE FROM prank_history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM prank_history")
    suspend fun clearAll()
}

@Database(entities = [PrankRecord::class], version = 1, exportSchema = false)
abstract class PrankDatabase : RoomDatabase() {
    abstract fun prankDao(): PrankDao

    companion object {
        @Volatile
        private var INSTANCE: PrankDatabase? = null

        fun getDatabase(context: Context): PrankDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrankDatabase::class.java,
                    "free_uc_prank_db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class PrankRepository(private val prankDao: PrankDao) {
    val allRecords: Flow<List<PrankRecord>> = prankDao.getAllRecords()

    suspend fun insert(record: PrankRecord) = prankDao.insertRecord(record)

    suspend fun deleteById(id: Int) = prankDao.deleteById(id)

    suspend fun clearAll() = prankDao.clearAll()
}
