/*
 * Anket sistemimizde bir soru ve o sorunun þýklarýný temsil eden sýnýf.
 * */

package com.onurgunes.anket;

import java.util.ArrayList;

public class SurveyItem {
	private Question question;
	private ArrayList<Option> options;
	
	public SurveyItem(){
		
	}
	
	public SurveyItem(Question question, ArrayList<Option> options){
		this.question = question;
		this.options = options;
	}
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public ArrayList<Option> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<Option> options) {
		this.options = options;
	}
}
