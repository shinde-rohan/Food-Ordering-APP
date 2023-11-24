package com.example.foodorderingapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.Adapter.NotificationAdapter
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(layoutInflater,container,false)
        val notification = arrayListOf("your order is cancelled","order has been taken","congratulations")
        val notificationImg = arrayListOf(R.drawable.sademoji,R.drawable.truck,R.drawable.congrats)

        val adapter = NotificationAdapter(notification,notificationImg)
        binding.notificationrecy.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationrecy.adapter = adapter
        return binding.root

    }

    companion object {
    }
}