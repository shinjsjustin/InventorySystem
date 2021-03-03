package com.bignerdranch.android.inventorysystem

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import java.lang.Integer.parseInt
import java.util.*
import androidx.lifecycle.Observer

private const val TAG = "ItemFragment"
private const val ARG_ITEM_ID = "item_id"
class ItemFragment : Fragment() {

    interface Callbacks{
        fun onBackSelected()
    }
    private var callbacks: Callbacks? = null

    private lateinit var item: Item
    private lateinit var itemName: EditText
    private lateinit var itemQuantity: EditText
    private lateinit var itemTag: EditText
    private lateinit var addButton: Button
    private lateinit var subButton: Button
    private lateinit var doneButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private val itemDetailViewModel: ItemDetailViewModel by lazy {
        ViewModelProviders.of(this).get(ItemDetailViewModel:: class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        item = Item()
        val itemId: UUID = arguments?.getSerializable(ARG_ITEM_ID) as UUID
        itemDetailViewModel.loadItem(itemId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        val view = inflater.inflate(R.layout.fragment_item, container, false)

        itemName = view.findViewById(R.id.item_name)
        itemQuantity = view.findViewById(R.id.item_quantity)
        itemTag = view.findViewById(R.id.item_tag)
        addButton = view.findViewById(R.id.add_button)
        subButton = view.findViewById(R.id.subtract_button)
        doneButton = view.findViewById(R.id.done_button)

        addButton.setOnClickListener{
            item.itemQuantity += 1
            updateUI()
        }

        subButton.setOnClickListener{
            item.itemQuantity -= 1
            updateUI()
        }

        doneButton.setOnClickListener{
            callbacks?.onBackSelected()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemDetailViewModel.itemLiveData.observe(
            viewLifecycleOwner,
            Observer {item ->
                item?.let{
                    this.item = item
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.itemName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        val tagWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.tag = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        val quantityWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.itemQuantity = Integer.parseInt(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        itemQuantity.addTextChangedListener(quantityWatcher)
        itemName.addTextChangedListener(titleWatcher)
        itemTag.addTextChangedListener(tagWatcher)
    }

    override fun onStop(){
        super.onStop()
        itemDetailViewModel.saveItem(item)
    }

    private fun updateUI(){
        itemName.setText(item.itemName)
        itemQuantity.setText(item.itemQuantity.toString())
        itemTag.setText(item.tag)
    }

    companion object {
        fun newInstance(itemId: UUID): ItemFragment {
            val args = Bundle().apply {
                putSerializable(ARG_ITEM_ID, itemId)
            }
            return ItemFragment().apply{
                arguments = args
            }
        }
    }
}