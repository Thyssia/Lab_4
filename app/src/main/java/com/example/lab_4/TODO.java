package com.example.lab_4;

import android.database.sqlite.SQLiteDatabase;

public class TODO {

    String todoText;
    boolean isUrgent;
    protected long id;

    public TODO() {
    }

    public TODO(String n, long i) {
        todoText = n;
        id = i;
    }

    public TODO(String n, long i, boolean urgent) {
        todoText = n;
        id = i;
        isUrgent = urgent;
    }

    public void update(String n) {
        todoText = n;
    }

    public String getTodoText() {
        return todoText;
    }

    public long getId() {
        return id;
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
