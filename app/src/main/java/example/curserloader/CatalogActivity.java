package example.curserloader;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import example.curserloader.recylerview.MyListCursorAdapter;
import example.curserloader.recylerview.OnClickListner;
import example.curserloader.widget.CollectionWidget;

import static example.curserloader.widget.WidgetDataProvider.mCollection;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, OnClickListner {

    private static final int PET_LOADER = 0;
    public static final String LOG_TAG = CatalogActivity.class.getSimpleName();
    private static final String TAG = "CatalogActivity";
    //PetCursorAdapter petCursorAdapter;
    MyListCursorAdapter mRecylerAdapter;
    CursorLoader mCursor =  null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView mRecylerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecylerAdapter = new MyListCursorAdapter(this, null,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(mRecylerAdapter);



       // petCursorAdapter = new PetCursorAdapter(this, null);


//        listView.setAdapter(petCursorAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
//                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
//                Uri uri = Uri.withAppendedPath(PetContract.PetEntry.CONTENT_URI, String.valueOf(itemId));
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        });
        getSupportLoaderManager().initLoader(PET_LOADER, null, this);
    }

    private void insertPet() {
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                changeSorting();
                //deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeSorting() {
        mCollection.add("ListView item " + 100);
        Log.d(TAG, "changeSorting() called");
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, CollectionWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME ,
                PetContract.PetEntry.COLUMN_PET_BREED,
        };

        switch (loaderId) {
            case PET_LOADER:
                mCursor = new CursorLoader(
                        this,
                        PetContract.PetEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        PetContract.PetEntry.COLUMN_PET_NAME+" DESC"
                );
                return mCursor;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mRecylerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecylerAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Position is : " + position, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
//        Uri uri = Uri.withAppendedPath(PetContract.PetEntry.CONTENT_URI, String.valueOf(position));
//        intent.setData(uri);
//        startActivity(intent);
    }
}