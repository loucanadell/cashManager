package com.msc.cashmanager.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class SelectedProduct(@SerializedName("id") var id: Int,
                      @SerializedName("name") var name: String?,
              @SerializedName("price") var price: Float) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedProduct> {
        override fun createFromParcel(parcel: Parcel): SelectedProduct {
            return SelectedProduct(parcel)
        }

        override fun newArray(size: Int): Array<SelectedProduct?> {
            return arrayOfNulls(size)
        }
    }

}