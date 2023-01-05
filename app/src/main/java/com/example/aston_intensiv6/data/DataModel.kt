package com.example.aston_intensiv6.data

import android.content.Context
import android.content.res.Resources
import com.example.aston_intensiv6.R
import kotlin.random.Random

class DataModel(context: Context) {
    private companion object {
        const val PHONE_SAMPLE = "+7900%d"
    }

    private val contactMap: MutableMap<Int, Contact> = HashMap()
    private val res: Resources = context.resources
    private val lastnameArray: Array<String> = res.getStringArray(R.array.lastnames)
    private val nameArray: Array<String> = res.getStringArray(R.array.names)

    private var contactsCreated = 0

    init {
        for (i in 0..120)
            addRandomContact()
    }

    private fun addRandomContact() {
        val randomName = nameArray[Random.nextInt(nameArray.size)]
        val randomLastName = lastnameArray[Random.nextInt(lastnameArray.size)]
        val randomNumber =
            String.format(PHONE_SAMPLE, (Random.nextInt(8_999_999) + 1_000_000))
        val picId = getRandomPicId()

        val contact = Contact(
            id = contactsCreated,
            name = randomName,
            surname = randomLastName,
            number = randomNumber,
            picId = picId
        )
        contactMap[contact.id] = contact
        contactsCreated++
    }

    private fun getRandomPicId(): Int = Random.nextInt(1050) + 1

    fun getContacts(): ArrayList<Contact> {
        val list = ArrayList<Contact>()
        list.addAll(contactMap.values)
        return list
    }

    fun getContById(id: Int): Contact? {
        return contactMap[id]
    }

    fun deleteContact(id: Int) {
        contactMap.remove(id)
    }

    fun replaceContact(con: Contact) {
        contactMap[con.id] = con
    }
}