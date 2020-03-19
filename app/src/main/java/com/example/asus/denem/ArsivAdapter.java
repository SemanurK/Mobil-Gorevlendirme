package com.example.asus.denem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArsivAdapter extends BaseAdapter {
    List<GorevAlanKisi> gelen_gorevler;
    LayoutInflater inflater;

    public ArsivAdapter(Activity activity, List<GorevAlanKisi> gelen_gorevler) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gelen_gorevler=gelen_gorevler;
    }
    @Override
    public int getCount() {
        return gelen_gorevler.size();
    }

    @Override
    public Object getItem(int position) {
        return gelen_gorevler.get(position);
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
            convertView=inflater.inflate(R.layout.arsiv_item,parent,false);
            h=new holder();
            h.gorevlendiren_ad=(TextView)convertView.findViewById(R.id.arsiv_kulad);
            h.gorev_aciklama=(TextView)convertView.findViewById(R.id.arsiv_aciklama);

            h.resim=(ImageView)convertView.findViewById(R.id.arsiv_resim);
            h.son_tarih=(TextView)convertView.findViewById(R.id.arsiv_sontarih);
            h.onaylanma_tarih=(TextView)convertView.findViewById(R.id.arsiv_onaylanma_tarih);

            convertView.setTag(h);

        }
        else
            h=(holder)convertView.getTag();
        GorevAlanKisi gorev_detay=gelen_gorevler.get(position);
        // h.son_tarih.setText(gorev_detay.getSontarih());
        h.gorevlendiren_ad.setText(gorev_detay.getAd_soyad());
        h.gorev_aciklama.setText(gorev_detay.getGorev_aciklama());
        h.son_tarih.setText(gorev_detay.getGorulme_tarih());
        h.onaylanma_tarih.setText(gorev_detay.getOnaylanma_tarih());
        // h.onaylanma_tarih.setText(gorev_detay);



        if(gorev_detay.isCinsiyet())
        {
            h.resim.setImageResource(R.drawable.kadin);
        }
        else{
            h.resim.setImageResource(R.drawable.erkek1);
        }

        return  convertView;

    }
    class holder{
        TextView gorevlendiren_ad;
        ImageView resim;
        TextView son_tarih;
        TextView onaylanma_tarih;
        TextView gorev_aciklama;
        // CheckBox checkBox;


    }
}
