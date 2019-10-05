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

    public static String EXTRA_TITLE = "com.tripleservice.mvvm_architecture.EXTRA_TITLE";
    public static String EXTRA_DESC = "com.tripleservice.mvvm_architecture.EXTRA_DESC";
    public static String EXTRA_PRIORITY = "com.tripleservice.mvvm_architecture.EXTRA_PRIORITY";
    public static String EXTRA_ID = "com.tripleservice.mvvm_architecture.EXTRA_ID";

    private static final int ADD_RESULT_CODE = 121;
    private static final int EDIT_RESULT_CODE = 122;
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
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                startActivityForResult(new Intent(MainActivity.this, AddNoteActivity.class), ADD_RESULT_CODE);
            }
        });
    }

    @Override
    public void onClick(Note note) {
        //handle recycler click events...


        Intent intent = new Intent(this, AddNoteActivity.class);

        intent.putExtra(EXTRA_TITLE, note.getTitle());
        intent.putExtra(EXTRA_DESC, note.getDescription());
        intent.putExtra(EXTRA_PRIORITY, note.getPriority());
        intent.putExtra(EXTRA_ID, note.getId());
        
        startActivityForResult(intent, EDIT_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RESULT_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EXTRA_TITLE);
            String desc = data.getStringExtra(EXTRA_DESC);
            int priority = data.getIntExtra(EXTRA_PRIORITY, 1);
            mNoteViewModel.insert(new Note(title, desc, priority));
            Toast.makeText(this, "Note Saved !! ", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == EDIT_RESULT_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "can't update Note !", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(EXTRA_TITLE);
            String desc = data.getStringExtra(EXTRA_DESC);
            int priority = data.getIntExtra(EXTRA_PRIORITY, 1);

            mNoteViewModel.update(new Note(id, title, desc, priority));
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
