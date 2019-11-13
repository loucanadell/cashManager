package com.msc.cashmanager.model

import android.os.Parcel
import android.os.Parcelable

class User(var username: String?, var lastname: String?, var email: String?,
           var password: String?, var address :String?) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(lastname)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}