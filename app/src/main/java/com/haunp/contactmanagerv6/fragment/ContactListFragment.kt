package com.haunp.contactmanagerv6.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haunp.contactmanagerv6.R
import com.haunp.contactmanagerv6.adapter.ContactAdapter
import com.haunp.contactmanagerv6.viewmodel.ContactViewModel

class ContactListFragment : Fragment() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter = ContactAdapter()
        recyclerView.adapter = contactAdapter

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.allContacts.observe(viewLifecycleOwner, { contacts ->
            contactAdapter.setContacts(contacts)
        })
        return view
    }
}
