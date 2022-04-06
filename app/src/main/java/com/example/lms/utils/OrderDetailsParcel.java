package com.example.lms.utils;

/**
 * Created by admin on 11/10/2014.
 */

import android.os.Parcel;
import android.os.Parcelable;
import com.example.lms.models.OrderDetails;

import java.util.ArrayList;


public class OrderDetailsParcel extends ArrayList<OrderDetails> implements Parcelable {


    public OrderDetailsParcel() {


    }


    public OrderDetailsParcel(Parcel in) {

        readFromParcel(in);

    }


    @SuppressWarnings("unchecked")

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public OrderDetailsParcel createFromParcel(Parcel in) {

            return new OrderDetailsParcel(in);

        }


        public Object[] newArray(int arg0) {

            return null;

        }


    };


    private void readFromParcel(Parcel in) {

        this.clear();


        //First we have to read the list size

        int size = in.readInt();


        //Reading remember that we wrote first the Name and later the Phone Number.

        //Order is fundamental


        for (int i = 0; i < size; i++) {

            OrderDetails c = new OrderDetails();

            c.setQty(in.readLong());
            c.setServiceId(in.readLong());
            c.setItemRemarks(in.readString());
            c.setItemId(in.readLong());
            c.setPrice(in.readDouble());
            c.setUnitPrice(in.readDouble());
            this.add(c);

        }


    }


    public int describeContents() {

        return 0;

    }


    public void writeToParcel(Parcel dest, int flags) {

        int size = this.size();

        //We have to write the list size, we need him recreating the list

        dest.writeInt(size);

        //We decided arbitrarily to write first the Name and later the Phone Number.

        for (int i = 0; i < size; i++) {

            OrderDetails c = this.get(i);


            dest.writeLong(c.getQty());
            dest.writeLong(c.getServiceId());
            dest.writeString(c.getItemRemarks());
            dest.writeLong(c.getItemId());
            dest.writeDouble(c.getPrice());
            dest.writeDouble(c.getUnitPrice());

        }

    }


}
