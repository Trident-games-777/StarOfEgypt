package jp.konami.duell.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model")
data class DatabaseModel(
    @PrimaryKey(autoGenerate = false)
    val _id: Int = Int.MAX_VALUE,
    val admin: Boolean,
    val user: String
)