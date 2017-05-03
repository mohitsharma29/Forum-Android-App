package com.example.student.forum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.post_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertData(1);
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_draft:
                // Do nothing for now
                insertData(0);
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertData(int status)
    {
        EditText title = (EditText) findViewById(R.id.title);
        EditText post = (EditText) findViewById(R.id.post);
        EditText tags = (EditText) findViewById(R.id.tags);

        String head = title.getText().toString().trim();
        String postData = post.getText().toString().trim();
        String postTag = tags.getText().toString().toLowerCase().trim();

        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ForumContract.ForumEntry.COLUMN_STATUS , status);
        values.put(ForumContract.ForumEntry.COLUMN_TITLE, head);
        values.put(ForumContract.ForumEntry.COLUMN_TAGS, postTag);
        values.put(ForumContract.ForumEntry.COLUMN_REPORT, 0);
        values.put(ForumContract.ForumEntry.COLUMN_COMMENTS, "");
        values.put(ForumContract.ForumEntry.COLUMN_DATE, getDate());
        values.put(ForumContract.ForumEntry.COLUMN_LIKES, 0);
        values.put(ForumContract.ForumEntry.COLUMN_USER, getUser());
        values.put(ForumContract.ForumEntry.COLUMN_POST, postData);

        long insert_id = db.insert(ForumContract.ForumEntry.TABLE_NAME, null, values);
        db.close();

        if(insert_id != -1)
        {
            Toast.makeText(Editor.this, "Posted!!" , Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(Editor.this, "Error posting."  , Toast.LENGTH_LONG).show();
        }
    }

    public String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        return strDate;
    }

    public String getUser()
    {
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String name = null;
        if(bd != null) {
            name = (String) bd.get("username");
        }
        return name;
    }
}
