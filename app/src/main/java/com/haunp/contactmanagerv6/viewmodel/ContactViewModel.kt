package com.haunp.contactmanagerv6.viewmodel

import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haunp.contactmanagerv6.R
import com.haunp.contactmanagerv6.data.Contact
import com.haunp.contactmanagerv6.data.ContactDatabase
import com.haunp.contactmanagerv6.data.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ContactRepository
    val allContacts: LiveData<List<Contact>>

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    init {
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(contactDao)
        allContacts = repository.allContacts
    }

    fun insertAllContacts(contacts: List<Contact>) = viewModelScope.launch(Dispatchers.IO) {
        // Kiểm tra trùng lặp trước khi chèn bất kỳ liên hệ nào
        _loadingState.postValue(true)
        for (contact in contacts) {
            val isDuplicate = repository.isContactDuplicate(contact.email, contact.phone)
            if (isDuplicate) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "File này đã tồn tại", Toast.LENGTH_SHORT).show()
                }
                return@launch // Dừng hàm nếu có trùng lặp
            }
        }

        // Nếu không có trùng lặp, chèn toàn bộ danh sách liên hệ
        repository.insertAll(contacts)
        withContext(Dispatchers.Main) {
            Toast.makeText(getApplication(), "Đã thêm tất cả liên hệ", Toast.LENGTH_SHORT).show()
        }
        _loadingState.postValue(false)
    }

    fun insert(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        // Kiểm tra trùng lặp
        val isDuplicate = repository.isContactDuplicate(contact.email, contact.phone)
        if (isDuplicate) {
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), "Email và số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show()
            }
        } else {
            repository.insert(contact)
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), "Đã thêm liên hệ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}