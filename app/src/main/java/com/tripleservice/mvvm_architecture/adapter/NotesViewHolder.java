package com.tripleservice.mvvm_architecture.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tripleservice.mvvm_architecture.Note;
import com.tripleservice.mvvm_architecture.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.notes_single_item_title_tv)
    TextView titleTxtView;
    @BindView(R.id.notes_single_item_desc_tv)
    TextView descTxtView;
    @BindView(R.id.notes_single_item_priority_tv)
    TextView priorityTxtView;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bind(Note note) {
        titleTxtView.setText(note.getTitle());
        descTxtView.setText(note.getDescription());
        priorityTxtView.setText("" + note.getPriority());
    }
}
