package com.example.student.forum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

public class post_detail extends AppCompatActivity {

    private int likeRef;
    private int reportRef;
    private String comments;
    private String theDate;
    private int likeFlag = 0;
    private int reportFlag = 0;

    public void setDate(String a)
    {
        theDate = a;
    }

    public String getDate()
    {
        return theDate;
    }

    public void setLikeFlag()
    {
        likeFlag = 1;
    }

    public int getLikeFlag()
    {
        return likeFlag;
    }

    public void setReportFlag()
    {
        reportFlag = 1;
    }

    public int getReportFlag()
    {
        return reportFlag;
    }

    public void setLikeRef(int a)
    {
        likeRef = a;
    }

    public int getLikeRef()
    {
        return likeRef;
    }

    public void setReportRef(int a)
    {
        reportRef = a;
    }

    public int getReportRef()
    {
        return reportRef;
    }

    public void setComments(String a)
    {
        comments = a;
    }

    public String getComments()
    {
        return comments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String date = "";
        if(bd != null)
        {
            String fdate = (String) bd.get("date");
            date = fdate;
        }

        TextView title = (TextView) findViewById(R.id.title);
        TextView user = (TextView) findViewById(R.id.user);
        TextView datef = (TextView) findViewById(R.id.date);
        TextView tags = (TextView) findViewById(R.id.tags);
        TextView post = (TextView) findViewById(R.id.post);
        final TextView numLikes = (TextView) findViewById(R.id.numLikes);

        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String projection[] = {
                ForumContract.ForumEntry.COLUMN_TITLE,
                ForumContract.ForumEntry.COLUMN_USER,
                ForumContract.ForumEntry.COLUMN_DATE,
                ForumContract.ForumEntry.COLUMN_REPORT,
                ForumContract.ForumEntry.COLUMN_COMMENTS,
                ForumContract.ForumEntry.COLUMN_LIKES,
                ForumContract.ForumEntry.COLUMN_TAGS,
                ForumContract.ForumEntry.COLUMN_POST
        };

        String selection = ForumContract.ForumEntry.COLUMN_DATE + "=?";
        String[] selectionArgs = {date};
        Cursor cursor = db.query(ForumContract.ForumEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,null);
        try {
            int userColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_USER);
            int dateColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_DATE);
            int titleColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_TITLE);
            int reportColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_REPORT);
            int commentColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_COMMENTS);
            int likesColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_LIKES);
            int tagsColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_TAGS);
            int postColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_POST);
            while(cursor.moveToNext())
            {
                String userID = cursor.getString(userColumnIndex);
                String postDate = cursor.getString(dateColumnIndex);
                setDate(postDate);
                String postTitle = cursor.getString(titleColumnIndex);
                int reportPost = cursor.getInt(reportColumnIndex);
                setReportRef(reportPost);
                String commentPost = cursor.getString(commentColumnIndex);
                setComments(commentPost);
                int postLikes = cursor.getInt(likesColumnIndex);
                setLikeRef(postLikes);
                String postTags = cursor.getString(tagsColumnIndex);
                String mainPost = cursor.getString(postColumnIndex);
                if(reportPost == 5)
                {
                    setTitle("Reported Post");
                }
                else
                {
                    setTitle(postTitle);
                    user.append(userID);
                    title.setText(postTitle);
                    datef.setText(postDate);
                    tags.append(postTags);
                    post.setText(mainPost);
                    numLikes.append("" + postLikes);
                    String items[] = commentPost.split("\n");
                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                    ListView listView = (ListView) findViewById(R.id.commList);
                    listView.setAdapter(itemsAdapter);
                }
            }
        } finally {
            cursor.close();
        }

        final Button like = (Button) findViewById(R.id.like);
        final Button report = (Button) findViewById(R.id.report);
        Button comIt = (Button) findViewById(R.id.comIt);
        final EditText comment = (EditText) findViewById(R.id.comment);
        final SQLiteDatabase wdb = helper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        final int likeFlag = 0;
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getLikeFlag() == 0) {
                    values.put(ForumContract.ForumEntry.COLUMN_LIKES, getLikeRef() + 1);
                    String selection = ForumContract.ForumEntry.COLUMN_DATE + "=?";
                    String selectionArgs[] = {getDate()};
                    wdb.update(ForumContract.ForumEntry.TABLE_NAME,values,selection,selectionArgs);
                    TextView newNumLikes = (TextView) findViewById(R.id.numLikes);
                    newNumLikes.setText("Likes: " + Integer.toString(getLikeRef() + 1));
                    setLikeFlag();
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getReportFlag() == 0)
                {
                    values.put(ForumContract.ForumEntry.COLUMN_REPORT, getReportRef() + 1);
                    String selection = ForumContract.ForumEntry.COLUMN_DATE + "=?";
                    String selectionArgs[] = {getDate()};
                    wdb.update(ForumContract.ForumEntry.TABLE_NAME,values,selection,selectionArgs);
                    setReportFlag();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        comIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values.put(ForumContract.ForumEntry.COLUMN_COMMENTS, getComments() + comment.getText().toString().trim() + '\n');
                String selection = ForumContract.ForumEntry.COLUMN_DATE + "=?";
                String selectionArgs[] = {getDate()};
                wdb.update(ForumContract.ForumEntry.TABLE_NAME,values,selection,selectionArgs);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }
}
