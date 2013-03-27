package com.example.mydatepickerdialog;

import java.lang.reflect.Method;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		
		// Watch for button clicks.
        Button button = (Button)findViewById(R.id.show);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
	}
	
    void showDialog() {
    	DialogFragment newFragment = DatePickerDialogFragment.newInstance(
                R.string.alert_dialog_two_buttons_title);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        Log.d("Alexfed from: ", DatePickerDialogFragment.getDateFrom().replace('.', '/'));
        Log.d("Alexfed to: ", DatePickerDialogFragment.getDateTo().replace('.', '/'));
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }



    public static class DatePickerDialogFragment extends SherlockDialogFragment {
    	
    	private static EditText dateFrom;
    	private static EditText dateTo;
    	private DatePicker dPicker;
    	
        public static DatePickerDialogFragment newInstance(int title) {
        	DatePickerDialogFragment frag = new DatePickerDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.date_picker, null);
            dateFrom = (EditText) view.findViewById(R.id.date_from);
            dateTo = (EditText) view.findViewById(R.id.date_to);
            dPicker = (DatePicker) view.findViewById(R.id.datePicker);
            
            setCurrentDateOnView();
            setOnTextFocus();
            
            builder.setTitle(title);
            builder.setView(view);
            builder.setPositiveButton(getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	((MainActivity)getActivity()).doPositiveClick();
                }
            });
            builder.setNegativeButton(getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	((MainActivity)getActivity()).doNegativeClick();
                }
            });
            return builder.create();
        }

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}
		
		public void setCurrentDateOnView() {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;

			if (currentapiVersion >= 11) {
				 try {
				    Method m = dPicker.getClass().getMethod("setCalendarViewShown", boolean.class);
				    m.invoke(dPicker, false);
				  }
				  catch (Exception e) {} // eat exception in our case
			}
			int correctMonth = month+1;
			dateFrom.setText(getActivity().getResources().getString(R.string.str_from) + " " + 
									day+"."+correctMonth+"."+year);
			dateTo.setText(getActivity().getResources().getString(R.string.str_to) + " " + 
									day+"."+correctMonth+"."+year);
			
			dPicker.init(year, month, day, new OnDateChangedListener() {
				
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					int realMonth = monthOfYear + 1;
					if(dateFrom.isFocused()){
						dateFrom.setText(getActivity().getResources().getString(R.string.str_from) + " " + 
								dayOfMonth+"."+realMonth+"."+year);
					}else{
						dateTo.setText(getActivity().getResources().getString(R.string.str_to) + " " + 
								dayOfMonth+"."+realMonth+"."+year);
					}
					
				}
			});	
		}
		
		private void setOnTextFocus(){
			dateFrom.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						String delims = "[\\s:.]+";
						String[] tokens = dateFrom.getText().toString().split(delims);
						dPicker.updateDate(Integer.valueOf(tokens[3]), 
								Integer.valueOf(tokens[2]) - 1, 
								Integer.valueOf(tokens[1]));
					}
					
				}
			});
			
			dateTo.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						String delims = "[\\s:.]+";
						String[] tokens = dateTo.getText().toString().split(delims);
						dPicker.updateDate(Integer.valueOf(tokens[3]), 
								Integer.valueOf(tokens[2]) - 1, 
								Integer.valueOf(tokens[1]));
					}
					
				}
			});
		}
        
		public static String getDateFrom(){
			String delims = "[\\s]+";
			String[] tokens = dateFrom.getText().toString().split(delims);
			return tokens[1].trim();
		}
		
		public static String getDateTo(){
			String delims = "[\\s]+";
			String[] tokens = dateTo.getText().toString().split(delims);
			return tokens[1].trim();
		}
    }


}
