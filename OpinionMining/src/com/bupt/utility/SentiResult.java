package com.bupt.utility;

/**
 * ������з�������ж����ݽṹ����
 * @author BUPT
 * @version 1.0
 */
public class SentiResult {
	public String topic; //�����
	public String sentiment_word; //��д�
	public int polarity; //��ʾ��ж����桢�����Ȩ�ض�

	public SentiResult(String t,String s,int p)
	{
		topic = t;
		sentiment_word = s;
		polarity = p;
	}
	public SentiResult(String t,int p)
	{
		topic = t;
		sentiment_word = "";
		polarity = p;
	}
	public SentiResult(int p)
	{
		topic = "";
		sentiment_word = "";
		polarity = p;
	}
}
