package com.bupt.utility;

/**
 * ��дʻ������ݽṹ��
 * @author BUPT
 * @version 1.0
 */
public class SentiNeg {
	public String sentiment_word; //��д�
	public int polarity; //��ʾ��ж����桢�����Ȩ�ض�,ֻ��+1��-1
	public int pos; //��ʾ��д����ڷ־���е�λ��
	public int power; //��ʾ��д�ǰ��񶨴ʵĴ���Ȩ�أ�һ����1������Ĭ��Ϊ1
	
	public SentiNeg(String s,int ps,int po,int pw)
	{
		sentiment_word = s;
		polarity = ps;
		pos = po;
		power = pw;
	}

	public String getSentiment_word() {
		return sentiment_word;
	}

	public void setSentiment_word(String sentimentWord) {
		sentiment_word = sentimentWord;
	}

	public int getPolarity() {
		return polarity;
	}

	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
}
