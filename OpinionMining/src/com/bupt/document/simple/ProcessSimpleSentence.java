package com.bupt.document.simple;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import com.bupt.utility.ImportSentimentDictionary;
import com.bupt.utility.PathProcessor;
import com.bupt.utility.Punctuation;
import com.bupt.utility.SentiNeg;
import com.bupt.utility.SentiResult;
import com.bupt.utility.Word;

/**
 * �ĵ�����з����еļ򵥾���з���ģ��
 * @author BUPT
 */
public class ProcessSimpleSentence {
	public int orientation = 0;

	/**
	 * �����ĵ�����з����Ļ�������ָ��ļ򵥾��Ӽ���з���ģ��
	 * @param review 
	 *        �Ѿ��зֺõľ����ı���
	 * @return result
	 *        ��ж������б�
	 */
	public LinkedList<SentiResult> syntaxAnalyzeForSemantic(String review)
	{
		LinkedList<SentiResult> result = new LinkedList<SentiResult>();
		ArrayList<Word> subSentence = new ArrayList<Word>();
		String[] sent = review.split(Punctuation.SPACE);
		LinkedList<String> ts = new LinkedList<String>(); //����ʼ���
		LinkedList<Integer> negPos = new LinkedList<Integer>(); //�񶨴����ڷ־��е�λ�ü���
		LinkedList<SentiNeg> sn = new LinkedList<SentiNeg>(); //��дʼ���	
		boolean found = false; //�Ƿ��ҵ������
		boolean hasDun = false;
		int tPOS=0;
		//�Ƿ�����ٺ�
		if(review.contains(Punctuation.PAUSE))
		{
			hasDun = true;
		}
		for(int i = 0 ;  i < sent.length ; i++)
		{
			if(sent[i].indexOf(Punctuation.SLASH) != -1)//����ִʿ������/��
			{
			//�ֱ�Ѵʺʹ��Ա�ǩ����word��
			String srcWord = sent[i].substring(0, sent[i].indexOf(Punctuation.SLASH));
			Word word = new Word(srcWord, Punctuation.EMPTY);
			subSentence.add(word);}
		}
		for(int j = 0; j < subSentence.size(); j++)
		{
			//����ʴ���
			if(ImportSentimentDictionary.topics.containsKey(subSentence.get(j).getSrcWord()))
			{
				found = true;
				ts.add(subSentence.get(j).getSrcWord());
				tPOS=j;
			}
			//�񶨴ʴ���
			else if(ImportSentimentDictionary.negations.contains(subSentence.get(j).getSrcWord()))
			{
				negPos.add(j);
			}
			
			//��дʴ���
			else if(ImportSentimentDictionary.sentiment_words.containsKey(subSentence.get(j).getSrcWord()))
			{	
	            
				if(ImportSentimentDictionary.sentiment_words.get(subSentence.get(j).getSrcWord())>0)//�������дʴ���
				{						
					sn.add(new SentiNeg(subSentence.get(j).getSrcWord(),1,j,1));
				}					
				else//�������дʴ���
				{
					sn.add(new SentiNeg(subSentence.get(j).getSrcWord(),-1,j,1));
				}
			}
			//�ж��Ƿ���д������������γ����ι�ϵ
		
		}
		//δ�ҵ�����ʣ��˳�
		if(!found)
		{
			return result;
		}
		//�ҵ�����ʣ��滻�ٺţ�������������
		
		/*
		if(found&&hasDun)

		{
			review = review.replaceFirst(".+��", ts.get(0)+"��");
			review = review.replaceAll("��.+��", "");
		}
		*/
		
		//��дʼ��Ϻͷ񶨴ʼ�����һһ��Ӧ��
		//������дʣ�������д�ǰ��ʵľ���С��5�ķ񶨴ʣ������Ӧ�ķ񶨴ʼ���
		for(int i = 0; i < sn.size(); i++)
		{
			for(int j = 0; j < negPos.size(); j++)
			{
				//����дʾ���С��5��ǰ��Χ���ҷ񶨴ʣ��ҵ����÷�,ͬʱȨ�ؼ�1
				if((Math.abs(sn.get(i).pos - negPos.get(j)) < 5))
				{
					//���ֻ��һ���񶨴ʣ�Ĭ�϶����е���дʶ�������
					if(negPos.size() != 1)
					{
						//�жϸ���д�֮ǰ����д���񶨴��Ƿ�����޴ǹ�ϵ
						if(i - 1 >= 0)
						{
							//֮ǰ�и�����дʣ���÷񶨴ʲ�����д���Ӱ��
							if(Math.abs(sn.get(i-1).pos - negPos.get(j)) < Math.abs(sn.get(i).pos - negPos.get(j))) 
							{
								continue;
							}
						}
						
						//�жϸ���д�֮�����д���񶨴��Ƿ�����޴ǹ�ϵ
						if(i +1 < sn.size())
						{
							//֮���и�����дʣ���÷񶨴ʲ�����д���Ӱ��
							if(Math.abs(sn.get(i+1).pos - negPos.get(j)) < Math.abs(sn.get(i).pos - negPos.get(j))) 
							{
								continue;
							}
						}							
					}
					
					//ȷ������д���񶨴ʴ����޴ǹ�ϵ
					sn.set(i, new SentiNeg(sn.get(i).sentiment_word, -1*sn.get(i).polarity, sn.get(i).pos, ++sn.get(i).power));
				}
			}
		}
		
		//���������䷨ģ�����Ƿ������Ե�����ʺ���д�
		int weigh = 0;
		int weighTopic = 0; //�ж�����ʵ�����
		for(int i = 0 ; i < ts.size() ; i++)
		{
			for(int j = 0 ; j < sn.size() ; j++)
			{
				//if(ImportSentimentDictionary.patterns.contains(PR.getPathOfCertainWords(review.trim(), ts.get(i), sn.get(j).sentiment_word)))
				//{
					//sentimentΪ����,polarity�����ݿ���Ĭ��Ϊ-9
					weigh = ImportSentimentDictionary.sentiment_words.get(sn.get(j).sentiment_word);
					//��Ȩ�ص�ѡȡ��׼��������дʣ�3�� > �����棨2��
					//����ʵĴ���
					if(weigh == -9)
					{
						//��д�ǰ����ڷ񶨴�
						if(sn.get(j).power != 1)
						{
							//���棬����3�ı���Ȩ��
							if(sn.get(j).polarity == 1)
							{
								//weigh=1;
								weigh = 3*sn.get(j).power;
							}
							//���棬����-3�ı���Ȩ��
							else
							{
								//weigh=-1;
								weigh = -3*sn.get(j).power;
							}								
						}
						//��д�ǰ��û�з񶨴�
						else
						{
							//weigh=1;
							weigh = -3;
						}
					}
					//����ʵĴ���
					else
					{
						//��д�ǰ����ڷ񶨴�
						if(sn.get(j).power != 1)
						{								
							//���棬����2�ı���Ȩ��
							if(sn.get(j).polarity == 1)
							{
								//weigh=1;
								weigh = 2*sn.get(j).power;
							}
							//���棬����-2�ı���Ȩ��
							else
							{
								//weigh=-1;
								weigh = -2*sn.get(j).power;
							}								
						}
						else //Ĭ��Ϊ1
						{
						
						}
					}
					
					//�ж�������Ƿ������ 
					//��������������ݿ���Ĭ��Ϊ-9
					 weighTopic = ImportSentimentDictionary.topics.get(ts.get(i));
					 if(weighTopic == -9)
					 {
						 //��д�Ϊ�����򲻶������Ȩ�ؼ�Ȩ
						 if(weigh < 0)
						 {
							 weighTopic = -1;	
						 }
						 //��д�Ϊ�����������Ȩ��-2
						 else
						 {   //weighTopic=-2;
							 weighTopic = -4;	
						 }
					 }
					 else //�����Ϊ��
					 {
						 weighTopic = 2;
						 if(weigh < 0)
						 {   //weighTopic=1;
							 weighTopic = 2;	
						 }
						 //��д�Ϊ�����������Ȩ��-2
						 else
						 {
							 weighTopic = 1;	
						 }
					 
						 
					 }
					 result.add(new SentiResult(ts.get(i),sn.get(j).sentiment_word,weigh*weighTopic));
				//}
			}
		}
		return result;
	}
}
