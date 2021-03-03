package com.bignerdranch.android.inventorysystem

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Item(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var itemName: String = "",
                var itemQuantity: Int = 0,
                var tag: String = "")
