package com.example.foodorderingapp.Model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class OrderDetails():Parcelable{
    var userId : String? = null
    var userName : String? = null
    var foodName : MutableList<String>? = null
    var foodImages : MutableList<String>? = null
    var foodPrices : MutableList<String>? = null
    var foodQuantities : MutableList<Int>? = null
    var address : String? = null
    var totalPrice : String? = null
    var phoneNumber : String? = null
    var orderAccepted : Boolean? = false
    var paymentReceived : Boolean? = false
    var itemPushKey : String? = null
    var currentTime : Long = 0

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        paymentReceived = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

     constructor(
         userId: String,
         name: String,
         foodItemName: ArrayList<String>,
         foodItemImage: ArrayList<String>,
         foodItemPrice: ArrayList<String>,
         foodItemQuantity: ArrayList<Int>,
         address: String,
         totalAmount : String,
         phone: String,
         b: Boolean,
         b1: Boolean,
         time: Long,
         itemPushKey: String?
     ) : this(){
         this.userId = userId
         this.userName = foodItemName.toString()
         this.foodPrices = foodItemPrice
         this.foodImages = foodItemImage
         this.foodQuantities = foodItemQuantity
         this.address = address
         this.totalPrice = totalAmount
         this.phoneNumber = phone
         this.currentTime = time
         this.itemPushKey = itemPushKey
         this.orderAccepted = orderAccepted
         this.paymentReceived = paymentReceived
     }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeValue(orderAccepted)
        parcel.writeValue(paymentReceived)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }


}
