package com.onurgunes.anket;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteQuestion extends Activity implements OnClickListener{

	LinearLayout ll
				,wrapperLayout;
	Button btnDeleteItems;
	Database db;
	ArrayList<Question> questionList;
	ArrayList<Integer> ids;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_questions);
		
		db = new Database(this);
		
		ll = (LinearLayout) findViewById(R.id.llQuestions);
		btnDeleteItems = (Button) findViewById(R.id.btnStartSurvey);
		
		btnDeleteItems.setOnClickListener(this);
		btnDeleteItems.setText("Sorularý Sil");
		
		questionList = db.getAllQuestions(); 
		ids = new ArrayList<Integer>();
		
		for (int i = 0; i < questionList.size(); i++) {
			wrapperLayout = new LinearLayout(this);
			CheckBox chck = new CheckBox(this);
			TextView txt = new TextView(this);
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																					LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			buttonParams.setMargins(0, 10, 0, 10);		
			
			chck.setText("");
			chck.setId(questionList.get(i).getId());
			ids.add(questionList.get(i).getId());
			txt.setText(questionList.get(i).getQuestion());
			txt.setTextSize(16);
			
			wrapperLayout.addView(chck,buttonParams);
			wrapperLayout.addView(txt,buttonParams);
			ll.addView(wrapperLayout,layoutParams);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.delete_question, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == btnDeleteItems.getId()){
			try {
				for (int i = 0; i < ids.size(); i++) {
					CheckBox v1 = (CheckBox) findViewById(ids.get(i));
					if( ((CheckBox) v1).isChecked() == true)
						db.deleteQuestionAndItsOptions(ids.get(i));
				}
				onCreate(null);
			} catch (Exception e) {
				Toast.makeText(DeleteQuestion.this, "Silme iþlemi esnasýnda sorun oluþtu", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
