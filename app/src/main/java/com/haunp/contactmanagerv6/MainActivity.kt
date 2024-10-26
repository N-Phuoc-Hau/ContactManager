package com.haunp.contactmanagerv6

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haunp.contactmanagerv6.data.Contact
import com.haunp.contactmanagerv6.fragment.AddContactFragment
import com.haunp.contactmanagerv6.fragment.ContactListFragment
import com.haunp.contactmanagerv6.fragment.LoadJsonFragment
import com.haunp.contactmanagerv6.viewmodel.ContactViewModel
import com.haunp.contactmanagerv6.viewmodel.ContactViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var contactViewModel: ContactViewModel

    private lateinit var contactCountTextView: TextView


    private lateinit var overlayView: View
    private lateinit var textViewLoadingMessage: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Khởi tạo ContactViewModel
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // Khởi tạo TextView để đếm số liên hệ
        contactCountTextView = findViewById(R.id.contactCountTextView)



        overlayView = findViewById(R.id.overlayView)
        textViewLoadingMessage = findViewById(R.id.textViewLoadingMessage)
        progressBar = findViewById(R.id.progressBar)

        contactViewModel.loadingState.observe(this) { isLoading ->
            if (isLoading) {
                overlayView.visibility = View.VISIBLE
                textViewLoadingMessage.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
            } else {
                overlayView.visibility = View.GONE
                textViewLoadingMessage.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }

        // Cập nhật số lượng liên hệ
        updateContactCount()
        // Thiết lập BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_add_contact -> {
                    loadFragment(AddContactFragment())
                    true
                }
                R.id.nav_contact_list -> {
                    loadFragment(ContactListFragment())
                    true
                }
                R.id.nav_delete_all -> {
                    deleteAllContacts()
                    true
                }
                R.id.nav_load_json -> {
                    loadFragment(LoadJsonFragment()) // Hiển thị LoadJsonFragment khi nhấn Load JSON
                    true
                }
                else -> false
            }
        }

        // Hiển thị danh sách liên hệ ban đầu khi mở ứng dụng
        if (savedInstanceState == null) {
            loadFragment(ContactListFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    private fun deleteAllContacts() {
        contactViewModel.deleteAll()

        // Reset trạng thái đã load JSON trong LoadJsonFragment
        val loadJsonFragment = supportFragmentManager.findFragmentByTag("LoadJsonFragment") as? LoadJsonFragment
        loadJsonFragment?.resetLoadedJsonFiles()

        Toast.makeText(this, "Đã xóa tất cả liên hệ", Toast.LENGTH_SHORT).show()
    }
    private fun updateContactCount() {
        contactViewModel.allContacts.observe(this, { contacts ->
            contactCountTextView.text = "Contacts: ${contacts.size}"
        })
    }

}

