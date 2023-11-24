package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.Adapter.CardAdapter
import com.example.foodorderingapp.Model.CartItems
import com.example.foodorderingapp.PayOutActivity
import com.example.foodorderingapp.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class CartFragment : Fragment() {

    private lateinit var  binding: FragmentCartBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var foodNames : MutableList<String>
    private lateinit var foodPrices : MutableList<String>
    private lateinit var foodDescriptions : MutableList<String>
    private lateinit var foodIngredients : MutableList<String>
    private lateinit var foodImageUrls : MutableList<String>
    private lateinit var quantity  : MutableList<Int>
    private lateinit var cartAdapter :CardAdapter
    private  lateinit var userId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater,container,false)


        //initialization auth and database
        auth = FirebaseAuth.getInstance()
        retriveCartItems()
        // before using firebase and model
//        val cardFood = listOf("Burger","Sandwitch","momo","samosa","xyz","jhhh")
//        val cardPrice = listOf("$4","$2","$5","$5","$9","$2")
//        val cardImage  = listOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,
//            R.drawable.menu4,R.drawable.menu5,R.drawable.menu6)
//
//        val adapter = CardAdapter(ArrayList(cardFood),ArrayList(cardPrice),ArrayList(cardImage))
//        binding.cardRecy.layoutManager = LinearLayoutManager(requireContext())
//        binding.cardRecy.adapter = adapter

        binding.btnProcced.setOnClickListener {
            // get order items details before processing to checkout
            getOrderItemDetails()
//            val intent = Intent(requireContext(),PayOutActivity::class.java)
//            startActivity(intent)
        }


        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdReference :DatabaseReference  = database.reference.child("user").child(userId).child("CartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        // get items quantities
        val foodQuantities = cartAdapter.getUpdateItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot in snapshot.children){
                    // get child card item to respective list
                    val orderItems = dataSnapshot.getValue(CartItems::class.java)

                    // add items details in to list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }

                    orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredient,foodQuantities)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"order making failed, please try again",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        if(isAdded && context!= null){
            val intent = Intent(requireContext(),PayOutActivity::class.java)
            intent.putExtra("foodItemName",foodName as ArrayList<String>)
            intent.putExtra("foodItemPrice",foodPrice as ArrayList<String>)
            intent.putExtra("foodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("foodItemImage",foodImage as ArrayList<String>)
            intent.putExtra("foodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("foodItemQuantity",foodQuantities as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retriveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val foodRef = database.reference.child("user").child(userId).child("CartItems")

        // list to store card items
        foodNames = mutableListOf()
        foodImageUrls = mutableListOf()
        foodDescriptions = mutableListOf()
        foodPrices = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()

        // fetch data from the database
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapShot in snapshot.children){
                    // get the cart item obj from child node
                    val cartItem:CartItems? = foodSnapShot.getValue(CartItems::class.java)
                    cartItem?.foodName?.let { foodNames.add(it) }
                    cartItem?.foodPrice?.let { foodPrices.add(it) }
                    cartItem?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItem?.foodQuantity?.let { quantity.add(it) }
                    cartItem?.foodImage?.let { foodImageUrls.add(it) }
                    cartItem?.foodIngredient?.let { foodIngredients.add(it) }
                }
                setAdapter()
            }
            private fun setAdapter() {
                cartAdapter= CardAdapter(foodNames,foodPrices,foodDescriptions,foodImageUrls,quantity,foodIngredients,requireContext())
                binding.cardRecy.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cardRecy.adapter = cartAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data is not fetched", Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun setAdapter() {
//        val adapter = CardAdapter(foodNames,foodPrices,foodDescriptions,foodImageUrls,quantity,foodIngredients,requireContext())
//        binding.cardRecy.layoutManager = LinearLayoutManager(requireContext())
//        binding.cardRecy.adapter = adapter
//    }

    companion object {

    }
}