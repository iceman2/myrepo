package com.rentner.shedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter {
	public GregorianCalendar pmonth; // calendar instance for previous month
	public GregorianCalendar pmonthmaxset;
	public static List<String> dayString;
	
	private GregorianCalendar selectedDate;
	private java.util.Calendar month;
	private Context mContext;
	
	
	private ArrayList<Integer> listOfBackgrounds;
	private ArrayList<String> items;
		
	private int firstDay;
	private int maxWeeknumber;
	private int maxP;
	private int calMaxP;
	private int mnthlength;
	private String itemvalue, curentDateString;
	private DateFormat df;

	public MyGridAdapter(Context mContext, ArrayList<Integer> listBackGrounds, GregorianCalendar monthCalendar) {
		super();
		this.mContext = mContext;
		this.listOfBackgrounds = listBackGrounds;
		
		
		MyGridAdapter.dayString = new ArrayList<String>();
		 Locale.setDefault( Locale.getDefault() );
		month = monthCalendar;
						
		month.setFirstDayOfWeek(GregorianCalendar.MONDAY);
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		
		// for debug
		//month.set((month.get(GregorianCalendar.YEAR) + 1), GregorianCalendar.JANUARY,1);
		
		this.items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
				
	}
	
	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	@Override
	public int getCount() {
			return dayString.size();
		
	}

	@Override
	public Object getItem(int position) {
				return dayString.get(position);		
		
	}

	@Override
	public long getItemId(int arg0) {
				return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayNumberText;

		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.grid_child, null);

		} else {
			v = convertView;
		}
		dayNumberText = (TextView) v.findViewById(R.id.date);
		
		// separates daystring into parts.
				String[] separatedTime = dayString.get(position).split("-");
				// taking last part of date. ie; 2 from 2012-12-02
				String gridvalue = separatedTime[2].replaceFirst("^0*", "");
				// checking whether the day is in current month or not.
				if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
					// setting offdays to white color.
					dayNumberText.setTextColor(Color.WHITE);
					dayNumberText.setClickable(false);
					dayNumberText.setFocusable(false);
				} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
					dayNumberText.setTextColor(Color.WHITE);
					dayNumberText.setClickable(false);
					dayNumberText.setFocusable(false);
				} else {
					// setting curent month's days in blue color.
					dayNumberText.setTextColor(Color.BLUE);
				}

				if (dayString.get(position).equals(curentDateString)) {
					dayNumberText.setTextColor(Color.RED);
					dayNumberText.setTextSize(24.0f);
				} 
				
				dayNumberText.setText(gridvalue);

		v.setBackgroundResource(listOfBackgrounds.get(position));
		return v;
	}
	
	public void refreshDays() {
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault( Locale.getDefault() );
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		//because we had changed the first day of week from sunday to monday
		if (month.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY) {
			firstDay = 7;
		} else {
			firstDay = month.get(GregorianCalendar.DAY_OF_WEEK) - 1;
		};
		
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == GregorianCalendar.JANUARY) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1), GregorianCalendar.DECEMBER,1);
		
		//pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}
