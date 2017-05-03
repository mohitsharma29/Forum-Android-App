package com.example.student.forum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

public class forgot_pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        Button sendIt = (Button) findViewById(R.id.sendIt);

        sendIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.email);
                EditText username = (EditText) findViewById(R.id.username);

                ForumDbHelper helper = new ForumDbHelper(forgot_pass.this);
                SQLiteDatabase db = helper.getReadableDatabase();

                String project[] = {
                        ForumContract.LoginEntry._ID,
                        ForumContract.LoginEntry.COLUMN_EMAIL,
                        ForumContract.LoginEntry.COLUMN_USERNAME,
                        ForumContract.LoginEntry.COLUMN_PASSWROD
                };

                String selection = ForumContract.LoginEntry.COLUMN_EMAIL + "=? AND " + ForumContract.LoginEntry.COLUMN_USERNAME + "=?";

                String selectionArgs[] = {email.getText().toString().trim(),username.getText().toString().trim()};
                Cursor cursor = db.query(ForumContract.LoginEntry.TABLE_NAME,project,selection,selectionArgs,null,null,null);

                try {
                    String pass = "";
                    String user = "";
                    String my_email = "";
                    int userColumnIndex = cursor.getColumnIndex(ForumContract.LoginEntry.COLUMN_USERNAME);
                    int passColumnIndex = cursor.getColumnIndex(ForumContract.LoginEntry.COLUMN_PASSWROD);
                    int emailColumnIndex = cursor.getColumnIndex(ForumContract.LoginEntry.COLUMN_EMAIL);
                    while(cursor.moveToNext())
                    {
                        pass = cursor.getString(passColumnIndex);
                        user = cursor.getString(userColumnIndex);
                        my_email = cursor.getString(emailColumnIndex);
                        Intent emailf = new Intent(Intent.ACTION_SEND);
                        emailf.setData(Uri.parse("mailto:"));
                        emailf.setType("text/plain");
                    }
                } finally {
                    cursor.close();
                }
            }
        });
    }
}
