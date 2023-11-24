package com.example.foodorderingapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.bumptech.glide.Glide
import com.example.foodorderingapp.Model.CartItems
import com.example.foodorderingapp.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    private var foodName : String? = null
    private var foodImage : String? = null
    private var foodPrice : String? = null
    private var foodDescription : String? = null
    private var foodIngredients : String? = null
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        //before using model and firebase
//        val foodName = intent.getStringExtra("menuItemName")
//        val foodImg = intent.getIntExtra("menuItemImg",0)
//        binding.DetailFoodName.text = foodName
//        binding.detailFoodImg.setImageResource(foodImg)

        //after firebase and model

        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodImage = intent.getStringExtra("MenuItemImage")
        with(binding){
            DetailFoodName.text = foodName
            descriptionTv.text = foodDescription
            ingredientTv.text = foodIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImg)

        }
        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.addItemButton.setOnClickListener {
            addItemCard()
        }
    }

    private fun addItemCard() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        // create ancard item object
        val cartItem = CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)
        // save data item to firebase
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Item added into cart successfully",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Item does not add",Toast.LENGTH_LONG).show()
        }
    }
}