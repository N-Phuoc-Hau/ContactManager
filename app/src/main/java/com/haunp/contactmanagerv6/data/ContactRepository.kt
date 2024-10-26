package com.haunp.contactmanagerv6.data

import androidx.lifecycle.LiveData

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    suspend fun deleteAll() {
        contactDao.deleteAll()
    }
    // ContactRepository.kt
    suspend fun insertAll(contacts: List<Contact>) {
        contactDao.insertAll(contacts)
    }

    suspend fun isContactDuplicate(email: String, phone: String): Boolean {
        return contactDao.countByEmailOrPhone(email, phone) > 0
    }

}