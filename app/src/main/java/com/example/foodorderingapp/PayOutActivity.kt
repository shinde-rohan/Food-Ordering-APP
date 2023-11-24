package com.example.foodorderingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodorderingapp.Fragment.CongratsSheetFragment
import com.example.foodorderingapp.Model.OrderDetails
import com.example.foodorderingapp.databinding.ActivityPayOutBinding
import com.example.foodorderingapp.databinding.FragmentCongratsSheetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId : String
    private lateinit var name : String
    private lateinit var address : String
    private lateinit var phone : String
    private lateinit var totalAmount : String
    private lateinit var foodItemName : ArrayList<String>
    private lateinit var foodItemPrice : ArrayList<String>
    private lateinit var foodItemDescription : ArrayList<String>
    private lateinit var foodItemImage : ArrayList<String>
    private lateinit var foodItemIngredient : ArrayList<String>
    private lateinit var foodItemQuantity : ArrayList<Int>

    private lateinit var binding: ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.placeMyOrder.setOnClickListener {
            // get data from textview
            name = binding.name.text.toString().trim()
            address = binding.name.text.toString().trim()
            phone = binding.phone.text.toString().trim()

            if(name.isBlank() && address.isBlank() && phone.isBlank()){
                Toast.makeText(this,"Plese enter all the details",Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }
            val bottomSheetDialog2 = CongratsSheetFragment()
            bottomSheetDialog2.show(supportFragmentManager,"test")

            // inialization auth and database , userdetails
            auth = FirebaseAuth.getInstance()
            databaseReference = FirebaseDatabase.getInstance().getReference()
            // set user data
            setUserData()

            // get user details from firebase
            val intent :Intent= intent
            foodItemName = intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
            foodItemPrice = intent.getStringArrayListExtra("foodItemPrice") as ArrayList<String>
            foodItemDescription = intent.getStringArrayListExtra("foodItemDescription") as ArrayList<String>
            foodItemImage = intent.getStringArrayListExtra("foodItemImage") as ArrayList<String>
            foodItemIngredient = intent.getStringArrayListExtra("foodItemIngredient") as ArrayList<String>
            foodItemQuantity = intent.getIntegerArrayListExtra("foodItemQuantity") as ArrayList<Int>

            totalAmount = calculateAmount().toString() +"$"
//            binding.amount.isEnabled = false
            binding.amount.setText(totalAmount)
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId,name,foodItemName,foodItemImage,foodItemPrice,foodItemQuantity,address,totalAmount,phone,false,false,time,itemPushKey)
        val orderRef = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog2 = CongratsSheetFragment()
            bottomSheetDialog2.show(supportFragmentManager,"test")

            // remove item from card
            removeItemFromCard()
            // add order to history
            addOrderToHistory(orderDetails)
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to order",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory").child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCard() {
        val cartItemRef = databaseReference.child("user").child(userId).child("CartItems")
        cartItemRef.removeValue()
    }

    private fun calculateAmount(): Int {
        var totalAmount = 0
        for(i in 0 until foodItemPrice.size){
            var price= foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '$'){
                price.dropLast(1).toInt()
            } else {
                price.toInt()
            }
            var quantity = foodItemQuantity[i]
            totalAmount += priceIntValue * quantity

        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user != null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val addresses = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}