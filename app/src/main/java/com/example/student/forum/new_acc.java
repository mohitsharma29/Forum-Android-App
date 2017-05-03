package com.example.student.forum;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

public class new_acc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_acc);
        Button newBut = (Button) findViewById(R.id.newBut);
        newBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newUser = (EditText) findViewById(R.id.newName);
                EditText newPass = (EditText) findViewById(R.id.newpass);
                EditText newEmail = (EditText) findViewById(R.id.newEmail);

                ForumDbHelper helper = new ForumDbHelper(new_acc.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ForumContract.LoginEntry.COLUMN_USERNAME, newUser.getText().toString().trim());
                values.put(ForumContract.LoginEntry.COLUMN_EMAIL, newEmail.getText().toString().trim());
                values.put(ForumContract.LoginEntry.COLUMN_PASSWROD, newPass.getText().toString().trim());
                long insert_id = db.insert(ForumContract.LoginEntry.TABLE_NAME, null, values);
                if (insert_id != -1)
                {
                    Toast.makeText(new_acc.this, "Account successfully created" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(new_acc.this,"Please try again with different username",Toast.LENGTH_LONG).show();
                }
                db.close();
            }
        });
    }
}
