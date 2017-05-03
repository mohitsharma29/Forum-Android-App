package com.example.student.forum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String username = "";
        if(bd != null)
        {
            String name = (String) bd.get("username");
            username = name;
        }

        ForumDbHelper helper = new ForumDbHelper(profile.this);
        SQLiteDatabase db = helper.getReadableDatabase();

        TextView showUser = (TextView) findViewById(R.id.showUser);
        TextView showEmail = (TextView) findViewById(R.id.showEmail);

        String project[] = {
                ForumContract.LoginEntry._ID,
                ForumContract.LoginEntry.COLUMN_EMAIL,
                ForumContract.LoginEntry.COLUMN_USERNAME,
        };

        String selection = ForumContract.LoginEntry.COLUMN_USERNAME + "=?";

        String selectionArgs[] = {username};
        Cursor cursor = db.query(ForumContract.LoginEntry.TABLE_NAME,project,selection,selectionArgs,null,null,null);

        try {
            String user = "";
            String my_email = "";
            int emailColumnIndex = cursor.getColumnIndex(ForumContract.LoginEntry.COLUMN_EMAIL);
            while(cursor.moveToNext())
            {
                my_email = cursor.getString(emailColumnIndex);
                showUser.setText(username);
                showEmail.setText(my_email);
            }
        } finally {
            cursor.close();
        }
    }
}
