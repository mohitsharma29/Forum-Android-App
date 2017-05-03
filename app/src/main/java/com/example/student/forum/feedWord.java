package com.example.student.forum;

/**
 * Created by Student on 4/29/2017.
 */

public class feedWord {
    private String question;
    private String username;
    private String date;

    public feedWord(String fquestion, String fusername, String fdate)
    {
        question = fquestion;
        username = fusername;
        date = fdate;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getUsername()
    {
        return username;
    }

    public String getDate()
    {
        return date;
    }
}
