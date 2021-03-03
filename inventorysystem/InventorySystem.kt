package com.bignerdranch.android.inventorysystem

import java.util.*

data class InventorySystem (val id: UUID = UUID.randomUUID(),
                            var systemName: String = "")