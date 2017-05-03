package com.example.student.forum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

public class changePass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Button setNew = (Button) findViewById(R.id.setNewPass);
        final EditText getNew = (EditText) findViewById(R.id.getNew);
        setTitle("Change your Password");

        setNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForumDbHelper helper = new ForumDbHelper(changePass.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(ForumContract.LoginEntry.COLUMN_PASSWROD, getNew.getText().toString().trim());
                long updated = db.update(ForumContract.LoginEntry.TABLE_NAME, values, ForumContract.LoginEntry.COLUMN_USERNAME + "=?" , new String[]{getuser()});

                if(updated == 1)
                {
                    Toast.makeText(changePass.this, "Password changed successfully" , Toast.LENGTH_LONG).show();
                    Intent newI = new Intent(changePass.this, login.class);
                    startActivity(newI);
                    finish();
                }
                else
                {
                    Toast.makeText(changePass.this, "Some error occured, Please try again later" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String getuser()
    {
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String username = "";
        if(bd != null)
        {
            String name = (String) bd.get("username");
            username = name;
        }
        return username;
    }
}
