package com.c72;

public class Question {
	private int sid;
	private int qid;
	private String qtype;
	private String qtitle;
	private String Answer_a;
	private String Answer_b;
	private String Answer_c;
	private String Answer_d;

	public Question() {}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getSid() {
		return sid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public int getQid() {
		return qid;
	}
	public void setQtype(String qtype) {
		this.qtype = qtype;
	}
	public String getQtype() {
		return qtype;
	}
	public void setQtitle(String qtitle) {
		this.qtitle = qtitle;
	}
	public String getQtitle() {
		return qtitle;
	}
	public void setAnswerA(String Answer_a) {
		this.Answer_a = Answer_a;
	}
	public String getAnswerA() {
		return Answer_a;
	}
	public void setAnswerB(String Answer_b) {
		this.Answer_b = Answer_b;
	}
	public String getAnswerB() {
		return Answer_b;
	}
	public void setAnswerC(String Answer_c) {
		this.Answer_c = Answer_c;
	}
	public String getAnswerC() {
		return Answer_c;
	}
	public void setAnswerD(String Answer_d) {
		this.Answer_d = Answer_d;
	}
	public String getAnswerD() {
		return Answer_d;
	}
}
