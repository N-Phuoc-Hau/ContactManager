package com.haunp.contactmanagerv6.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haunp.contactmanagerv6.R
import com.haunp.contactmanagerv6.data.Contact
import com.haunp.contactmanagerv6.viewmodel.ContactViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadJsonFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private val loadedJsonFiles = mutableSetOf<Int>() // Lưu các file JSON đã được load

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_load_json, container, false)


        // Khởi tạo ViewModel
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        // Nút chọn JSON
        val buttonSelectJson = view.findViewById<Button>(R.id.buttonSelectJson)
        buttonSelectJson.setOnClickListener {
            showJsonSelectionDialog()
        }

        return view
    }

    private fun showJsonSelectionDialog() {
        val jsonOptions = arrayOf("Load contact_data_small.json", "Load contact_data_medium.json", "Load contact_data_big.json")
        val jsonResources = arrayOf(R.raw.contact_data_small, R.raw.contact_data_medium, R.raw.contact_data_big)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Chọn file JSON")
            .setItems(jsonOptions) { _, which ->
                val selectedJson = jsonResources[which]
                if (loadedJsonFiles.contains(selectedJson)) {
                    Toast.makeText(requireContext(), "File JSON này đã được load trước đó", Toast.LENGTH_SHORT).show()
                } else {
                    loadContactsFromJson(selectedJson)
                }
            }
        builder.create().show()
    }

    private fun loadContactsFromJson(resourceId: Int) {
        // Thêm file JSON vào danh sách đã load để tránh load lại
        loadedJsonFiles.add(resourceId)


        // Sử dụng Coroutine để tải và thêm dữ liệu vào database trên luồng nền
        lifecycleScope.launch(Dispatchers.IO) {
            val inputStream = resources.openRawResource(resourceId)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val contactType = object : TypeToken<List<Contact>>() {}.type
            val contactList: List<Contact> = gson.fromJson(jsonString, contactType)

            // Sử dụng transaction để đảm bảo tất cả dữ liệu được thêm trước khi cập nhật giao diện
            contactViewModel.insertAllContacts(contactList)

            // Chuyển về luồng Main để cập nhật giao diện sau khi hoàn tất
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Đã tải xong", Toast.LENGTH_SHORT).show()


                // Chuyển sang ContactListFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ContactListFragment())
                    .commit()
            }
        }
    }

    fun resetLoadedJsonFiles() {
        loadedJsonFiles.clear()
        Toast.makeText(requireContext(), "Đã reset danh sách file JSON đã load", Toast.LENGTH_SHORT).show()
    }
}
