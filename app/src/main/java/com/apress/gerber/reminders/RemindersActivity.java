package com.apress.gerber.reminders;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import static com.apress.gerber.reminders.R.*;


public class RemindersActivity extends AppCompatActivity {

    private ListView mListView;
    private RemindersDbAdapter mDbAdapter;
    private RemindersSimpleCursorAdapter mCursorAdapter;



    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(id.reminders_list_view);
        mListView.setDivider(null);

        mDbAdapter = new RemindersDbAdapter(this);
        mDbAdapter.open();




        //when we click an individual item in the listview
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RemindersActivity.this);
                ListView modeListView = new ListView(RemindersActivity.this);
                String[] modes = new String[]{"Edit Reminder","Delete Reminder"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(RemindersActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //edit reminder
                          if (position == 0) {
                              int nId = getIdFromPosition(masterListPosition);
                              Reminder reminder = mDbAdapter.fetchReminderById(nId);
                              fireCustomDialog(reminder);
                              //delete reminder

                          } else {

                                  mDbAdapter.deleteReminderById(getIdFromPosition(masterListPosition));
                                  mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());

                          }
                        dialog.dismiss();
                    }
                });
            }
        });


        Cursor cursor = mDbAdapter.fetchAllReminders();

        //from columns defined in the db
        String[] from = new String[]{RemindersDbAdapter.COL_CONTENT};


        //to the ids of views in the layout
        int[] to = new int[]{R.id.row_text};

        mCursorAdapter = new

                RemindersSimpleCursorAdapter(
                //context
                RemindersActivity.this,
                //the layout of the row
                R.layout.reminders_row,
                //cursor
                cursor,
                //from columns defined in the db
                from,
                //to the ids of views in the layout
                to,
                //flag - not used
                0);


        // the cursorAdapter (controller) is now updating the listView (view)
        // with data from the db (model)
        mListView.setAdapter(mCursorAdapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {


            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {



                @Override
                public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                    return false;
                }


                @Override
                public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_reminder:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminderById(getIdFromPosition(nC));
                                }
                            }

                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(android.view.ActionMode mode) {

                }

                @Override
                public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {

                }

            });
        }
    }

    private int getIdFromPosition(int nC) {
        return (int)mCursorAdapter.getItemId(nC);
    }






    private void fireCustomDialog(final Reminder reminder){
        // custom dialog
           final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_custom);

        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);

        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);

        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);

        final RatingBar rangoEstrellas = (RatingBar) dialog.findViewById(R.id.ratingBar);

        final TextView textoCantEstrellas = (TextView) dialog.findViewById(R.id.cant);

        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);


        //Actualiza poniendo el metodo aca a lo primero

        rangoEstrellas.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        textoCantEstrellas.setText(String.valueOf(rating));


                    }
                }
        );





        final boolean isEditOperation = (reminder != null);
        //this is for an edit
        if (isEditOperation){
            titleView.setText("Edit Reminder");

            rangoEstrellas.setRating(reminder.getImportant());

            editCustom.setText(reminder.getContent());

            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }

        commitButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  final String reminderText = editCustom.getText().toString();

                  if (isEditOperation) {

                      Reminder reminderEdited = new Reminder(reminder.getId(),reminderText, Float.parseFloat(textoCantEstrellas.getText().toString()));

                      mDbAdapter.updateReminder(reminderEdited);

                  }
                  else {


                      mDbAdapter.createReminder(reminderText, Float.parseFloat(textoCantEstrellas.getText().toString()));

                      }
                      mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                      dialog.dismiss();
              }    });


              Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
              buttonCancel.setOnClickListener(new View.OnClickListener() {        @Override        public void onClick(View v) {            dialog.dismiss();        }    });
            dialog.show();
    }









    //Remainder of the class listing omitted for brevity
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
                //create new Reminder
                fireCustomDialog(null);
                return true;
             case R.id.action_exit:
                 finish();
                 return true;
            default:            return false;
        }


    }


}

