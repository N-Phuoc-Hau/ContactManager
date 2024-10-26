package com.haunp.contactmanagerv6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.haunp.contactmanagerv6.data.Contact
import com.haunp.contactmanagerv6.data.ContactDatabase
import com.haunp.contactmanagerv6.data.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ContactRepository
    val allContacts: LiveData<List<Contact>>

    init {
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(contactDao)
        allContacts = repository.allContacts
    }

    fun insertAllContacts(contacts: List<Contact>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(contacts)
    }

    fun insert(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}