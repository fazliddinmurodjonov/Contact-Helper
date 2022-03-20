package com.example.androiddblesson4permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import com.example.androiddblesson4permission.databinding.ActivitySmsBinding
import com.example.models.Contact
import java.lang.Exception

class SmsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val contact = intent.extras?.getSerializable("contact") as Contact
        binding.contactName.text = contact.name
        binding.contactNumber.text = contact.number
        binding.backButton.setOnClickListener { finish() }
        binding.sendMessageButton.setOnClickListener {
            val message = binding.sendMessage.text.toString()
            sendSMS(contact.number!!, message)
            finish()
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            Toast.makeText(applicationContext, "Message Sent",
                Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            Toast.makeText(applicationContext, ex.message.toString(),
                Toast.LENGTH_LONG).show()
            ex.printStackTrace()
        }
    }
}