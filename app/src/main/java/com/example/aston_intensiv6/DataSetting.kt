package com.example.aston_intensiv6

import com.example.aston_intensiv6.data.Contact

interface DataSetting {
    fun getContacts(): ArrayList<Contact>
    fun getContactById(id: Int): Contact?
    fun deleteContactById(id: Int)
    fun replaceContact(contact: Contact)
}