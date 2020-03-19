package com.example.asus.denem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Bolum_Adapter extends BaseAdapter {
    List<BolumModel> bolumler;
    LayoutInflater inflater;
    public Bolum_Adapter(Activity activity, List<BolumModel> bolumler) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bolumler=bolumler;
    }
    @Override
    public int getCount() {
        return bolumler.size();
    }

    @Override
    public Object getItem(int position) {
        return bolumler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder h=null;
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.bolum_satir,parent,false);
            h=new holder();
            h.Bolum_adi=(TextView)convertView.findViewById(R.id.bolumadi);

            convertView.setTag(h);

        }
        else
            h=(holder)convertView.getTag();


        BolumModel b=bolumler.get(position);
        h.Bolum_adi.setText(b.getBolum_adi());

        return convertView;
    }
    class holder{
        TextView Bolum_adi;


    }
}
