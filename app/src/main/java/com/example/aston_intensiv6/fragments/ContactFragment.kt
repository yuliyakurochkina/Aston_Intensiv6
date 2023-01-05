package com.example.aston_intensiv6.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.aston_intensiv6.DataSetting
import com.example.aston_intensiv6.R
import com.example.aston_intensiv6.data.Contact
import com.squareup.picasso.Picasso

class ContactFragment : Fragment(R.layout.fragment_contact) {

    companion object {
        private const val ARGS_ID = "ARGS_id111"

        fun newContactFragment(id: Int): ContactFragment {
            val fragment = ContactFragment()
            val bundle = Bundle()
            bundle.putInt(ARGS_ID, id)
            fragment.arguments = bundle
            return fragment
        }
        fun getContactIdFromArgs(bundle: Bundle): Int {
            return bundle.getInt(ARGS_ID)
        }
    }

    private var dataSource: DataSetting? = null
    private lateinit var editName: EditText
    private lateinit var editLastName: EditText
    private lateinit var editNumber: EditText
    private lateinit var userPic: ImageView
    private lateinit var btnConfirm: Button
    private var contact: Contact? = null
    private var contactId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated")
        editName = view.findViewById(R.id.edit_name)
        editLastName = view.findViewById(R.id.edit_lastname)
        editNumber = view.findViewById(R.id.edit_number)
        userPic = view.findViewById(R.id.userPicBig)
        btnConfirm = view.findViewById(R.id.btn_confirm)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataSetting) {
            dataSource = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate")
        arguments?.let {
            contactId = getContactIdFromArgs(it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        dataSource = null
    }

    override fun onStart() {
        super.onStart()
        dataSource?.let {
            if (contactId >= 0) {
                contact = it.getContactById(contactId)
            }
        }
        contact?.let {
            setData(it)
            btnConfirm.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    it.apply {
                        name = editName.text?.toString().toString()
                        surname = editLastName.text?.toString().toString()
                        number = editNumber.text?.toString().toString()
                    }
                    dataSource?.replaceContact(it)
                    setFragmentResult(RecyclerViewFragment.REQUEST_KEY, Bundle())
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })
        }
    }

    private fun setData(con: Contact) {
        val factory = Editable.Factory.getInstance()
        editName.text = factory.newEditable(con.name)
        editLastName.text = factory.newEditable(con.surname)
        editNumber.text = factory.newEditable(con.number)
        Picasso.get().load(con.getBigPicURL().toUri())
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder_error)
            .into(userPic)
    }
}