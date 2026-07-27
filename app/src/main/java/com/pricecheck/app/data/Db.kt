package com.pricecheck.app.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity
data class SavedItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val store: String,
    val totalPrice: Double,
    val size: Double,
    val qty: Double,
    val unitPrice: Double, // per 100 units (g/ml)
    val timestamp: Long
)

@Dao
interface SavedItemDao {
    @Insert
    suspend fun insert(item: SavedItem)

    @Delete
    suspend fun delete(item: SavedItem)

    @Query("SELECT * FROM SavedItem")
    fun getAll(): Flow<List<SavedItem>>
}

@Database(entities = [SavedItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedItemDao(): SavedItemDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pricecheck.db"
                ).build().also { instance = it }
            }
    }
}
