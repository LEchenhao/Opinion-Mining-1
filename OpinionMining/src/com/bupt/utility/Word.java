package com.bupt.utility;

/**
 * �����ı�ע�ĴʵĽṹ��
 * @author BUPT
 * @version 1.0
 */
public class Word {
	public Word(String srcWord, String tag) {
		super();
		this.srcWord = srcWord; 
		this.tag = tag;
	}
	private String srcWord; //��
	private String tag; //��ע
	/**/
	public String getView()
	{
		String s = srcWord+sentimentTag;
		return s;
	}
	public String getPosView()
	{
		String s = srcWord+tag;
		return s;
	}
	private String sentimentTag="";
	public String getSTag()
	{
		return sentimentTag;
	}
	public void setSTag(String s)
	{
		sentimentTag = s;
	}
	public String getSrcWord() {
		return srcWord;
	}
	public void setSrcWord(String srcWord) {
		this.srcWord = srcWord;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
