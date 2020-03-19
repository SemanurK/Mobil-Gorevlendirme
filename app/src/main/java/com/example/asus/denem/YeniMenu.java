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

public class YeniMenu extends BaseAdapter {
    List<ListMenuModel> listmenu;
    LayoutInflater inflater;
    public YeniMenu(Activity activity, List<ListMenuModel> listmenu)
    {
        inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listmenu=listmenu;
    }
    @Override
    public int getCount() {
        return listmenu.size();
    }

    @Override
    public Object getItem(int position) {
        return listmenu.get(position);
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
            convertView=inflater.inflate(R.layout.grid_item,parent,false);
            h=new holder();
            h.menuadi=(TextView)convertView.findViewById(R.id.text1);
            h.menuicon=(ImageView) convertView.findViewById(R.id.img1);

            convertView.setTag(h);

        }
        else
            h=(holder)convertView.getTag();

        ListMenuModel lm=listmenu.get(position);
        switch (lm.getMenuAdi())
        {
            case "Anasayfa":
                h.menuicon.setImageResource(R.drawable.anasayfa);
                h.menuadi.setText(lm.getMenuAdi());
                break;
            case "Profilim":
               h.menuicon.setImageResource(R.drawable.profil);
               h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Görev Ekle":
                h.menuicon.setImageResource(R.drawable.gorevekle);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Görevlerim":
                h.menuicon.setImageResource(R.drawable.gorevlerim);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Görev Arşivi":
                h.menuicon.setImageResource(R.drawable.arsiv3);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "İzin Düzenle":
                h.menuicon.setImageResource(R.drawable.izin);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "İşlemler":
                h.menuicon.setImageResource(R.drawable.islemler);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Kullanıcı Ekle":
               h.menuicon.setImageResource(R.drawable.ekle);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case  "Kullanıcı Sil":
                h.menuicon.setImageResource(R.drawable.sil);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Kullanıcı Güncelle":
                h.menuicon.setImageResource(R.drawable.duzenle);
                h.menuadi.setText(lm.getMenuAdi());

                break;
           case "Kullanıcı Listele":
                h.menuicon.setImageResource(R.drawable.listele);
                h.menuadi.setText(lm.getMenuAdi());

               break;
            case "Görev Ayarları":
               h.menuicon.setImageResource(R.drawable.rol);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Yeni Eklenenler":
                h.menuicon.setImageResource(R.drawable.yenikayit);
                h.menuadi.setText(lm.getMenuAdi());

                break;
            case "Çıkış Yap":
                h.menuicon.setImageResource(R.drawable.cikisyap);
                h.menuadi.setText(lm.getMenuAdi());

                break;

        }
      //  menuadi.setText(lm.getMenuAdi());

        return convertView;
        /*View  view=null;

        view=inflater.inflate(R.layout.grid_item,null);
        TextView menuadi=(TextView)view.findViewById(R.id.text1);
        ImageView menuicon=(ImageView)view.findViewById(R.id.img1);
        ListMenuModel lm=listmenu.get(position);
        menuadi.setText(lm.getMenuAdi());

        switch (lm.getMenuAdi())
        {
            case "Anasayfa":
                menuicon.setImageResource(R.drawable.anasayfa);
                menuadi.setText(lm.getMenuAdi());
                break;
            case "Profilim":
                menuicon.setImageResource(R.drawable.profil);
                menuadi.setText(lm.getMenuAdi());

                break;
            case "Görev Ekle":
                menuicon.setImageResource(R.drawable.gorevekle);
                menuadi.setText(lm.getMenuAdi());

                break;
            case "Görevlerim":
                menuicon.setImageResource(R.drawable.gorevlerim);
                menuadi.setText(lm.getMenuAdi());

                break;
            case "Görev Arşivi":
                menuicon.setImageResource(R.drawable.arsiv);
                break;
            case "İzin Düzenle":
                menuicon.setImageResource(R.drawable.izin);
                break;
            case "İşlemler":
                menuicon.setImageResource(R.drawable.islemler);
                break;
            case "Kullanıcı Ekle":
                menuicon.setImageResource(R.drawable.ekle);
                break;
            case  "Kullanıcı Sil":
                menuicon.setImageResource(R.drawable.sil);
                break;
            case "Kullanıcı Güncelle":
                menuicon.setImageResource(R.drawable.duzenle);
                break;
            case "Kullanıcı Listele":
                menuicon.setImageResource(R.drawable.listele);
                break;
            case "Görev Ayarları":
                menuicon.setImageResource(R.drawable.rol);
                break;
            case "Yeni Eklenenler":
                menuicon.setImageResource(R.drawable.yenikayit);
                break;
            case "Çıkış Yap":
                menuicon.setImageResource(R.drawable.cikisyap);
                break;

        }

        return view;*/
    }
    class holder{
        TextView menuadi;
        ImageView menuicon;


    }
}
