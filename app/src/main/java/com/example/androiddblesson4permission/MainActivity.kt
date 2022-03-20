package com.example.androiddblesson4permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.daimajia.swipe.SwipeLayout
import com.example.adapters.ContactRecyclerView
import com.example.androiddblesson4permission.databinding.ActivityMainBinding
import com.example.models.Contact
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.github.florent37.runtimepermission.kotlin.askPermission
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var contactRecyclerView: ContactRecyclerView
    lateinit var contactList: ArrayList<Contact>
    var permission: Boolean = false
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contactList = ArrayList()
        myMethod()

    }

    @SuppressLint("Range", "Recycle")
    private fun getContactList() {
        val uri: Uri = ContactsContract.Contacts.CONTENT_URI
        val sorting: String = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        var cursor: Cursor? = contentResolver.query(uri, null, null, null, sorting)
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                var id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                var name: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var uriPhone: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                var selection: String = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                var phoneCursor: Cursor? =
                    contentResolver.query(uriPhone, null, selection, arrayOf(id), null)
                if (phoneCursor!!.moveToNext()) {
                    val number: String =
                        phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contact = Contact(name, number)
                    contactList.add(contact)
                    phoneCursor.close()
                }
            }
            cursor.close()

        }

    }

    fun myMethod() {
        askPermission(Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS) {
            //all permissions already granted or just granted
            //your action
            getContactList()
            contactRecyclerView = ContactRecyclerView(contactList)
            binding.contactRV.adapter = contactRecyclerView
            contactRecyclerView.setOnMyItemCallListener(object :
                ContactRecyclerView.OnMyItemCallListener {
                override fun onClick(contact: Contact) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:${contact.number}")
                    startActivity(callIntent)
                }

            })
            contactRecyclerView.setOnMyItemSmsListener(object :
                ContactRecyclerView.OnMyItemSmsListener {
                override fun onClick(contact: Contact) {
                    val intent = Intent(this@MainActivity, SmsActivity::class.java)
                    var bundle = Bundle()
                    bundle.putSerializable("contact", contact)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

            })

            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
        }.onDeclined { e ->
            if (e.hasDenied()) {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }

}