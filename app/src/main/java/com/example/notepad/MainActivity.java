package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.util.ArrayList;
import static java.lang.Math.min;


public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteListener {
    RecyclerView recyclerView;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = Model.getInstance();
        int length = fileList().length;
        model.uniqueID = length;
        //////////////loads files to model///////////////////
        int start = 0;
        if(length > 10) {
            start = length - 10;
            new LoadFiles().execute(fileList());
        }
        for(int i = start; i < length; ++i) {
            try {
                FileInputStream fis = openFileInput("Note#"+i);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                String lines;
                model.addNote();
                model.at(0).title = reader.readLine();
                while ((lines = reader.readLine()) != null) {
                    stringBuffer.append(lines + "\n");
                }
                model.at(0).content = stringBuffer.toString();
                model.at(0).FileName = "Note#"+i;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ///////////////////////////////////////////////////
        recyclerView = findViewById(R.id.recyclerview);
        NoteAdapter noteAdapter = new NoteAdapter(model,this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        FloatingActionButton newButton = findViewById(R.id.new_note_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.addNote();
                model.at(0).FileName = "Note#"+model.uniqueID;
                try (FileOutputStream fos = openFileOutput("Note#"+model.uniqueID, Context.MODE_PRIVATE)) {
                    model.uniqueID+=1;
                    fos.write("".getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView != null) recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int len = model.notes.size();
        for(int i = 0; i < len; ++i) {
            if(i != Integer.parseInt(model.notes.get(i).FileName.substring(5))) {
                File oldFile = new File(getFilesDir(), model.notes.get(i).FileName);
                File newFile = new File(getFilesDir(), "Note#"+i);
                oldFile.renameTo(newFile);
            }
        }
        model.notes.clear();
        model.uniqueID = 0;
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(int position) {
        deleteFile(model.at(position).FileName);
        model.remove(position);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private class LoadFiles extends AsyncTask<String, Void, ArrayList<Model.Note>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Model.Note> doInBackground(String... files) {
            ArrayList<Model.Note> toBeLoaded = new ArrayList<>();
            int len = files.length - 10;
            for(int i = 0; i < len; ++i) {
                try {
                    FileInputStream fis = openFileInput("Note#"+i);
                    InputStreamReader inputStreamReader = new InputStreamReader(fis);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    String lines;
                    Model.Note n = new Model.Note();
                    n.title = reader.readLine();
                    while ((lines = reader.readLine()) != null) {
                        stringBuffer.append(lines + "\n");
                    }
                    n.content = stringBuffer.toString();
                    n.FileName = "Note#"+i;
                    toBeLoaded.add(n);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return toBeLoaded;
        }

        @Override
        protected void onPostExecute(ArrayList<Model.Note> newNotes) {
            super.onPostExecute(newNotes);
            for(int i = 0; i < model.notes.size(); ++i) {
                newNotes.add(model.notes.get(i));
            }
            model.notes = newNotes;
            recyclerView.getAdapter().notifyDataSetChanged();
        }
}
}
