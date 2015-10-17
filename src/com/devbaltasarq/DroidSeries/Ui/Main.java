package com.devbaltasarq.DroidSeries.Ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.devbaltasarq.DroidSeries.Core.Serie;
import com.devbaltasarq.DroidSeries.R;

import java.util.ArrayList;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        this.listSeries = new ArrayList<>();

        // Button "add"
        Button btAdd = (Button) this.findViewById( R.id.btAdd );
        btAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.this.onAdd();
            }
        });

        // Create list view
        ListView lvSeries = (ListView) this.findViewById( R.id.lvSeries );
        lvSeries.setLongClickable( true );
        this.laSeries = new ArrayAdapter<Serie>(
                this,
                android.R.layout.simple_selectable_list_item,
                listSeries );
        lvSeries.setAdapter( this.laSeries );

        // Contextual menu
        this.registerForContextMenu( lvSeries );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch( menuItem.getItemId() ) {
            case R.id.mainMenuItemAddSerie:
                this.onAdd();
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
    {
        super.onCreateContextMenu( contextMenu, view, contextMenuInfo );

        if ( view.getId() == R.id.lvSeries ) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;

            contextMenu.setHeaderTitle( Main.this.listSeries.get( acmi.position ).getName() );
            this.getMenuInflater().inflate( R.menu.context_menu, contextMenu );
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch( item.getItemId() ) {
            case R.id.contextMenuIncEpisode:
                this.listSeries.get( ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).position ).incEpisode();
                Toast.makeText( this, "Episode number incremented", Toast.LENGTH_SHORT ).show();
                break;
            case R.id.contextMenuIncSeason:
                this.listSeries.get( ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).position ).incSeason();
                Toast.makeText( this, "Season number incremented", Toast.LENGTH_SHORT ).show();
                break;
        }

        this.laSeries.notifyDataSetChanged();
        return true;
    }

    private void onAdd() {
        final EditText edName = new EditText( this );
        AlertDialog.Builder dlg = new AlertDialog.Builder( this );
        dlg.setTitle( "New series name" );
        dlg.setView( edName );
        dlg.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    laSeries.add( new Serie( edName.getText().toString().trim() ) );
                    Toast.makeText( Main.this, "Serie created", Toast.LENGTH_SHORT ).show();
                }
                catch(IllegalArgumentException exc) {
                    Toast.makeText( Main.this, "Serie not created: " + exc.getMessage(), Toast.LENGTH_LONG ).show();
                }
            }
        });
        dlg.setNegativeButton( "Cancel", null );
        dlg.create().show();
    }

    private ArrayList<Serie> listSeries;
    private ArrayAdapter<Serie> laSeries;
}
