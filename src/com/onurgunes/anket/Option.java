package com.onurgunes.anket;

public class Option {
	private int id;
	private int questionId;
	private String option;
	private int selectedCount;

	public Option(){
		
	}
	
	public Option(int id, int questionId, String option, int selectedCount){
		this.id = id;
		this.option = option;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int selectedCount) {
		this.selectedCount = selectedCount;
	}
	
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
}
