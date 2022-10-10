package com.example.lab_4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) findViewById(R.id.editText);
        Switch swUrgent = findViewById(R.id.switch2);
        Button addButton = findViewById(R.id.myButton);

        elements = new ArrayList<>();

        addButton.setOnClickListener(click -> {
            String listItem = editText.getText().toString();

            todo = new TODO();
            todo.setTodoText(listItem);
            todo.setUrgent(swUrgent.isChecked());

            elements.add(todo);
            myAdapter.notifyDataSetChanged();
            editText.setText("");
            swUrgent.setChecked(false);
        });

        ListView myList = findViewById(R.id.myList);
        myList.setAdapter(myAdapter = new MyListAdapter());

        myList.setOnItemLongClickListener((p, b, pos, id) -> {
            View newView = getLayoutInflater().inflate(R.layout.todo, null);
            TextView tView = newView.findViewById(R.id.textGoesHere);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            tView.setText(elements.get(pos).getTodoText());

            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + pos + "\n ")// + elements.get(pos).todoText)

                    .setPositiveButton("Yes", (click, arg) -> {
                        elements.remove(elements.get(pos));
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> { })
                    .setView(newView)
                    .create().show();
            return true;
        });
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
            TextView tView = newView.findViewById(R.id.textGoesHere);
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
class TODO {

    String todoText;
    boolean isUrgent;

    public String getTodoText() {
        return todoText;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }
}
