package com.example.cringecoindisplayer

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.time.LocalDateTime

import java.util.Date

data class AssetData(
    var symbol: String,
    var urlBadge: String,
    var lastUpdated: LocalDateTime,
    var lastPrice: Float
    )