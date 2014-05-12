package com.onurgunes.anket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button btnChooseExistingQuestions,
			btnAddQuestion,
			btnDeleteQuestion,
			btnShowStatistics;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnChooseExistingQuestions = (Button) findViewById(R.id.btnChooseExistingQuestion);
        btnAddQuestion = (Button) findViewById(R.id.btnAddQuestion);
        btnDeleteQuestion = (Button) findViewById(R.id.btnDeleteQuestion);
        btnShowStatistics = (Button) findViewById(R.id.btnShowStatistics);
        
        btnChooseExistingQuestions.setOnClickListener(this);
        btnAddQuestion.setOnClickListener(this);
        btnDeleteQuestion.setOnClickListener(this);
        btnShowStatistics.setOnClickListener(this);
        
    }   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == btnAddQuestion.getId()) {
			Intent intent = new Intent(MainActivity.this, AddQuestion.class);
			MainActivity.this.startActivity(intent);
		}
		else if (v.getId() == btnChooseExistingQuestions.getId()) {
			Intent intent = new Intent(MainActivity.this, ChooseQuestions.class);
			MainActivity.this.startActivity(intent);
		}
		else if(v.getId() == btnDeleteQuestion.getId()){
			Intent intent = new Intent(MainActivity.this, DeleteQuestion.class);
			MainActivity.this.startActivity(intent);
		}
		else if(v.getId() == btnShowStatistics.getId()){
			Intent intent = new Intent(MainActivity.this, ShowStatistics.class);
			MainActivity.this.startActivity(intent);
		}
	}
    
}
