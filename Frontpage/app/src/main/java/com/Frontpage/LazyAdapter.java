package com.Frontpage;


/**
 * Created by luciano on 19/12/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

    String [] titles;
    String [] details;
    Context context;
    String [] images;
    private static LayoutInflater inflater=null;

    public LazyAdapter(MainActivity mainActivity, String[] Titulo, String[] Detalhes, String [] ImageURL) {
        // TODO Auto-generated constructor stub
        titles =Titulo;
        context=mainActivity;
        images=ImageURL;
        details=Detalhes;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView txtview_titulo;
        TextView txtview_detalhes;
        ImageView imgview;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listview_layout, null);
        holder.txtview_titulo =(TextView) rowView.findViewById(R.id.title);
        holder.txtview_detalhes =(TextView) rowView.findViewById(R.id.news_detail);
        //holder.imgview =(ImageView) rowView.findViewById(R.id.list_image);
        holder.txtview_titulo.setText(titles[position]);
        holder.txtview_detalhes.setText(details[position]);
        //holder.imgview.setImageResource(imageId[position]);

        return rowView;
    }
}
