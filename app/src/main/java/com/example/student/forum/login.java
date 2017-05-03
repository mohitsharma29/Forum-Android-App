package com.example.student.forum;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            createDefaults();
            createPosts();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        Button login = (Button) findViewById(R.id.login);
        Button newAcc = (Button) findViewById(R.id.newAcc);
        //Button guest = (Button) findViewById(R.id.guest);
        //Button forgot = (Button) findViewById(R.id.forgot);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                String user_data = username.getText().toString().trim();
                String pass_data = password.getText().toString().trim();
                if(Login(user_data,pass_data) == 1)
                {
                    Intent some = new Intent(login.this, feed.class);
                    some.putExtra("username" , user_data);
                    startActivity(some);
                }
                else
                {
                    Toast.makeText(login.this,"Incorrect Username or password.",Toast.LENGTH_LONG).show();
                }
            }
        });

        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, new_acc.class);
                startActivity(i);
            }
        });

        /*forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, forgot_pass.class);
                startActivity(i);
            }
        });*/
    }

    public void createDefaults() {
        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ForumContract.LoginEntry.COLUMN_USERNAME, "admin");
        values.put(ForumContract.LoginEntry.COLUMN_PASSWROD, "admin");
        values.put(ForumContract.LoginEntry.COLUMN_EMAIL, "admin@admin.com");

        long insert_id = db.insert(ForumContract.LoginEntry.TABLE_NAME,null,values);
        db.close();
    }
    public int Login(String user_data, String pass_data) {
        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from " + ForumContract.LoginEntry.TABLE_NAME + " where username= \"" + user_data + "\" and password= \"" + pass_data + "\";", null);
            c.moveToFirst();
            i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createPosts(){
        ForumDbHelper forum = new ForumDbHelper(this);
        SQLiteDatabase db = forum.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        String randomText = "Lorem ispum domles ruus paala " +
                "Lorem ispum domles ruus paala Lorem ispum " +
                "domles ruus paala Lorem ispum domles ruus paala " +
                "Lorem ispum domles ruus paala Lorem ispum domles ruus paala " +
                "Lorem ispum domles ruus paala Lorem ispum domles ruus paala " +
                "Lorem ispum domles ruus paala Lorem ispum domles ruus paala " +
                "Lorem ispum domles ruus paala Lorem ispum domles ruus paala " +
                "Lorem ispum domles ruus paala Lorem ispum domles ruus paala ";
        ContentValues values = new ContentValues();
        values.put(ForumContract.ForumEntry.COLUMN_DATE, strDate);
        values.put(ForumContract.ForumEntry.COLUMN_USER, "admin");
        values.put(ForumContract.ForumEntry.COLUMN_COMMENTS,"Some comment 1\nsome comment\nsome comment 3\n");
        values.put(ForumContract.ForumEntry.COLUMN_LIKES, 1000);
        values.put(ForumContract.ForumEntry.COLUMN_REPORT, 0);
        values.put(ForumContract.ForumEntry.COLUMN_POST, randomText);
        values.put(ForumContract.ForumEntry.COLUMN_TAGS, "admin,god,BML");
        values.put(ForumContract.ForumEntry.COLUMN_TITLE, "God Entry 1");
        values.put(ForumContract.ForumEntry.COLUMN_STATUS, 1);
        long insert_id = db.insert(ForumContract.ForumEntry.TABLE_NAME,null,values);
        db.close();
    }
}
