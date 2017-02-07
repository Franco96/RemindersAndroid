package com.apress.gerber.reminders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.apress.gerber.reminders.R.*;

public class RemindersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);

        ListView mListView = (ListView) findViewById(id.reminders_list_view);

        //The arrayAdatper is the controller in our
        // model-view-controller relationship. (controller)
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), layout.reminders_row, id.row_text, new String[]{"first record", "second record", "third record"});
        mListView.setAdapter(arrayAdapter);
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
            case id.action_new:
                //create new Reminder
                Log.d(getLocalClassName(), "create new Reminder");
                return true;
            case id.action_exit:
                finish();
                return true;
            default:
                return false;    }



    }


}

