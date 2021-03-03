package com.bignerdranch.android.inventorysystem

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


private const val ARG_SYSTEM_ID = "system_id"
class ItemListFragment : Fragment() {

    interface Callbacks {
        fun onItemSelected(itemId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var itemRecyclerView: RecyclerView
    private var adapter: ItemAdapter? = ItemAdapter(emptyList())

    private val itemListViewModel: ItemListViewModel by lazy {
        ViewModelProviders.of(this).get(ItemListViewModel::class.java)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list,container,false)

        itemRecyclerView = view.findViewById(R.id.item_recycler_view) as RecyclerView
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        itemListViewModel.itemListLiveData.observe(
            viewLifecycleOwner,
            Observer { items ->
                items?.let{
                    updateUI(items)
                }
            }
        )
    }

    override fun onDetach(){
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_item_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_item -> {
                val item = Item()
                itemListViewModel.addItem(item)
                callbacks?.onItemSelected(item.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(items: List<Item>){
        adapter = ItemAdapter(items)
        itemRecyclerView.adapter = adapter
    }

    private inner class ItemHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        private lateinit var item: Item

        private val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.item_quantity)
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Item){
            this.item = item
            nameTextView.text = this.item.itemName
            quantityTextView.text = this.item.itemQuantity.toString()

            deleteButton.setOnClickListener{
                itemListViewModel.deleteItem(this.item)
            }
        }

        override fun onClick(v: View){
            callbacks?.onItemSelected(item.id)
        }

    }

    private inner class ItemAdapter(var items: List<Item>):RecyclerView.Adapter<ItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = layoutInflater.inflate(R.layout.list_item_item, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ItemHolder, position: Int){
            val item = items[position]
            holder.bind(item)
        }
    }

    companion object {
        fun newInstance(systemId: UUID): ItemListFragment {
            val args = Bundle().apply{
                putSerializable(ARG_SYSTEM_ID, systemId)
            }
            return ItemListFragment().apply{
                arguments = args
            }
        }
    }
}