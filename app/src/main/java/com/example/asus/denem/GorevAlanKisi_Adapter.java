package com.example.asus.denem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GorevAlanKisi_Adapter extends BaseAdapter {
    List<GorevAlanKisi> liste;
    LayoutInflater inflater;

    public GorevAlanKisi_Adapter(Activity activity, List<GorevAlanKisi> liste) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.liste = liste;
    }

    @Override
    public int getCount() {
        return liste.size();
    }

    @Override
    public Object getItem(int position) {
        return liste.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       /* View  view=null;

        view=inflater.inflate(R.layout.gorev_detay,null);
        TextView ad_soyad=(TextView)view.findViewById(R.id.ga_kul_ad);
        TextView goruldu=(TextView)view.findViewById(R.id.gorulme_tarih);
        TextView onaydlandi=(TextView)view.findViewById(R.id.onaylanma_tarih);
        ImageView resim=(ImageView)view.findViewById(R.id.ga_resim);
        GorevAlanKisi ga_kisi=liste.get(position);
        ad_soyad.setText(ga_kisi.getAd_soyad());
        String o=ga_kisi.getOnaylanma_tarih();
        String g=ga_kisi.getOnaylanma_tarih();
        goruldu.setText(g);
        onaydlandi.setText(o);
        if(ga_kisi.isCinsiyet()==true)
        {
            resim.setImageResource(R.drawable.kadin);
        }
        else
            resim.setImageResource(R.drawable.erkek1);

        return view;*/
       holder h=null;
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.gorev_detay,parent,false);
            h=new holder();
            h.ad_soyad=(TextView)convertView.findViewById(R.id.ga_kul_ad);
            h.gorulme_tarih=(TextView)convertView.findViewById(R.id.gorulme_tarih);
            h.onaylanma_tarih=(TextView)convertView.findViewById(R.id.onaylanma_tarih);
            h.cinsiyet=(ImageView)convertView.findViewById(R.id.ga_resim);
            h.isaret=(ImageView)convertView.findViewById(R.id.ga_durum);
            convertView.setTag(h);
        }
        else
            h=(holder)convertView.getTag();
           GorevAlanKisi ga_kisi=liste.get(position);
           h.ad_soyad.setText(ga_kisi.getAd_soyad());
           h.gorulme_tarih.setText(ga_kisi.getGorulme_tarih());
           h.onaylanma_tarih.setText(ga_kisi.getOnaylanma_tarih());
           if(ga_kisi.isCinsiyet())
           {
               h.cinsiyet.setImageResource(R.drawable.kadin);
           }
           else h.cinsiyet.setImageResource(R.drawable.erkek1);
           if(ga_kisi.isDurum())
           {
               h.isaret.setImageResource(R.drawable.isaret);
           }else h.isaret.setImageResource(R.drawable.isaretsiz);

        return convertView;
    }
    public void update(List<GorevAlanKisi> gorevAlanKisis)
    {
        this.liste=gorevAlanKisis;
        notifyDataSetChanged();
    }
    class holder{

        TextView ad_soyad;
        TextView gorulme_tarih;
        TextView onaylanma_tarih;
        ImageView isaret;
        ImageView cinsiyet;
            }
}
