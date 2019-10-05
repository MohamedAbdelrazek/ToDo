package com.tripleservice.mvvm_architecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tripleservice.mvvm_architecture.adapter.NotesAdapter;
import com.tripleservice.mvvm_architecture.adapter.OnItemClicked;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements OnItemClicked {

    private static final int RESULT_CODE = 121;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton addActionButton;

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = mNotesAdapter.getNoteAtPosition(viewHolder.getAdapterPosition());
                mNoteViewModel.delete(note);
                Toast.makeText(MainActivity.this, "Note Deleted Successfully!", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(mNotesRecyclerView);


        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddNoteActivity.class), RESULT_CODE);
            }
        });
    }

    @Override
    public void onClick(Note note) {
        //handle recycler click events...



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddNoteActivity.EXTRA_DESC);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);
            mNoteViewModel.insert(new Note(title, desc, priority));
            Toast.makeText(this, "Note Saved !! ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not Saved !! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_note: {
                mNoteViewModel.deleteAllNotes();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
