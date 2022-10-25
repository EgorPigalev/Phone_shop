package com.example.phone_shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterMask extends BaseAdapter {

    private Context mContext;
    List<Mask> maskList;

    public AdapterMask(Context mContext, List<Mask> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return maskList.get(i).getId_phone();
    }

    private Bitmap getUserImage(String encodedImg) // Преобразование из String в Bitmap
    {

        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
            return null;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext,R.layout.item_mask,null);

        TextView Manufacturer = v.findViewById(R.id.tvManufacturer);
        TextView Model = v.findViewById(R.id.tvModel);
        TextView Colour = v.findViewById(R.id.tvColour);
        TextView Price = v.findViewById(R.id.tvPrice);
        ImageView Image = v.findViewById(R.id.ivPicture);

        Mask mask = maskList.get(position);
        Manufacturer.setText(mask.getManufacturer());
        Model.setText(mask.getModel());
        Colour.setText(mask.getColour());
        Price.setText(mask.getPrice().toString());
        if(mask.getImage().toString().equals("null"))
        {
            Image.setImageResource(R.drawable.absence);
        }
        else
        {
            Image.setImageBitmap(getUserImage(mask.getImage()));
        }

        return v;
    }

}