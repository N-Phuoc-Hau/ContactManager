package com.haunp.contactmanagerv6.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haunp.contactmanagerv6.R
import com.haunp.contactmanagerv6.data.Contact
import com.haunp.contactmanagerv6.viewmodel.ContactViewModel
import kotlinx.coroutines.launch
import java.io.InputStream

@Suppress("DEPRECATION")
class LoadJsonFragmentTeset : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private val loadedJsonFiles = mutableSetOf<Int>()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_load_json, container, false)

        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE
        showJsonSelectionDialog()

        // Khởi tạo ViewModel
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)


        progressBar.visibility = View.GONE
        return view
    }

    private fun showJsonSelectionDialog() {
        val jsonOptions = arrayOf("Load contact_data_small.json", "Load contact_data_medium.json", "Load contact_data_big.json")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Chọn file JSON")
            .setItems(jsonOptions) { _, which ->
                val resourceId = when (which) {
                    0 -> R.raw.contact_data_small
                    1 -> R.raw.contact_data_medium
                    else -> R.raw.contact_data_big
                }

                loadContactsFromJson(resourceId)
            }
        builder.create().show()
    }

    private fun loadContactsFromJson(resourceId: Int) {
        if (loadedJsonFiles.contains(resourceId)) {
            Toast.makeText(requireContext(), "File JSON này đã được load trước đó", Toast.LENGTH_SHORT).show()
            return
        }

        loadedJsonFiles.add(resourceId)


        lifecycleScope.launch {
            val inputStream = resources.openRawResource(resourceId)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val contactType = object : TypeToken<List<Contact>>() {}.type
            val contactList: List<Contact> = gson.fromJson(jsonString, contactType)

            contactList.forEach { contact ->
                contactViewModel.insert(contact)
            }


            Toast.makeText(requireContext(), "Đã tải các liên hệ không trùng lặp từ JSON", Toast.LENGTH_SHORT).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ContactListFragment())
                .commit()
        }
    }

    fun resetLoadedJsonFiles() {
        loadedJsonFiles.clear()
    }
}





