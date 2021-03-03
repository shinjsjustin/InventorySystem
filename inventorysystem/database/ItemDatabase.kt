package com.bignerdranch.android.inventorysystem.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.inventorysystem.Item

@Database(entities = [Item::class], version=1)
@TypeConverters(ItemTypeConverters::class)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

}