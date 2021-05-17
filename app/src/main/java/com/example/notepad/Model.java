package com.example.notepad;

import java.util.ArrayList;

public class Model {
    private static Model ourInstance;
    static Model getInstance()
    {
        if (ourInstance == null) {
            ourInstance = new Model();
        }
        return ourInstance;
    }

    ArrayList<Note> notes;
    int uniqueID;

    Model() {
        notes = new ArrayList<>();
        uniqueID = 0;
    }

    void addNote() {
        Note n = new Note();
        notes.add(n);
    }

    Note at(int i) {
        return notes.get(notes.size()-1-i);
    }

    void remove(int i) {
        notes.remove(notes.size()-1-i);
    }


    void fakeNote() {
            for(int i = 0; i < 10000; ++i) {
                Note n = new Note();
                n.title = "Note #" + i;
                n.content = "hello ji " + (i+1);
                notes.add(n);
            }

    }

    public static class Note {
        CharSequence title;
        CharSequence content;
        String FileName;
        Note() {
            title = "";
            content = "";
            FileName = "";
        }
    }
}
