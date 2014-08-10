package com.rentner.shedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class DayPickerDialog extends DialogFragment {
	private int selectedItem = 0;
	private View selectedView;
	
	public interface DayPickerDialogListener {
		public void onDialogItemClick(DayPickerDialog dialog);

	}

	public DayPickerDialog() {
		super();
	}

	DayPickerDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the
	// DayPickerDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (DayPickerDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement DayPickerDialogListener");
		}
	}

	public void setSelectedView(View view) {
		this.selectedView = view;
	}

	public int getSelectedItem() {
		return this.selectedItem;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.day_type_picker_title).setItems(
				R.array.day_types_array, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						selectedItem = which;
						mListener.onDialogItemClick(DayPickerDialog.this);
						switch (which) {
						case 0:
							selectedView
									.setBackgroundResource(R.drawable.day_cell);
				
							break;

						case 1:
							selectedView
									.setBackgroundResource(R.drawable.night_cell);
							
							break;

						case 2:
							selectedView
									.setBackgroundResource(R.drawable.eight_cell);
							
							break;

						case 3:
							selectedView
									.setBackgroundResource(R.drawable.leave_cell);
							
							break;

						case 4:
							selectedView
									.setBackgroundResource(R.drawable.default_cell);
							
							break;

						default:

							break;
						}

					}
				});
		return builder.create();
	}

}
