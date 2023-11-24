package com.example.foodorderingapp.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.BuyItemBinding

class BuyAgainAdapter(private var buyFood : MutableList<String>,
                      private var buyPrice : MutableList<String>,
                      private var buyImage : MutableList<String>,
                        private var context : Context
):RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {
   inner class BuyAgainViewHolder(private val binding: BuyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImg: String) {
            binding.buyName.text = foodName
            binding.buyPrice.text = foodPrice
//            binding.buyImg.setImageResource(foodImg)
            val uriString = foodImg
            val uri = Uri.parse(uriString)
            Glide.with(context).load(uri).into(binding.buyImg)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return buyFood.size
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyFood[position],buyPrice[position],buyImage[position])
    }
}