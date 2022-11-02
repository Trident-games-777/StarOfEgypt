package jp.konami.duell.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {

    @Query("SELECT COUNT(*) FROM model")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DatabaseModel)

    @Query("SELECT admin FROM model LIMIT 1")
    suspend fun getAdmin(): Boolean

    @Query("SELECT user FROM model LIMIT 1")
    suspend fun getUser(): String

    @Query("SELECT * FROM model LIMIT 1")
    suspend fun getEntity(): DatabaseModel
}