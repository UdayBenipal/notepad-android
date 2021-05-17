package com.example.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.NoteViewHolder> {
    Model model;
    OnNoteListener monNoteListener;

    public NoteAdapter(Model model, OnNoteListener onNoteListener) {
        this.model = model;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View noteView = inflater.inflate(R.layout.note_item, parent, false);
        NoteViewHolder viewHolder = new NoteViewHolder(noteView, monNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String currentContent = model.at(position).content.toString().trim();
        if(currentContent.length() > 15) currentContent = currentContent.substring(0,15);
        holder.txt_content.setText(currentContent.trim());
        //////////////////////////////////////////////////
        String currentTitle = model.at(position).title.toString();
        if(currentTitle.isEmpty()) {
            if(currentContent.length() > 10) {
                currentTitle = currentContent.substring(0,10);
            } else {
                currentTitle = currentContent;
            }
        } else if(currentTitle.length() > 10) {
                currentTitle = currentTitle.substring(0,10);
        }
        holder.txt_title.setText(currentTitle);
    }

    @Override
    public int getItemCount() {
        return model.notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_title;
        TextView txt_content;
        OnNoteListener onNoteListener;
        public NoteViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.Title_Item);
            txt_content = itemView.findViewById(R.id.Note_Item);
            this.onNoteListener = onNoteListener;
            ImageButton deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.delete_button) {
                onNoteListener.onButtonClick(getAdapterPosition());
            } else {
                onNoteListener.onNoteClick(getAdapterPosition());
            }
        }
    }

    public interface  OnNoteListener {
        void onNoteClick(int position);
        void onButtonClick(int position);
    }
}
