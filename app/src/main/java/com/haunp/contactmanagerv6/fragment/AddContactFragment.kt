package com.haunp.contactmanagerv6.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.haunp.contactmanagerv6.R
import com.haunp.contactmanagerv6.data.Contact
import com.haunp.contactmanagerv6.viewmodel.ContactViewModel

class AddContactFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)

        nameEditText = view.findViewById(R.id.editTextName)
        emailEditText = view.findViewById(R.id.editTextEmail)
        phoneEditText = view.findViewById(R.id.editTextPhone)

        val addButton = view.findViewById<Button>(R.id.buttonAddContact)
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        addButton.setOnClickListener {
            addNewContact()
        }

        return view
    }

    private fun addNewContact() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
            val newContact = Contact(name = name, email = email, phone = phone)
            contactViewModel.insert(newContact)
            Toast.makeText(requireContext(), "Contact added", Toast.LENGTH_SHORT).show()
            // Quay lại ContactListFragment sau khi thêm liên hệ
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ContactListFragment())
                .commit()
        } else {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
        }
    }
}
