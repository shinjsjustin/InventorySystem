package com.bignerdranch.android.inventorysystem.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.inventorysystem.Item
import java.util.*

@Dao
interface ItemDao{
    @Query("SELECT * FROM item")
    fun getItems(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE id=(:id)")
    fun getItem(id: UUID): LiveData<Item?>

    @Update
    fun updateItem(item: Item)

    @Insert
    fun addItem(item: Item)

    @Delete
    fun deleteItem(item: Item)
}