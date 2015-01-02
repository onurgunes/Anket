package com.onurgunes.anket;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddQuestion extends Activity {

	Database db;
	EditText input;
	Button btnAddOption
		  ,btnDone
		  ,btnDeleteOptions;
	TextView tvQuestion,
			tvOptions;
	AlertDialog openAddOptionWindow;
	ArrayList<String> options;
	String question;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_question);
		
		db = new Database(this);
		
		btnAddOption = (Button) findViewById(R.id.btnAddOption);
		btnDone = (Button) findViewById(R.id.btnDone);
		btnDeleteOptions = (Button) findViewById(R.id.btnDeleteOptions);
		tvQuestion = (TextView) findViewById(R.id.txtQuestion);
		tvOptions = (TextView) findViewById(R.id.txtOptions);
		
		options = new ArrayList<String>();
		
		/*	Seçenek ekleme penceresinin baþlangýcý	*/
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setTextSize(16);
        builder.setTitle(getResources().getString(R.string.add_question_option));
        builder.setView(input);
        //Seçenek ekleme penceresinde tamam a basýldýðýnda yapýlacaklar
        builder.setPositiveButton(getResources().getString(R.string.accept_to_add_option), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String inputText = input.getText().toString();
				if (!inputText.toString().isEmpty()) {
					options.add(inputText);
					tvOptions.setText(tvOptions.getText() + "\n" + inputText);
					input.setText("");
				}
			}
		});
        builder.setNegativeButton(getResources().getString(R.string.cancel_to_add_option), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Seçenek ekleme penceresi kapatýlacak
				dialog.cancel();		
			}
		});
		openAddOptionWindow = builder.create();
		/*	Seçenek ekleme penceresinin bitiþi	*/
		
		/*	Seçenek ekle butonuna týklandýþý baþlangýç	*/
		btnAddOption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openAddOptionWindow.show();
			}
		});
		
		/*	Soru ekleme iþleminin bittiði belirten btnDone butonunun týklandýðýnda
		 * 	soru ve þýklar veritabanýna kaydedilip activity sonlandýrýlacak*/
		btnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Soruyu veritabanýna kaydet
				try {
					question = tvQuestion.getText().toString();
					if (question.isEmpty()) {
						throw new IllegalStateException(getResources().getString(R.string.question_added_message_warning_1));
					}
					else if(options.isEmpty()){
						throw new IllegalStateException(getResources().getString(R.string.question_added_message_warning_2));
					}
					db.addQuestionAndOptions(question, options);
					Toast.makeText(AddQuestion.this, getResources().getString(R.string.question_added_message_success), Toast.LENGTH_SHORT).show();
					AddQuestion.this.finish();	//Activity'i sonlandýr
				} catch (Exception e) {
					if (e instanceof IllegalStateException) {
						Toast.makeText(AddQuestion.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(AddQuestion.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		/*	Soruya eklenen seçenekleri sýfýrlar*/
		btnDeleteOptions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tvOptions.setText("");
				options.clear();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_question, menu);
		return true;
	}

}

