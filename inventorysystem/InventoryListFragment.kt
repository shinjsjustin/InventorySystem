package com.bignerdranch.android.inventorysystem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class InventoryListFragment : Fragment() {

    interface Callbacks {
        fun onSystemSelected(systemId: UUID)
    }
    private var callbacks: Callbacks? = null

    private lateinit var inventoryRecyclerView: RecyclerView
    private var adapter: InventoryAdapter? = null

    private val inventoryListViewModel: InventoryListViewModel by lazy{
        ViewModelProviders.of(this).get(InventoryListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory_list,container,false)

        inventoryRecyclerView = view.findViewById(R.id.inventory_recycler_view) as RecyclerView
        inventoryRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI(){
        val systems = inventoryListViewModel.systems
        adapter = InventoryAdapter(systems)
        inventoryRecyclerView.adapter = adapter
    }

    private inner class InventoryHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var system: InventorySystem

        private val systemNameView: TextView = itemView.findViewById(R.id.system_name)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(system: InventorySystem){
            this.system = system
            systemNameView.text = this.system.systemName
        }

        override fun onClick(v: View){
            callbacks?.onSystemSelected(system.id)
        }
    }

    private inner class InventoryAdapter(var inventorySystems: List<InventorySystem>): RecyclerView.Adapter<InventoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryHolder {
            val view = layoutInflater.inflate(R.layout.inventory_system_icon, parent, false)
            return InventoryHolder(view)
        }
        override fun getItemCount() = inventorySystems.size

        override fun onBindViewHolder(holder: InventoryHolder, position: Int) {
            val system = inventorySystems[position]
            holder.bind(system)
        }
    }

    companion object {
        fun newInstance(): InventoryListFragment {
            return InventoryListFragment()
        }
    }
}