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

public class IzinAdapter extends BaseAdapter {
    List<IzinModel> list;
    LayoutInflater inflater;
    public IzinAdapter(Activity activity, List<IzinModel> list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView=inflater.inflate(R.layout.rol_item,parent,false);
            h=new holder();
            h.menuad=(TextView)convertView.findViewById(R.id.menu_ad);

            h.ischecked=(ImageView)convertView.findViewById(R.id.izin_durum);
            convertView.setTag(h);

        }
        else
            h=(holder)convertView.getTag();
        IzinModel izinler=list.get(position);
        h.menuad.setText(izinler.getMenuAdi());

        if(izinler.isDurum())
        {
            h.ischecked.setImageResource(R.drawable.isaret);
        }
        else
            h.ischecked.setImageResource(R.drawable.isaretsiz);


        return convertView;
    }
    public void update(List<IzinModel> detays)
    {
        this.list=detays;
        notifyDataSetChanged();
    }

}

class holder{
    TextView menuad;
    ImageView ischecked;

}
