package com.fwrt.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String,         // 本地或网络图片路径
    val purchaseDate: Long,       // 时间戳（ms）
    val expiryDate: Long,         // 时间戳（ms）
    val nutrition: String         // 简单字符串描述，例如"能量100kcal，蛋白质5g"
)