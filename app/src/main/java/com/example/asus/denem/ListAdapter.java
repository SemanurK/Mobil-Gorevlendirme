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

public class ListAdapter extends BaseAdapter {
    List<ListMenuModel> listmenu;
    LayoutInflater inflater;
    public ListAdapter(Activity activity,List<ListMenuModel> listmenu)
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
        View  view=null;

        view=inflater.inflate(R.layout.menu_satir,null);
        TextView menuadi=(TextView)view.findViewById(R.id.menuadi);
        ImageView menuicon=(ImageView)view.findViewById(R.id.menuicon);
        ListMenuModel lm=listmenu.get(position);
        menuadi.setText(lm.getMenuAdi());

        switch (lm.getMenuAdi())
        {
            case "Anasayfa":
                menuicon.setImageResource(R.drawable.anasayfa);
                break;
            case "Profilim":
                menuicon.setImageResource(R.drawable.profil);
                break;
            case "Görev Ekle":
                menuicon.setImageResource(R.drawable.gorevekle);
                break;
            case "Görevlerim":
                menuicon.setImageResource(R.drawable.gorevlerim);
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

        return view;
    }
}
