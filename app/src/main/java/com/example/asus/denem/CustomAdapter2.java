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

public class CustomAdapter2 extends BaseAdapter {
    List<Kullanicilar> gelen_kullanicilar;
    LayoutInflater inflater;
    public CustomAdapter2(Activity activity, List<Kullanicilar> gelen_kullanicilar) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gelen_kullanicilar=gelen_kullanicilar;
    }
    @Override
    public int getCount() {
        return gelen_kullanicilar.size();
    }

    @Override
    public Object getItem(int position) {
        return gelen_kullanicilar.get(position);
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
            convertView=inflater.inflate(R.layout.kullanici_satir,parent,false);
            h=new holder();
            h.kullanici_isim=(TextView)convertView.findViewById(R.id.kullanici_isim);
            h.rol_adi=(TextView)convertView.findViewById(R.id.kul_unvan);

            h.resim=(ImageView)convertView.findViewById(R.id.k_resim);

            h.ischecked=(ImageView)convertView.findViewById(R.id.k_durum);
            convertView.setTag(h);

        }
        else
            h=(holder)convertView.getTag();


        Kullanicilar kullanicilar=gelen_kullanicilar.get(position);
        h.kullanici_isim.setText(kullanicilar.getKUL_ADSOYAD());
        h.rol_adi.setText(kullanicilar.getRol_Ad());

        if(kullanicilar.isDurum())
        {
            h.ischecked.setImageResource(R.drawable.isaret);
        }
        else
            h.ischecked.setImageResource(R.drawable.isaretsiz);

        if(kullanicilar.isCINSIYET())
        {
            h.resim.setImageResource(R.drawable.kadin);
        }
        else{
            h.resim.setImageResource(R.drawable.erkek1);
        }
        return convertView;
    }
    public void update(List<Kullanicilar> detays)
    {
        this.gelen_kullanicilar=detays;
        notifyDataSetChanged();
    }

    class holder{
        TextView kullanici_isim,rol_adi;
        ImageView resim;

        ImageView ischecked;

    }
}
