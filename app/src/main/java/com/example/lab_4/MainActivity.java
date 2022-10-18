package com.example.lab_4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TODO> elements;
    private MyListAdapter myAdapter;
    TODO todo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText editText = (EditText) findViewById(R.id.editText);
        Switch swUrgent = findViewById(R.id.switch2);
        Button addButton = findViewById(R.id.myButton);

        elements = new ArrayList<>();

        loadDataFromDatabase();

        addButton.setOnClickListener(click -> {
            myAdapter.notifyDataSetChanged();

            String listItem = editText.getText().toString();

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_ITEMS, listItem);
            newRowValues.put((MyOpener.COL_URGENT), swUrgent.isChecked());
            long newID = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            todo = new TODO(listItem, newID, swUrgent.isChecked());
            elements.add(todo);

            editText.setText("");
            swUrgent.setChecked(false);
            myAdapter.notifyDataSetChanged();

        });

        ListView myList = findViewById(R.id.myList);
        myList.setAdapter(myAdapter = new MyListAdapter());

        //myList.setOnItemClickListener((parent, view, pos, id) -> {
//            elements.remove(pos);
//            myAdapter.notifyDataSetChanged();
        //});

        myList.setOnItemLongClickListener((p, b, pos, id) -> {
            View newView = getLayoutInflater().inflate(R.layout.todo, null);
            TextView tView = newView.findViewById(R.id.textHere);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            tView.setText(elements.get(pos).getTodoText());

            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + pos + "\n " + elements.get(pos).todoText)

                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteTodo(elements.get(pos));
                        elements.remove (elements.get(pos));

                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> { })
                    .setView(newView)
                    .create().show();
            return true;
        });
    }

    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        dbOpener.onCreate(db);

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ITEMS, MyOpener.COL_URGENT};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null,
                null, null, null, null, null);

        Cursor c = db.rawQuery("SELECT * from " + MyOpener.TABLE_NAME,null);
        //int colIndex = c.getColumnIndex();

        int nameColIndex = results.getColumnIndex(MyOpener.COL_ITEMS);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int urgentColIndex = results.getColumnIndex(MyOpener.COL_URGENT);

        while (results.moveToNext()) {
            String name = results.getString(nameColIndex);
            long id = results.getLong(idColIndex);
            boolean urgent = (results.getInt(urgentColIndex) != 0);

            elements.add(new TODO(name, id, urgent));
        }
    }

    protected void updateTodo(TODO c) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_ITEMS, c.getTodoText());

        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?",
                new String[]{Long.toString(c.getId())});
        db.close();
    }

    protected void deleteTodo(TODO c) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?",
                new String[]{Long.toString(c.getId())});

        db.close();
    }

    private class MyListAdapter extends BaseAdapter {
        public int getCount() {
            return elements.size();
        }
        public TODO getItem(int position) {
            return elements.get(position);
        }
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            if (newView == null) {
                newView = inflater.inflate(R.layout.todo, parent, false);
            }
            TextView tView = newView.findViewById(R.id.textHere);
            tView.setText(getItem(position).todoText);

            if (getItem(position).isUrgent) {
                tView.setBackgroundColor(Color.RED);
                tView.setTextColor(Color.WHITE);
            }
            else {
                tView.setBackgroundColor(Color.WHITE);
                tView.setTextColor(Color.BLACK);
            }
            return newView;
        }
    }
}
