package com.bignerdranch.android.inventorysystem

import android.app.Application

class InventoryApplication : Application() {
    override fun onCreate(){
        super.onCreate()
        ItemRepository.initialize(this)
    }
}