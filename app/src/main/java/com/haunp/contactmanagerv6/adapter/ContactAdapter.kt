package com.haunp.contactmanagerv6.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haunp.contactmanagerv6.R
import com.haunp.contactmanagerv6.data.Contact


class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contactList = emptyList<Contact>()

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contactName)
        val emailTextView: TextView = itemView.findViewById(R.id.contactEmail)
        val phoneTextView: TextView = itemView.findViewById(R.id.contactPhone)

        fun bind(contact: Contact) {
            nameTextView.text = contact.name
            emailTextView.text = contact.email
            phoneTextView.text = contact.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bind(contact)
    }

    override fun getItemCount() = contactList.size

    fun setContacts(contacts: List<Contact>) {
        contactList = contacts
        notifyDataSetChanged()
    }
}