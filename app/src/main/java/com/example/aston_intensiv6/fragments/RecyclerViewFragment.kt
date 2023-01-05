package com.example.aston_intensiv6.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_intensiv6.DataSetting
import com.example.aston_intensiv6.Decoration
import com.example.aston_intensiv6.R
import com.example.aston_intensiv6.data.Contact
import com.example.aston_intensiv6.diff_util.DiffUtilCallBack
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewFragment : Fragment(R.layout.fragment_recycler) {

    private val picasso = Picasso.get()
    private var dataSource: DataSetting? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var curLayoutManager: LinearLayoutManager
    private var currentHolderPosition = -1
    private var tempList: MutableList<Contact> = ArrayList()

    companion object {
        const val REQUEST_KEY = "123R"
        fun newRecyclerFragment() = RecyclerViewFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataSetting) {
            dataSource = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_recycler, container, false)
        recyclerView = v.findViewById(R.id.recycler_contacts)
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(REQUEST_KEY) { _, _ ->
            dataSource?.let {
                changeRecyclerData()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                val menuItem = menu.findItem(R.id.search_menu_view)
                val searchView = menuItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        tempList.clear()
                        val allContacts = dataSource?.getContacts()
                        p0?.let { text ->
                            val textToSearch: String = text.lowercase(Locale.getDefault())
                            if (text.isNotBlank()) {
                                allContacts?.let {
                                    for (i in 0 until allContacts.size) {
                                        if ("${allContacts[i].name} ${allContacts[i].surname}".lowercase()
                                                .contains(
                                                    textToSearch
                                                )
                                        ) {
                                            tempList.add(allContacts[i])
                                        }
                                        changeRecyclerData(tempList)
                                    }
                                }
                            } else {
                                allContacts?.let {
                                    changeRecyclerData()
                                }
                            }
                        }
                        return false
                    }
                })
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onStart() {
        super.onStart()
        recyclerView.let {
            curLayoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            it.layoutManager = curLayoutManager
            it.itemAnimator = null
            it.addItemDecoration(Decoration(20))
        }
        dataSource?.let {
            recyclerView.adapter = ContactAdapter(it.getContacts())
        }
        if (currentHolderPosition > 0) {
            recyclerView.scrollToPosition(currentHolderPosition)
        }
    }

    override fun onStop() {
        super.onStop()
        curLayoutManager.let {
            currentHolderPosition = it.findFirstVisibleItemPosition()
        }
    }

    override fun onDetach() {
        super.onDetach()
        dataSource = null
    }

    private fun changeRecyclerData() {
        recyclerView.recycledViewPool.clear()
        val conAdapter = recyclerView.adapter as ContactAdapter
        dataSource?.getContacts()?.let {
            val newList: MutableList<Contact> = ArrayList()
            newList.addAll(it)
            conAdapter.changeContacts(newList as List<Contact>)
        }
    }

    private fun changeRecyclerData(list: List<Contact>) {
        recyclerView.recycledViewPool.clear()
        val newList: MutableList<Contact> = ArrayList()
        newList.addAll(list)
        val conAdapter = recyclerView.adapter as ContactAdapter
        conAdapter.changeContacts(list = newList as ArrayList)

    }

    inner class ContactAdapter(private var contacts: List<Contact>) : RecyclerView
    .Adapter<ContactAdapter.ContactHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_contact, parent, false)
            return ContactHolder(itemView)
        }

        override fun getItemCount() = contacts.size

        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            holder.bind(contacts[position])
        }

        fun changeContacts(list: List<Contact>) {
            val oldContacts = getContactList()
            val diffUtilCallback = DiffUtilCallBack(oldList = oldContacts, newList = list)
            val result = DiffUtil.calculateDiff(diffUtilCallback, false)
            contacts = list
            result.dispatchUpdatesTo(this)
        }

        private fun getContactList() = contacts

        inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener, View.OnLongClickListener {
            private val contactPic: ImageView = itemView.findViewById(R.id.userPic)
            private val contactInfo: TextView = itemView.findViewById(R.id.contactData)
            private var contactBind: Contact? = null

            init {
                itemView.setOnClickListener(this)
                itemView.setOnLongClickListener(this)
            }

            override fun onClick(p0: View?) {
                contactBind?.let {
                    requireActivity().supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        ContactFragment.newContactFragment(it.id),
                        null
                    ).addToBackStack("TAG").commit()
                }
            }

            fun bind(contact: Contact) {
                contactBind = contact
                picasso.load(contact.getSmallPicURL().toUri())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder_error)
                    .into(contactPic)
                contactInfo.text = contact.toString()
            }

            override fun onLongClick(p0: View?): Boolean {
                contactBind?.let {
                    val dialog = DeleteContactDialog.newDeleteContactDialog(it.id)
                    dialog.show(requireActivity().supportFragmentManager, "TAG!")
                }
                return true
            }
        }
    }
}