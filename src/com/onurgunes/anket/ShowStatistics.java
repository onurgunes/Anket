package com.onurgunes.anket;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowStatistics extends Activity implements OnClickListener{

	Button resetButton;
	LinearLayout ll, wrapperLayout;
	Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_statistics);
		
		db = new Database(this);
        ArrayList<SurveyItem> items = new ArrayList<SurveyItem>();
        
        resetButton = (Button) findViewById(R.id.btnResetStatistics);
        ll = (LinearLayout) findViewById(R.id.llQuestions);
        
        resetButton.setOnClickListener(this);
        resetButton.setText("Verileri sýfýrla");
        
		items = db.getStatistics();

		for (int i = 0; i < items.size(); i++) {	
//			Log.i("Soru" + (i+1),items.get(i).getQuestion().getQuestion());
			wrapperLayout = new LinearLayout(this);
			TextView tvQuestion = new TextView(this);
			LinearLayout layout = new LinearLayout(this);
			
			LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			textViewParams.setMargins(0, 0, 10, 0);
			
			tvQuestion.setText(items.get(i).getQuestion().getQuestion());
			ll.addView(tvQuestion);
			for (int j = 0; j < items.get(i).getOptions().size(); j++) {
				TextView tvStatistic = new TextView(this);
				TextView tvOption = new TextView(this);
				
				tvQuestion.setTextSize(16);
				tvStatistic.setTextSize(16);
				tvOption.setTextSize(16);
				tvStatistic.setTypeface(null,Typeface.BOLD);
				tvOption.setTypeface(null,Typeface.BOLD);
				
				tvStatistic.setText( String.valueOf(items.get(i).getOptions().get(j).getSelectedCount()) );
				tvOption.setText( items.get(i).getOptions().get(j).getOption() );
//				Log.i("SecilimSayisi" + (i+1) , String.valueOf(items.get(i).getOptions().get(j).getSelectedCount() ));
				
				LinearLayout newLine = new LinearLayout(this);
				
				newLine.addView(tvStatistic,textViewParams);
				newLine.addView(tvOption,textViewParams);
				
				layout.addView(newLine);
			}
			layout.setOrientation(LinearLayout.VERTICAL);
			wrapperLayout.addView(layout);
			ll.addView(wrapperLayout);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_statistics, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		//Týklanýnca veriler resetlenecek
//		Log.i("Idler",String.valueOf(v.getId() + " " + resetButton.getId()));
		if (v.getId() == resetButton.getId()) {
			try {
				db.resetStatistics();
				Toast.makeText(ShowStatistics.this, "Deðerler sýfýrlandý", Toast.LENGTH_SHORT).show();
				onCreate(null);
			} catch (Exception e) {
				Toast.makeText(ShowStatistics.this, "Bir hata oluþtu", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
