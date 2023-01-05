package com.example.aston_intensiv6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aston_intensiv6.data.Contact
import com.example.aston_intensiv6.data.DataModel
import com.example.aston_intensiv6.fragments.RecyclerViewFragment
import java.util.ArrayList

class MainActivity : AppCompatActivity(), DataSetting {

    private var model: DataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = DataModel(context = applicationContext)

        supportFragmentManager.beginTransaction().run {
            replace(
                R.id.fragment_container,
                RecyclerViewFragment.newRecyclerFragment(),
                null
            )
            commit()
        }
    }

    override fun getContacts(): ArrayList<Contact> {
        var list: ArrayList<Contact> = ArrayList()
        model?.let {
            list = it.getContacts()
        }
        return list
    }

    override fun getContactById(id: Int): Contact? {
        var contact: Contact? = null
        model?.let {
            contact = it.getContById(id)
        }
        return contact
    }

    override fun deleteContactById(id: Int) {
        model?.deleteContact(id)
    }

    override fun replaceContact(contact: Contact) {
        model?.replaceContact(contact)
    }
}