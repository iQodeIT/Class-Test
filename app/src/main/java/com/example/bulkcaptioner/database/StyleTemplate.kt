package com.example.bulkcaptioner.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bulkcaptioner.model.CaptionStyle

@Entity(tableName = "style_templates")
data class StyleTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @Embedded val style: CaptionStyle,
    val isDefault: Boolean = false
)
