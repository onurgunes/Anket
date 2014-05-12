package com.onurgunes.anket;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "MyDb";
	private static int DATABASE_VERSION = 1;
	private static final String TABLE_QUESTION = "sorular";
	private static final String TABLE_OPTIONS = "secenekler";
	private static final String[] QUESTION_COLUMNS = {"Id","Soru"};
	private static final String[] OPTIONS_COLUMNS = {"Id", "SoruId", "Secenek", "SecilimSayisi"};
	
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SORU = "CREATE TABLE sorular (Id INTEGER PRIMARY KEY, Soru TEXT)";
		String CREATE_SECENEK = "CREATE TABLE secenekler (Id INTEGER PRIMARY KEY, SoruId INTEGER, Secenek TEXT, SecilimSayisi INTEGER)";
		
		db.execSQL(CREATE_SORU);
		db.execSQL(CREATE_SECENEK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS sorular");
		this.onCreate(db);
	}

	//Veritabanýndaki son sorunun id bilgisini getiren fonksiyon
	private int getLastQuestionId(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT Id FROM " + TABLE_QUESTION + " ORDER BY Id DESC LIMIT 1;", null);
		cursor.moveToFirst();
//		Log.i("Last id",String.valueOf(cursor.getInt(0)));
		if(cursor.isNull(0))
			return 0;
		return (cursor.getInt(0));
		
	}
	
	//Sorular tablosundan soru sayýsýný getiren fonksiyon
	public int getQuestionCount(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(Id) FROM "+ TABLE_QUESTION, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	//Soru id sine göre sorunun þýk sayýsýný getiren fonksiyon
	public int getOptionCount(int questionId){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(Id) FROM "+ TABLE_OPTIONS + " WHERE SoruId = " + questionId, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	//Soru ve soruya ait þýklarý veritabanýna kaydeden fonksiyon
	public void addQuestionAndOptions(String question, ArrayList<String> options){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(QUESTION_COLUMNS[1], question);
		db.insert(TABLE_QUESTION, null, values);
		values.clear();
		int questionId = getLastQuestionId();
		for (int i = 0; i < options.size() ; i++) {
			values.put(OPTIONS_COLUMNS[1], questionId);
			values.put(OPTIONS_COLUMNS[2], options.get(i));
			values.put(OPTIONS_COLUMNS[3], 0);
			db.insert(TABLE_OPTIONS, null, values);
		}
		db.close();
//		Log.i("Insert Completed","OK");
	}
	
	// Verilen soru id ye ait soru ve þýklarý silen fonksiyon
	public void deleteQuestionAndItsOptions(int questinId){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OPTIONS, OPTIONS_COLUMNS[1] + "=" + questinId, null);//Seçenekleri sil
		db.delete(TABLE_QUESTION, QUESTION_COLUMNS[0] + "=" + questinId, null);//Soruyu sil
	}
	
	//Gelen id ye göre sorularý ve o sorularýn þýklarýný getiren fonksiyon
	public SurveyItem getQuestionAndItsOptionById(int id){
		SurveyItem item = new SurveyItem();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursorQuestion = db.rawQuery("SELECT Id,Soru FROM " + TABLE_QUESTION + " WHERE Id = " + id, null);
		cursorQuestion.moveToFirst();
		item.setQuestion(new Question(cursorQuestion.getInt(0),cursorQuestion.getString(1)));
		cursorQuestion.close();
		
		ArrayList<Option> options = new ArrayList<Option>();
		Cursor cursorOptions = db.rawQuery("SELECT * FROM " + TABLE_OPTIONS + " WHERE SoruId = " + id, null);
		if(cursorOptions.getCount() > 0){
//			Log.i("cursorOptions.getcount",String.valueOf(cursorOptions.getCount()));
			cursorOptions.moveToFirst();
//			Log.i("","ilkine geldi");
			do{
				Option o = new Option();
				o.setId(cursorOptions.getInt(0));
				o.setQuestionId(0);
				o.setOption(cursorOptions.getString(2));
				o.setSelectedCount(0);
				options.add(o);
			}while(cursorOptions.moveToNext());
		}
		item.setOptions(options);
		return item;
	}
	
	//Vertitabanýndan tüm sorularý ve soru idlerini getiren fonksiyon
	public ArrayList<Question> getAllQuestions(){
		ArrayList<Question> questions = new ArrayList<Question>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_QUESTION, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				 questions.add(new Question(cursor.getInt(0), cursor.getString(1)));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return questions;
	}
	
	//Ýstatistikleri sýfýrlayan fonksiyon
	public void resetStatistics(){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(OPTIONS_COLUMNS[3], 0);
		db.update(TABLE_OPTIONS, values, null, null);
		db.close();
	}
	
	//Doldurulan anketi gerekli tablolara kaydeden fonksiyon
	public void saveStatistics(int optionId){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.rawQuery("SELECT SecilimSayisi FROM "+ TABLE_OPTIONS + " WHERE Id = " + optionId, null);
		cursor.moveToFirst();
		values.put(OPTIONS_COLUMNS[3], ( cursor.getInt(0) + 1) );
//		Log.i("eski deðer", String.valueOf(cursor.getInt(0)));
		db.update(TABLE_OPTIONS, values, " Id = ? ", new String[]{String.valueOf(optionId)});
		cursor = db.rawQuery("SELECT SecilimSayisi FROM "+ TABLE_OPTIONS + " WHERE Id = " + optionId, null);
		cursor.moveToFirst();
//		Log.i("yeni deðer", String.valueOf(cursor.getInt(0)));
		db.close();
	}
	
	//Tüm sorulara ait istatistikleri getiren fonksiyon
	public ArrayList<SurveyItem> getStatistics(){
		ArrayList<SurveyItem> items = new ArrayList<SurveyItem>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursorQuestion = db.rawQuery("SELECT Id, Soru FROM " + TABLE_QUESTION, null);
//		Log.i("cursorQuestion.getCount()",String.valueOf( cursorQuestion.getCount() ));
		if(cursorQuestion.getCount() > 0){
			cursorQuestion.moveToFirst();
			do{
				SurveyItem item = new SurveyItem();
				item.setQuestion(new Question(cursorQuestion.getInt(0),cursorQuestion.getString(1)));
				int id = cursorQuestion.getInt(0);
				ArrayList<Option> options = new ArrayList<Option>();
				Cursor cursorOptions = db.rawQuery("SELECT * FROM " + TABLE_OPTIONS + " WHERE SoruId = " + id, null);
//				Log.i("cursorOptions.getCount()",String.valueOf( cursorOptions.getCount() ));
				if(cursorOptions.getCount() > 0){
					cursorOptions.moveToFirst();
					do{
						Option o = new Option();
						o.setId(cursorOptions.getInt(0));
						o.setQuestionId(cursorOptions.getInt(1));
						o.setOption(cursorOptions.getString(2));
						o.setSelectedCount(cursorOptions.getInt(3));
						options.add(o);						
					}while(cursorOptions.moveToNext());
//					Log.i("seçenekler geldi","OK");
					item.setOptions(options);
//					Log.i("Soru",item.getQuestion().getQuestion());
					items.add(item);
				}
				
			}while(cursorQuestion.moveToNext());
		}
		
		cursorQuestion.close();
		return items;
	}
}
