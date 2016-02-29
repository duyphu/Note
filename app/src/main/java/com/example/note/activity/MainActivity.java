package com.example.note.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.note.R;
import com.example.note.activity.base.BaseActivity;
import com.example.note.custom.adapter.NoteListAdapter;
import com.example.note.db.table.NoteTable;
import com.example.note.model.NoteItem;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private GridView gvNoteList;
    private ArrayList<Integer> mIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.t_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.notes);
        actionBar.setDisplayUseLogoEnabled(true);

        mIds = new ArrayList<Integer>();
        NoteTable noteTable = new NoteTable(this);
        final ArrayList<NoteItem> list = noteTable.getAll();
        for (int i = 0; i < list.size(); i++) {
            mIds.add(list.get(i).getId());
        }
        gvNoteList = (GridView)findViewById(R.id.gv_note_list);
        NoteListAdapter adapter = new NoteListAdapter(this, R.layout.item_grid_note, list);
        gvNoteList.setAdapter(adapter);
        gvNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteItem item = list.get(position);
                int noteId = item.getId();
                Intent intent = new Intent(MainActivity.this, OpenNoteFragmentActivyty.class);
                intent.putExtra("noteId", noteId);
                intent.putExtra("listId", mIds);
                startActivity(intent);
            }
        });

        Log.i("MainActivity", " onCreat");
    }

    protected void onResume(){
        super.onResume();
        Log.i("MainActivity", " onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
}
