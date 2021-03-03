package com.bignerdranch.android.inventorysystem

import androidx.lifecycle.ViewModel

class InventoryListViewModel: ViewModel() {
    val systems = mutableListOf<InventorySystem>()

    init{
        for(i in 0 until 10){
            val system = InventorySystem()
            system.systemName = "System #$i"
            systems += system
        }
    }
}