package com.example.asus.denem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Spinner_Adapter extends BaseAdapter {
    List<BolumModel> bolumler;
    List<RolModel> roller;
    LayoutInflater inflater;
    boolean flag_b,flag_r;
    public Spinner_Adapter(Activity activity, List<BolumModel> bolumler,List<RolModel> roller,boolean flag_b,boolean flag_r) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bolumler=bolumler;
        this.roller=roller;
        this.flag_b=flag_b;
        this.flag_r=flag_r;
    }

    @Override
    public int getCount() {
        if(flag_b==true && flag_r==false)
        return bolumler.size();
        else
            return  roller.size();
    }

    @Override
    public Object getItem(int position) {
        if(flag_b==true && flag_r==false)
            return bolumler.get(position);
        else
            return  roller.size();
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder h;
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.spinner_item,parent,false);
            h=new holder();
            h.satir=(TextView)convertView.findViewById(R.id.spinner_value);

            convertView.setTag(h);

        }
        else
            h=(holder)convertView.getTag();
        if(flag_b==true && flag_r==false)  {
            BolumModel b = bolumler.get(position);
            h.satir.setText(b.getBolum_adi());
        }
        else
        {
            RolModel r=roller.get(position);
            h.satir.setText(r.getRol_adi());
        }

        return convertView;

    }
    class  holder{
        TextView satir;
    }
}
