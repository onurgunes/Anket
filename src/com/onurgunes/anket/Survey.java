package com.onurgunes.anket;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Survey extends Activity implements OnClickListener{

	LinearLayout ll, wrapperLayout;
	Button btnStartSurvey;
	Database db;
	Survey item;
	RadioGroup radioGroup;
	TextView txtQuestion;
	ArrayList<Integer> ids;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
		ids = new ArrayList<Integer>();
		Bundle extras = getIntent().getExtras();
		ids = extras.getIntegerArrayList("ids");
		if(ids.size()>0)
			Log.i("ids",String.valueOf(ids.get(0)));

		ll = (LinearLayout) findViewById(R.id.llQuestions);
		btnStartSurvey = (Button) findViewById(R.id.btnFinishSurvey);
		
		btnStartSurvey.setOnClickListener(this);
		
		db = new Database(this);
		
		//ids �nceki ekranda (choosequestions) se�ilen soru say�s�d�r
		for (int i = 0; i < ids.size(); i++) {
//			Log.i("ids.size()",String.valueOf(ids.size()));
			wrapperLayout = new LinearLayout(this);
			radioGroup = new RadioGroup(this);
			txtQuestion = new TextView(this);
			
			radioGroup.setId(i*100); //Id �ak��mas� olmamas� i�in bir trick
			int optionCount = db.getOptionCount(ids.get(i));
//			Log.i("OptionCount",String.valueOf(optionCount));
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			buttonParams.setMargins(0, 10, 0, 10);
			
			SurveyItem item = db.getQuestionAndItsOptionById(ids.get(i));
//			Log.i("Se�ilme say�s�", String.valueOf( item.getOptions().get(i).getSelectedCount() ));
			txtQuestion.setText(item.getQuestion().getQuestion());
//			Log.i("Soru Id", String.valueOf(item.getQuestion().getId()));
//			Log.i("Soru", String.valueOf(item.getQuestion().getQuestion()));
			
			//ids dizisinin ilk eleman�n�n se�enek say�s�
			for (int j = 0; j < optionCount; j++) {
//				Log.i("Radio buton k�sm�na geldi","");
				RadioButton rButton = new RadioButton(this);
				rButton.setId(item.getOptions().get(j).getId());
				rButton.setText(item.getOptions().get(j).getOption());
				radioGroup.addView(rButton);
			}
			LinearLayout layoutQuestion = new LinearLayout(this);
			layoutQuestion.addView(txtQuestion,layoutParams);
			wrapperLayout.addView(radioGroup,buttonParams);
			ll.addView(layoutQuestion);
			ll.addView(wrapperLayout,layoutParams);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.survey, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == btnStartSurvey.getId()){
			//veritaban�n� anket sonu�lar�yla g�ncelle
			try {
				for (int i = 0; i < ids.size(); i++) {
					RadioGroup rGroup = (RadioGroup) findViewById(i*100);
					db.saveStatistics(rGroup.getCheckedRadioButtonId());
//					Log.i("Secilen id", String.valueOf( rGroup.getCheckedRadioButtonId() ));
				}
				Toast.makeText(Survey.this, "Anket ba�ar�yla kaydedildi", Toast.LENGTH_SHORT).show();
				Survey.this.finish();
			} catch (Exception e) {
				Toast.makeText(Survey.this, "Anket sonu�lar� i�lenemedi", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
