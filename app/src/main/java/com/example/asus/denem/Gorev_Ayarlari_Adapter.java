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

public class Gorev_Ayarlari_Adapter extends BaseAdapter {
    List<Gorev_Detay2> gorev_detay2List;
    LayoutInflater inflater;

    public Gorev_Ayarlari_Adapter(Activity activity, List<Gorev_Detay2> gorev_detay2List) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gorev_detay2List = gorev_detay2List;
    }
    @Override
    public int getCount() {
        return gorev_detay2List.size();
    }

    @Override
    public Object getItem(int position) {
        return gorev_detay2List.get(position);
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
            convertView=inflater.inflate(R.layout.gorev_duzenle_satir,parent,false);
            h=new holder();
            h.gorev_aciklama=(TextView)convertView.findViewById(R.id.gorev_ayarlari_aciklama);
           // h.duzenle=(Button)convertView.findViewById(R.id.gorev_duzenle);
            h.son_tarih=(TextView)convertView.findViewById(R.id.gorev_tarih);
            h.ischecked=(ImageView)convertView.findViewById(R.id.gorev_durum);
            convertView.setTag(h);
        }
        else
            h=(holder)convertView.getTag();

            Gorev_Detay2 gorev_detay2=gorev_detay2List.get(position);
        if(gorev_detay2.isGorev_durum())
        {
            h.ischecked.setImageResource(R.drawable.isaret);
           // h.duzenle.setText("tiklandi");
        }
        else
            h.ischecked.setImageResource(R.drawable.isaretsiz);

            h.gorev_aciklama.setText(gorev_detay2.getGorev_aciklama());
            h.son_tarih.setText(gorev_detay2.getSon_tarih());



       return convertView;
    }
    class holder{

        TextView son_tarih;
        TextView gorev_aciklama;
        Button duzenle;
        ImageView ischecked;

    }
    public void update(List<Gorev_Detay2> detays)
    {
        this.gorev_detay2List=detays;
        notifyDataSetChanged();
    }
}
