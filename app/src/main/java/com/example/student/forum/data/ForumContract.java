package com.example.student.forum.data;

import android.provider.BaseColumns;

/**
 * Created by Student on 4/19/2017.
 */

public final class ForumContract {
    private ForumContract(){};

    public static class LoginEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "login";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWROD = "password";
        public static final String COLUMN_EMAIL = "email";
    }

    public static class ForumEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "forum";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_DATE = "datetime";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_COMMENTS = "comments";
        public static final String COLUMN_POST = "post";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_REPORT = "report";
        public static final String COLUMN_STATUS = "status";
    }
}
