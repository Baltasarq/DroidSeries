package com.devbaltasarq.DroidSeries.Ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.devbaltasarq.DroidSeries.Core.Serie;
import com.devbaltasarq.DroidSeries.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main extends Activity {
    public static final String EtqPrefsSeries = "SERIES";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        // Button "add"
        Button btAdd = (Button) this.findViewById( R.id.btAdd );
        btAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.this.onAdd();
            }
        });

        // Create list view
        this.listSeries = new ArrayList<>();
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

    private void saveSate() {
        SharedPreferences.Editor editor = this.getPreferences( Context.MODE_PRIVATE ).edit();
        Set<String> series = new HashSet<>();

        editor.clear();
        for( Serie serie: this.listSeries ) {
            series.add( serie.getName() );
            System.out.println( "Stored serie: " + serie.getName()  );
        }

        // Write settings
        editor.putStringSet( EtqPrefsSeries, series );
        System.out.println( "Saved: " + series.toString() );

        for( Serie serie: this.listSeries ) {
            editor.putInt( serie.getId(), serie.getCodedEpisode() );
            System.out.println( "Saved serie: " + serie.getName() + ": " + serie.getId() );
        }

        editor.apply();
        System.out.println( "Saved state..." );
    }

    private void loadState() {
        Set<String> series;
        SharedPreferences prefs = this.getPreferences( Context.MODE_PRIVATE );

        // Retrieve series names
        this.listSeries.clear();
        series = prefs.getStringSet( EtqPrefsSeries, new HashSet<String>()  );
        System.out.println( "Loaded: " + series.toString() );

        for(String serieName: series) {
            this.listSeries.add( new Serie( serieName ) );
        }

        // Retrieve last episode for each serie
        for(Serie serie: this.listSeries) {
            serie.setCodedEpisode( prefs.getInt( serie.getId(), 1001 ) );
            System.out.println( "Retrieved serie: " + serie.getName() );
        }

        series.clear();
        this.laSeries.notifyDataSetChanged();
        System.out.println( "Loaded state..." );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.saveSate();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.loadState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );

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
