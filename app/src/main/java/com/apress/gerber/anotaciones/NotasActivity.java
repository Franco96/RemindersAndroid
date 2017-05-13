package com.apress.gerber.anotaciones;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import static com.apress.gerber.anotaciones.R.*;

public class NotasActivity extends AppCompatActivity {
    //Atributos
    private ListView mListView;
    private NotasBaseDatosAdapter notasBaseDatosAdapter;
    private NotasSimpleCursorAdapter cursorAdapter;
    private UseDialog mostrarDialog;
    public Toolbar toolbar;
    public Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotaciones);

        init();
        clickEnListWiewGeneral();
    }

    //............Inicia atributos de la aplicacion............//
    private void init()
    {
        //Inicializamos la toolbar seteandole un icono y el id
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        //Creamos la lista para las anotaciones
        mListView = (ListView) findViewById(id.reminders_list_view);
        mListView.setDivider(null);

        //Creamos la base de datos adaptador
        notasBaseDatosAdapter = new NotasBaseDatosAdapter(this);
        notasBaseDatosAdapter.open();

        //cursor
        cursor = notasBaseDatosAdapter.fetchAllReminders();
        String[] from = new String[]{NotasBaseDatosAdapter.COL_CONTENT};
        //Aca tenemos los ids de vista del diseño
        int[] to = new int[]{R.id.row_text};
        cursorAdapter = new
                NotasSimpleCursorAdapter(
                //context
                NotasActivity.this,
                //the layout of the row
                R.layout.anotaciones_row,
                //cursor
                cursor,
                //Le paso el contenido de la base de dato
                from,
                //ids
                to,
                //flag - not used
                0);

        //El cursosAdaoter ahora actualiza la listWiew con los datos de la base de datos
        mListView.setAdapter(cursorAdapter);

        //Creamos el objeto que nos ayudara a mostrar los paneles de dialogo
        mostrarDialog = new UseDialog(this,notasBaseDatosAdapter,cursorAdapter);
    }



    private void clickEnListWiewGeneral()
    {
        //Metodo para cuando hacemos click en un item individual de la listView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                          @Override
                                          public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {

                                                 //Lista secundaria para que contenga las opciones que se muestran al hacer click
                                                 ListView modeListView = new ListView(NotasActivity.this); //Crearemos un dialogo que se muestre cuando se hace click
                                                 AlertDialog.Builder builder = new AlertDialog.Builder(NotasActivity.this);
                                                 String[] modes = new String[]{"Ver ranking", "Editar Nota", "Eliminar Nota"};
                                                 //Creamos el adaptador para enlazarlo a la lista
                                                 ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(NotasActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                                                 //Enlazamos
                                                 modeListView.setAdapter(modeAdapter);
                                                 //mostramos lista
                                                 builder.setView(modeListView);
                                                 final Dialog dialog = builder.create();
                                                 dialog.show();
                                                 //llamamos al metodo que se encarga de configurar la lista secundaria cuando hacemos click en un item
                                                 clickEnListWiewSecundaria(modeListView,masterListPosition,dialog);
                                             }
                                         });
    }

    private void clickEnListWiewSecundaria(ListView modeListView,final int masterListPosition,final Dialog dialog)
    {
        // Metodo para cuando hacemos click en un item individual de la lista secundaria
        modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int nId = getIdFromPosition(masterListPosition);
                Notas notas = notasBaseDatosAdapter.fetchReminderById(nId);

                switch (position) {
                    case 0:
                        //Ver ranking de estrellas
                        mostrarDialog.mostrarRankingEstrellas(notas);
                        break;
                    case 1:
                        //Editar nota
                        mostrarDialog.customDialogCascara(notas);
                        break;
                    case 2:
                        //Eliminar nota
                        notasBaseDatosAdapter.deleteReminderById(nId);
                        cursorAdapter.changeCursor(notasBaseDatosAdapter.fetchAllReminders());
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    //.............Obtener Id de una nota de la ListWiew...........//
    private int getIdFromPosition(int nC)
    {
        return (int) cursorAdapter.getItemId(nC);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                //crea nueva Nota
                 mostrarDialog.customDialogCascara(null);
                return true;
            case R.id.action_exit:
                 finish();
                 return true;
            default:
                return false;
        }
    }

}

