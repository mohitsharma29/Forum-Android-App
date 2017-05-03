package com.example.student.forum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

import java.util.ArrayList;

public class feed extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Welcome " + getUser());
        ArrayList<feedWord> words = new ArrayList<feedWord>();
        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String projection[] = {
                ForumContract.ForumEntry.COLUMN_TITLE,
                ForumContract.ForumEntry.COLUMN_USER,
                ForumContract.ForumEntry.COLUMN_DATE,
                ForumContract.ForumEntry.COLUMN_STATUS
        };
        String selection = ForumContract.ForumEntry.COLUMN_STATUS + "=?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query(ForumContract.ForumEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,null);
        try {
            int userColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_USER);
            int dateColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_DATE);
            int titleColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_TITLE);
            while(cursor.moveToNext())
            {
                String userID = cursor.getString(userColumnIndex);
                String date = cursor.getString(dateColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                words.add(new feedWord(title,userID,date));
            }
        } finally {
            cursor.close();
        }
        feedAdapter itemsAdapter = new feedAdapter(this,words);
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Intent i = new Intent(feed.this, post_detail.class);
                feedWord selectedFromList = (feedWord)(listView.getItemAtPosition(position));
                i.putExtra("date" , selectedFromList.getDate());
                startActivity(i);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.myPass:
                        Intent i = new Intent(feed.this, changePass.class);
                        i.putExtra("username", getUser());
                        startActivity(i);
                        break;
                    case R.id.drafts:
                        i = new Intent(feed.this, drafts.class);
                        i.putExtra("username", getUser());
                        startActivity(i);
                        break;
                    case R.id.myPost:
                        i = new Intent(feed.this, myPost.class);
                        i.putExtra("username", getUser());
                        startActivity(i);
                }
                return false;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView name;
        name = (TextView)hView.findViewById(R.id.userName);
        name.setText(getUser());
    }

    public String getUser()
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.feed_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_new_post:
                // Do nothing for now
                goEdit();
                return true;
            case R.id.action_logout:
                // Do nothing for now
                goBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goEdit()
    {
        Intent i = new Intent(feed.this, Editor.class);
        i.putExtra("username" , getUser());
        startActivity(i);
    }

    public void goBack()
    {
        Intent i = new Intent(feed.this, login.class);
        i.putExtra("username",getUser());
        startActivity(i);
        finish();
    }
}
