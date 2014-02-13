package com.bupt.document.complex;

import com.bupt.utility.Punctuation;


/**
 * ���Ӿ�ת��Ϊ�򵥾䴦��ģ��
 * input��String ԭ���Ӿ� & type ���Ӿ�����
 * output��restring ת����ļ򵥾�
 * @author BUPT
 *
 */
public class ProcessComplexSentence {
	public static final String CONJUNCTION_A = "��ʹ/c";
	public static final String CONJUNCTION_B = "����/c";	 
	public static final String CONJUNCTION_C = "����/c";
	public static final String CONJUNCTION_D = "����/c";
	public static final String CONJUNCTION_E = "����/c";
	public static final String CONJUNCTION_F = "��Ȼ/c";
	public static final String CONJUNCTION_G = "û��/v";
	public static final String CONJUNCTION_H = "û��/d";
	
	
	/**
	 * ���Ӿ�ת��Ϊ�򵥾�
	 * @param str
	 *        �ִʺ�ĸ��Ӿ�
	 * @param type
	 *        ���Ӿ�����
	 * @return
	 * 
	 * 
	 */
	public static String processComplexSentence(String str,int type){
		String restring = "";
		
		//���һ�ĸ��Ӿ䴦�������䲻һ��Ϊ�棬����ʡ��
		if(type==1);
		
		//�����ĸ��Ӿ䴦�������������Ӿ����٣�����Ӿ䶨Ϊ�棬ֻȡ����Ӿ�
		else if(type==2)
		{
			String []Review = str.split(Punctuation.COMMA_A);//�����ŷ־�
			for(int counter = 0 ; counter < Review.length ; counter++)
			{
				//System.out.println(Review[counter]);
				String[] sent = Review[counter].split(Punctuation.SPACE);

				for(int i = 0; i < sent.length; i++)
				{
					if(sent[i].contentEquals(CONJUNCTION_A)||sent[i].contentEquals(CONJUNCTION_B)||sent[i].contentEquals(CONJUNCTION_C)
							||sent[i].contentEquals(CONJUNCTION_D)||sent[i].contentEquals(CONJUNCTION_E)||sent[i].contentEquals(CONJUNCTION_F))
					{
					Review[counter]="";break;
					}
				
				}
			 if(!Review[counter].isEmpty()){
				if(restring.isEmpty())restring = restring+Review[counter];
			    else restring = restring+Punctuation.COMMA_A+Review[counter];
			 }
		   }
		}
		
		//������ĸ��Ӿ䴦�������Ӿ�ͽ���Ӿ䶼Ϊ��
		else if(type==3)
		{
			String[] Review = str.split(Punctuation.SPACE);
			for(int counter = 0; counter < Review.length; counter++)
			{
				if(Review[counter].contentEquals(CONJUNCTION_G)||Review[counter].contentEquals(CONJUNCTION_H))continue;
				else{
					restring = restring+Punctuation.SPACE+Review[counter];
				}
			}
		}

		return restring;
	}
}
