package com.example.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.example.androiddblesson4permission.R
import com.example.androiddblesson4permission.databinding.ItemContactBinding
import com.example.models.Contact

class ContactRecyclerView(var contactList: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactRecyclerView.ContactViewHolder>() {
    lateinit var callListener: OnMyItemCallListener
    lateinit var smsListener: OnMyItemSmsListener

    inner class ContactViewHolder(var binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(contact: Contact, position: Int) {
            binding.root.showMode = SwipeLayout.ShowMode.LayDown
            binding.root.showMode = SwipeLayout.ShowMode.PullOut
            binding.root.addDrag(SwipeLayout.DragEdge.Right, binding.layout)
            binding.contactName.text = contact.name
            binding.contactNumber.text = contact.number
            binding.call.setOnClickListener {
                callListener.onClick(contact)
            }
            binding.sms.setOnClickListener {
                smsListener.onClick(contact)
            }
        }
    }

    interface OnMyItemCallListener {
        fun onClick(contact: Contact)
    }

    interface OnMyItemSmsListener {
        fun onClick(contact: Contact)
    }

    fun setOnMyItemCallListener(listener: OnMyItemCallListener) {
        callListener = listener
    }

    fun setOnMyItemSmsListener(listener: OnMyItemSmsListener) {
        smsListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(ItemContactBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.onBind(contact, position)

    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}