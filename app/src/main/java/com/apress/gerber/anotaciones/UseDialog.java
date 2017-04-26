package com.apress.gerber.anotaciones;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//clase utilizada por la actividad principal para mostrar paneles de dialogos dependiedo
// del servicio de dialogo que el cliente quiera
public class UseDialog {

    //ATRIBUTOS

      private NotasActivity a;
      private NotasBaseDatosAdapter notasBaseDatosAdapter;
      private NotasSimpleCursorAdapter cursorAdapter;
    //atributos de interaccion con el usuario
      private  Dialog dialog;
      private  RatingBar rangoEstrellas ;
      private  TextView textoCantEstrellas;
      private  TextView tituloDialogNota;
      private  EditText editCustom;
      Button  commitButton;
      Button  buttonCancel;
      LinearLayout rootLayout;



    //constructor

    public UseDialog(NotasActivity activity, NotasBaseDatosAdapter baseDatosAdapter, NotasSimpleCursorAdapter cursorAdapter)
    {
        a = activity;
        this.notasBaseDatosAdapter = baseDatosAdapter;
        this.cursorAdapter = cursorAdapter;

        dialog = new Dialog(a);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public void mostrarRankingEstrellas(final Notas notas) {

        //Ranking que aparece en el dialogo
        dialog.setContentView(R.layout.rango_estrellas);
        rangoEstrellas = (RatingBar) dialog.findViewById(R.id.ratingBar);
        rangoEstrellas.setEnabled(false);
        textoCantEstrellas = (TextView) dialog.findViewById(R.id.cant);
        rangoEstrellas.setRating(notas.getImportant());
        textoCantEstrellas.setText(String.valueOf(notas.getImportant()));
        dialog.show();
    }

    public void customDialogCascara(Notas notas) {

        //..........INICIALIZAMOS LAYOUT DE CUSTOM_DIALOG.....................//
        //custom dialog
        dialog.setContentView(R.layout.dialog_custom);
        //Titulo del dialogo
         tituloDialogNota = (TextView) dialog.findViewById(R.id.custom_title);
        //Campo de la anotacion del dialogo
          editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        //Botones
         commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
          buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        //Ranking que aparece en el dialogo
         rangoEstrellas = (RatingBar) dialog.findViewById(R.id.ratingBar);
         textoCantEstrellas = (TextView) dialog.findViewById(R.id.cant);
        //Layout del dialogo
         rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        //Creamos un booleano para verificar si es un texto editable o una nueva nota
         boolean isEditOperation = (notas != null);
        //.........................................................................//

        //Aca tenemos el rango de estrellas cuando clickeamos
        rangoEstrellas.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        textoCantEstrellas.setText(String.valueOf(rating));
                    }
                }
        );
        customDialog(isEditOperation,notas);
    }

private  void customDialog(final boolean isEditOperation ,final Notas notas)
{
       //si es editable
        if (isEditOperation){
            //Actualizamos lo que ya tenia la nota ya que es editable y debemos cargar lo que ya tenia para editarla
            tituloDialogNota.setText(R.string.EditNota);
            rangoEstrellas.setRating(notas.getImportant());
            editCustom.setText(notas.getContent());
            rootLayout.setBackgroundColor(a.getResources().getColor(R.color.blue));
        }

        //Cuando presionamos el boton de aceptar
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String reminderText = editCustom.getText().toString();

                if (isEditOperation) {

                    //Actualizamos la nota editada
                    Notas notasEdited = new Notas(notas.getId(),reminderText, Float.parseFloat(textoCantEstrellas.getText().toString()));
                    notasBaseDatosAdapter.updateReminder(notasEdited);
                }
                else {

                    //sino es editable creamos una nueva nota con la informacion que paso el usuario en los campos
                    notasBaseDatosAdapter.createReminder(reminderText, Float.parseFloat(textoCantEstrellas.getText().toString()));
                    Toast.makeText(a.getApplicationContext(),"Nota creada",Toast.LENGTH_SHORT).show();
                }
                //Debemos actualizar el cursor ya que la base de datos cambio al agregar una nueva nota o editarla
                cursorAdapter.changeCursor(notasBaseDatosAdapter.fetchAllReminders());
                dialog.dismiss();
            }    });
        //Boton cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override        public void onClick(View v) {dialog.dismiss(); }
        });

        dialog.show();
}



}
