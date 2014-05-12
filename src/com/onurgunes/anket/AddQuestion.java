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
		
		/*	Se�enek ekleme penceresinin ba�lang�c�	*/
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setTextSize(16);
        builder.setTitle("Soruya Se�enek Ekle");
        builder.setView(input);
        //Se�enek ekleme penceresinde tamam a bas�ld���nda yap�lacaklar
        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String inputText = input.getText().toString();
				options.add(inputText);
				tvOptions.setText(tvOptions.getText() + "\n" + inputText);
				input.setText("");
			}
		});
        builder.setNegativeButton("�ptal", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Se�enek ekleme penceresi kapat�lacak
				dialog.cancel();		
			}
		});
		openAddOptionWindow = builder.create();
		/*	Se�enek ekleme penceresinin biti�i	*/
		
		/*	Se�enek ekle butonuna t�kland��� ba�lang��	*/
		btnAddOption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openAddOptionWindow.show();
			}
		});
		
		/*	Soru ekleme i�leminin bitti�i belirten btnDone butonunun t�kland���nda
		 * 	soru ve ��klar veritaban�na kaydedilip activity sonland�r�lacak*/
		btnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Soruyu veritaban�na kaydet
				try {
					question = tvQuestion.getText().toString();
					if (question.isEmpty()) {
						throw new IllegalStateException("Soru bo� b�rak�lamaz");
					}
					else if(options.isEmpty()){
						throw new IllegalStateException("Se�enek girmediniz");
					}
					db.addQuestionAndOptions(question, options);
					Toast.makeText(AddQuestion.this, "Soru ba�ar�yla eklendi", Toast.LENGTH_SHORT).show();
					AddQuestion.this.finish();	//Activity'i sonland�r
				} catch (Exception e) {
					if (e instanceof IllegalStateException) {
						Toast.makeText(AddQuestion.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(AddQuestion.this, "Soru eklenirken bir sorunla kar��la��ld�", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		
		/*	Soruya eklenen se�enekleri s�f�rlar*/
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

