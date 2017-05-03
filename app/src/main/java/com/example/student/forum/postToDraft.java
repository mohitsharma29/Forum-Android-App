package com.example.student.forum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class postToDraft extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_draft);

        EditText title = (EditText) findViewById(R.id.title);
        EditText post = (EditText) findViewById(R.id.post);
        EditText tags = (EditText) findViewById(R.id.tags);

        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String projection[] = {
                ForumContract.ForumEntry.COLUMN_TITLE,
                ForumContract.ForumEntry.COLUMN_TAGS,
                ForumContract.ForumEntry.COLUMN_POST
        };

        String selection = ForumContract.ForumEntry.COLUMN_DATE + "=?";
        String[] selectionArgs = {getDate()};
        Cursor cursor = db.query(ForumContract.ForumEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,null);
        try {
            int titleColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_TITLE);
            int tagsColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_TAGS);
            int postColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_POST);
            while(cursor.moveToNext())
            {
                String postTitle = cursor.getString(titleColumnIndex);
                String postTags = cursor.getString(tagsColumnIndex);
                String mainPost = cursor.getString(postColumnIndex);
                title.setText(postTitle);
                post.setText(mainPost);
                tags.setText(postTags);
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.draft_post_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                updateData(1);
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_draft:
                // Do nothing for now
                updateData(0);
                return true;
            case R.id.action_discard:
                deletePost();
                finish();
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateData(int status)
    {
        EditText title = (EditText) findViewById(R.id.title);
        EditText post = (EditText) findViewById(R.id.post);
        EditText tags = (EditText) findViewById(R.id.tags);

        ForumDbHelper helper = new ForumDbHelper(postToDraft.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ForumContract.ForumEntry.COLUMN_POST, post.getText().toString().trim());
        values.put(ForumContract.ForumEntry.COLUMN_TAGS, tags.getText().toString().trim());
        values.put(ForumContract.ForumEntry.COLUMN_TITLE, tags.getText().toString().trim());
        values.put(ForumContract.ForumEntry.COLUMN_REPORT,0);
        values.put(ForumContract.ForumEntry.COLUMN_COMMENTS,"");
        values.put(ForumContract.ForumEntry.COLUMN_USER,getUser());
        values.put(ForumContract.ForumEntry.COLUMN_DATE, getNewDate());
        values.put(ForumContract.ForumEntry.COLUMN_LIKES,0);
        values.put(ForumContract.ForumEntry.COLUMN_STATUS,status);
        long updated = db.update(ForumContract.ForumEntry.TABLE_NAME, values, ForumContract.ForumEntry.COLUMN_DATE + "=? AND " + ForumContract.ForumEntry.COLUMN_USER + "=?", new String[]{getDate(),getUser()});

        if(updated != 0)
        {
            Toast.makeText(postToDraft.this,"Successfully posted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(postToDraft.this,"Error posting, please check and try again", Toast.LENGTH_LONG).show();
        }
    }

    public String getDate()
    {
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String date = "";
        if(bd != null)
        {
            String fdate = (String) bd.get("date");
            date = fdate;
        }
        return date;
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

    public String getNewDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        return strDate;
    }

    public void deletePost()
    {
        ForumDbHelper helper = new ForumDbHelper(postToDraft.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String whereClause = ForumContract.ForumEntry.COLUMN_DATE + "=?";
        String[] whereArgs = new String[] {getDate()};
        db.delete(ForumContract.ForumEntry.TABLE_NAME, whereClause, whereArgs);
    }
}
