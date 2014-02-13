package com.bupt.utility;

/**
 * ����䷨·����·���ڵ���
 * @author BUPT
 * @version 1.0
 */
public class Step {
	public String dir = ""; //��ʾ�ڵ����͡��������� 
	public String value = ""; //�ڵ�Ĵ���ֵ
	public int id=-1; //������ϵ���ڵ���
	public Step(String d,String val,int i)
	{
		dir = d;
		value = val;
		id = i;
	}
	public boolean equals(Step s)
	{
		if(s.dir.equals(dir)&&s.value.equals(value))
			return true;
		else
			return false;
	}
	@Override
	public String toString()
	{
		return dir+"#"+value;
	}

}
