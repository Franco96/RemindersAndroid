package com.apress.gerber.anotaciones;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lautaro on 08/02/2017.
 */


/*
Esta clase es muy importante ya que es la clase que se necesita para enlazar el modelo con la vista,
es decir que enlaza los valores de la base de datos con la listWiew para que se muestre por pantalla
 */
public class NotasSimpleCursorAdapter extends SimpleCursorAdapter {


    public NotasSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {

        super(context, layout, c, from, to, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }


    //Durante el tiempo de ejecución, ListView invocará repetidamente el método bindView ()
    //en el adaptador con objetos de vista en pantalla individuales a medida que el usuario se cargue y se desplace por la lista.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(NotasBaseDatosAdapter.COL_IMPORTANT);
            holder.listTab =  view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }

        if (cursor.getInt(holder.colImp) > 2)
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.orange));
        else
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.green));

    }


    static class ViewHolder {
        //guarda los indices de las columnas
        int colImp;
        //guarda las  view
         View listTab;
    }

}