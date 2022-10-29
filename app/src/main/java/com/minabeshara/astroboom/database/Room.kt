package com.minabeshara.astroboom.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.minabeshara.astroboom.model.Asteroid
import com.minabeshara.astroboom.model.PictureOfDay

@Dao
interface AsteroidDao {

    @Query("select * from asteroid")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<Asteroid>)

    @Query("select * from pictureofday LIMIT 1")
    fun getPictureOfDay(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(pic: PictureOfDay)

    @Query("DELETE from asteroid")
    fun deleteAllAsteroids()

}

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidsDatabase::class.java, "asteroids"
            ).build()
        }
    }
    return INSTANCE
}
