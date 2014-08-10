package com.rentner.shedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.rentner.shedule.DayPickerDialog.DayPickerDialogListener;

public class MainActivity extends FragmentActivity implements DayPickerDialogListener {
	public GregorianCalendar month, itemmonth;// calendar instances
	private GridView grid;
	private MyGridAdapter adapter;
	private ArrayList<Integer> bgs = null;
	private Integer[] mThumbIds = { R.drawable.day_cell, R.drawable.night_cell,
			R.drawable.eight_cell, R.drawable.leave_cell,
			R.drawable.default_cell };
	private int backGround = 0, dialogSelectedItem = 0, positionPlusOne = 0, updateMonth = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Locale.setDefault(Locale.getDefault());
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		
		TextView title = (TextView) findViewById(R.id.top_title);
		//title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		Resources res = getResources();
		String monthArray[] = res.getStringArray(R.array.arrayOfMonths);
		title.setText( monthArray[month.get(Calendar.MONTH)] + " " +
				android.text.format.DateFormat.format("yyyy", month));

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		updateMonth = sharedPref.getInt("updatedIn", 0);
	
		if (month.get(Calendar.MONTH) != updateMonth) {
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.clear();
			editor.commit();
		}
				
		bgs = new ArrayList<Integer>();
		for (int i = 1; i < 36; i++) { //36 = 35 cells in grid (7x5) + 1

			backGround = sharedPref.getInt(Integer.toString(i),
					R.drawable.default_cell);
			bgs.add(i - 1, backGround);
		}
		
		grid = (GridView) findViewById(R.id.gridview);
		adapter = new MyGridAdapter(this, bgs, month);
		grid.setAdapter(adapter);
		grid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				positionPlusOne = position + 1;
				DayPickerDialog dayTypePicker = new DayPickerDialog();
				dayTypePicker.setSelectedView(v);
				dayTypePicker.show(getSupportFragmentManager(), "days");
				return false;
			}
		});

	}

	public void saveCellBackground(int selectedItem, int pos) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("updatedIn", month.get(Calendar.MONTH));
		switch (selectedItem) {
		case 0:
			editor.putInt(Integer.toString(pos), mThumbIds[0]);
			break;
		case 1:
			editor.putInt(Integer.toString(pos), mThumbIds[1]);
			break;
		case 2:
			editor.putInt(Integer.toString(pos), mThumbIds[2]);
			break;
		case 3:
			editor.putInt(Integer.toString(pos), mThumbIds[3]);
			break;
		default:
			editor.putInt(Integer.toString(pos), mThumbIds[4]);
			break;
		}
		editor.commit();

	}

	@Override
	public void onDialogItemClick(DayPickerDialog dialog) {
		dialogSelectedItem = dialog.getSelectedItem();
		saveCellBackground(dialogSelectedItem, positionPlusOne);

	}
	
	
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

}
