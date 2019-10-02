package com.tripleservice.mvvm_architecture;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tripleservice.mvvm_architecture.adapter.NotesAdapter;
import com.tripleservice.mvvm_architecture.adapter.OnItemClicked;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements OnItemClicked {

    private NoteViewModel mNoteViewModel;
    private NotesAdapter mNotesAdapter;

    @BindView(R.id.notes_recycler_view)
    RecyclerView mNotesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNotesRecyclerView.setHasFixedSize(true);
        mNotesAdapter = new NotesAdapter(this);
        mNotesRecyclerView.setAdapter(mNotesAdapter);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecyclerView ....

                mNotesAdapter.updateNotes(notes);
            }
        });
    }

    @Override
    public void onClick(Note note) {
        //handle recycler click events...

    }
}
