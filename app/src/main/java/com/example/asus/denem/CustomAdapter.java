package com.example.asus.denem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.asus.denem.R.color.colorPrimary;

public class CustomAdapter extends BaseAdapter {
    List<Gorev_Detay> gelen_gorevler;
    LayoutInflater inflater;

     public CustomAdapter(Activity activity, List<Gorev_Detay> gelen_gorevler) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     this.gelen_gorevler=gelen_gorevler;
    }


    @Override
    public int getCount() {
        return gelen_gorevler.size();
        //listview gösterilecek satır sayısını
    }

    @Override
    public Object getItem(int position) {
        return gelen_gorevler.get(position);
        //position ile sırası geleman eleman
    }

    @Override
    public long getItemId(int position) {
        return position;
        //eğer varsa niteleyici id bilgisi
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       holder h=null;
       if(convertView==null)
       {
           convertView=inflater.inflate(R.layout.gorev_satir,parent,false);
           h=new holder();
           h.gorevlendirem_ad=(TextView)convertView.findViewById(R.id.gorevlendiren_isim);
           h.gorev_aciklama=(TextView)convertView.findViewById(R.id.gorev_aciklama);
         //  h.checkBox=(CheckBox)convertView.findViewById(R.id.gorev_durum);
           h.resim=(ImageView)convertView.findViewById(R.id.resim);
           h.son_tarih=(TextView)convertView.findViewById(R.id.son_tarih);
           h.ischecked=(ImageView)convertView.findViewById(R.id.durum);
           convertView.setTag(h);

       }
       else
           h=(holder)convertView.getTag();
       Gorev_Detay gorev_detay=gelen_gorevler.get(position);
      // h.son_tarih.setText(gorev_detay.getSontarih());
       h.gorevlendirem_ad.setText(gorev_detay.getGorevlendiren_Ad());
       h.gorev_aciklama.setText(gorev_detay.getGorev_aciklama());

        if(gorev_detay.isGorev_durum())
        {
            h.ischecked.setImageResource(R.drawable.isaret);
        }
        else
            h.ischecked.setImageResource(R.drawable.isaretsiz);

        SimpleDateFormat formatter1=new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        Date date1=null;
        int gun_farki = 0;
        try {
             date1=formatter1.parse(gorev_detay.getSontarih());
            long fark=date.getTime()-date1.getTime();
            gun_farki=Math.abs((int)(fark / (1000*60*60*24)));

        } catch (ParseException e) {
            e.printStackTrace();
        }


       if(date1.compareTo(date)>0 && gun_farki<=3 ||date1.compareTo(date)<0 && gun_farki==0)
        {
           h.son_tarih.setTextColor(Color.RED);
            h.son_tarih.setText(gorev_detay.getSontarih());
        }
        else if(date1.before(date)&& gun_farki!=0)
        {
            h.son_tarih.setText(gorev_detay.getSontarih());
            h.son_tarih.setTextColor(Color.BLACK);
            h.ischecked.setImageResource(R.drawable.gecersiz);
        }

        else
        {
            h.son_tarih.setText(gorev_detay.getSontarih());
            h.son_tarih.setTextColor(Color.BLACK);
        }
        if(gorev_detay.isCinsiyet())
        {
            h.resim.setImageResource(R.drawable.kadin);
        }
        else{
           h.resim.setImageResource(R.drawable.erkek1);
        }




       return  convertView;


    }
    public void update(List<Gorev_Detay> detays)
    {
        this.gelen_gorevler=detays;
        notifyDataSetChanged();
    }

    class holder{
        TextView gorevlendirem_ad;
        ImageView resim;
        TextView son_tarih;
        TextView gorev_aciklama;
       // CheckBox checkBox;
        ImageView ischecked;

    }
}
