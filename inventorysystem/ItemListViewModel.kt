package com.bignerdranch.android.inventorysystem

import androidx.lifecycle.ViewModel

class ItemListViewModel : ViewModel() {
    private val itemRepository = ItemRepository.get()
    val itemListLiveData = itemRepository.getItems()

    fun addItem(item: Item){
        itemRepository.addItem(item)
    }

    fun deleteItem(item: Item){
        itemRepository.deleteItem(item)
    }
}