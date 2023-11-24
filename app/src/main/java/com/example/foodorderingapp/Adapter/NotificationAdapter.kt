package com.example.foodorderingapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.databinding.NotificationItemBinding

class NotificationAdapter(private var notification : ArrayList<String>,private var notificationImg : ArrayList<Int>):RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
   inner class NotificationViewHolder(private val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                notifyText.text = notification[position]
                notifyImg.setImageResource(notificationImg[position])
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {

        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return notification.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }
}