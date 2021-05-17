package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    int position;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        model = Model.getInstance();

        EditText titleText = (EditText) findViewById(R.id.title_input);
        titleText.setText(model.at(position).title);
        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                model.at(position).title = s;
            }
        });

        EditText contentText = findViewById(R.id.content_input);
        contentText.setText(model.at(position).content);
        contentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                model.at(position).content = s;
            }
        });

        FloatingActionButton saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(model.at(position).content.toString().isEmpty() &&
                model.at(position).title.toString().isEmpty()) {
            deleteFile(model.at(position).FileName);
            model.remove(position);
        } else {
            String noteTitle = model.at(position).title.toString() + "\n";
            String noteContent = model.at(position).content.toString();
            try (FileOutputStream fos = openFileOutput(model.at(position).FileName, Context.MODE_PRIVATE)) {
                fos.write(noteTitle.getBytes());
                fos.write(noteContent.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
