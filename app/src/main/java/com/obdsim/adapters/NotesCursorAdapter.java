package com.obdsim.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.obdsim.R;
import com.obdsim.data.DBOpenHelper;

/**
 * Used instead of the SimpleCursorAdapter to add more functionality and customization.
 */
public class NotesCursorAdapter extends CursorAdapter {

    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.save_note_list_item, parent, false);
    }

    /**
     * @param view
     * @param context
     * @param cursor  points to the particular row that it's supposed to be displayed.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        @SuppressLint("Range") String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

        TextView noteTextView = view.findViewById(R.id.noteTextView);
        noteTextView.setText(noteText);
    }

}
