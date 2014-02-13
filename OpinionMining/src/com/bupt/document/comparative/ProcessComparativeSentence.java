package com.bupt.document.comparative;
import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bupt.utility.ImportSentimentDictionary;
import com.bupt.utility.Punctuation;
import com.bupt.utility.SentiResult;
import com.bupt.utility.Word;

/**
 * �ȽϾ���з�����
 * @author BUPT
 *
 */
public class ProcessComparativeSentence {
	public static final String COMPAREWORD_BI = "��/p";
	public static final String COMPAREWORD_BI_A = "��";
	public static final String COMPAREWORD_ZUI = "��/d";
	public static final String COMPAREWORD_D = "d";
	public static final String COMPAREWORD_ZUI_A = "��";
	public static final String COMPAREWORD_YIYANG = "һ��/uyy";
	public static final String COMPAREWORD_YIYANG_A = "һ��";
	public static final String COMPAREWORD_YIYANG_A2 = "����";
	public static final String COMPAREWORD_YU = "��";
	private static final Object COMPAREWORD_YIYANG2 = null;

	private static Log log = LogFactory.getLog(ProcessComparativeSentence.class);
	
/**
 * �ȽϾ���з���ģ��
 * @param keyword
 * 		�ȽϹؼ���
 * @param str
 * 		�ȽϾ�
 * @return
 *      �ȽϾ���е÷ּ�
 */
	public  static LinkedList<SentiResult> processcomparativesentence(int type,String str){
    	LinkedList<SentiResult> result = new LinkedList<SentiResult>();//�ȽϾ���з��������
    	ProcessComparativeSentence ps = new ProcessComparativeSentence();//�ȽϾ���з�����ʵ����
      
    	if(type==1)
    	{
    		result = ps.biAnalyze(str);//"��"�־䴦��
    		}
    	else if(type==2)
    	{
    		result = ps.superiorAnalyze(str);//"��"�־䴦��
    		}
    	else if(type==3)
    	{
    		result = ps.equalityAnalyze(str);//"һ��"���ʹ���
    		}  
   
    return result;
	}

	/**
	 * ���Ⱦ����з�������
	 * @param review 
	 *         �ִ�����ַ���
	 * @return LinkedList<SentiResult> 
	 *         ���Ⱦ����е÷ּ�
	 * 
	 */
	public  LinkedList<SentiResult> superiorAnalyze(String review) {
		
		LinkedList<SentiResult> r = new LinkedList<SentiResult>();//���Ⱦ����е÷ּ�
		LinkedList<Integer> negPos = new LinkedList<Integer>(); //�񶨴����ڷ־��е�λ�ü���
		LinkedList<Integer> negPos1 = new LinkedList<Integer>(); //�񶨴����ڷ־��е�λ�ü���
		boolean foundT = false;//�Ƿ��������
		boolean foundS = false;//�Ƿ�����д�
		boolean foundN = false;//�Ƿ��з񶨴�
		boolean foundZ = false;//�Ƿ��бȽϹؼ��ʡ��
		int poszui = 0;//�����λ��
		int spos=0;//��д�λ��
		int s =1 ;//��дʼ���
		String sentiword = null;//��д�����
		int p = 0;//����ʼ���
		String topic  = null;//���������
		int negcount = 0;//�񶨴��롰�ǰ�ľ�����5���ڵķ񶨴ʸ���
		int negcount1 = 0;//�񶨴��롰���ľ�����5���ڵķ񶨴ʸ���
		try {
			String[] sent = review.split(Punctuation.SPACE);
			ArrayList<Word> subSentence = new ArrayList<Word>();

			for(int i = 0 ;  i < sent.length ; i++)
			{
				//����ִʿ������/��
				if(sent[i].indexOf(Punctuation.SLASH) != -1)
				{
				//�ֱ�Ѵʺʹ��Ա�ǩ����word��
				String srcWord = sent[i].substring(0, sent[i].indexOf(Punctuation.SLASH));
				String tag = sent[i].substring(sent[i].indexOf(Punctuation.SLASH) + 1,sent[i].indexOf(Punctuation.SLASH) + 2);
				Word word = new Word(srcWord, tag);
				subSentence.add(word);}
			}
			for(int j = 0; j < subSentence.size(); j++)
			{
				//�����
				if(ImportSentimentDictionary.topics.containsKey(subSentence.get(j).getSrcWord()))
				{
					foundT = true;//�ҵ������
					p = ImportSentimentDictionary.topics.get(subSentence.get(j).getSrcWord());//��������ʵ����ֵ
					topic = subSentence.get(j).getSrcWord();//��������ʵ�����
					
				}
			
				//�ȽϹؼ���
				else if(subSentence.get(j).getSrcWord().equals(COMPAREWORD_ZUI_A)&&subSentence.get(j).getTag().equals(COMPAREWORD_D))
				{
					foundZ = true;//�ҵ��ȽϹؼ��֡��
					poszui = j;//��¼�����λ��
				}
				//�񶨴�
				else if(ImportSentimentDictionary.negations.contains(subSentence.get(j).getSrcWord()))
				{
					foundN = true;
					if(!foundZ){
					negPos.add(j);//��¼���֮ǰ�ķ񶨴�
					}
					else negPos1.add(j);//��¼���֮��񶨴�
				}
				//��д�
				else if(ImportSentimentDictionary.sentiment_words.containsKey(subSentence.get(j).getSrcWord())&&foundZ&&!foundS){
					foundS = true;//����ֺ�ĵ�һ����д�
					s = ImportSentimentDictionary.sentiment_words.get(subSentence.get(j).getSrcWord());//��д�����
					//System.out.println("�ҵ���д�:"+subSentence.get(j).getSrcWord()+"��д�����Ϊ:"+s);
					spos=j;
					if(s>0&&!foundN)s = 1;
					else if(s>0&&foundN) s = 2;//�з񶨴ʣ���Ȩ��Ϊ2
					else if(s<0) s = -3;//������дʣ�Ȩ��Ϊ-3
					//System.out.println("s"+s);
					sentiword = subSentence.get(j).getSrcWord();
				}
				if(j-spos<=2)
					if(subSentence.get(j).getTag().startsWith("n")&&ImportSentimentDictionary.compara_objects.containsKey(subSentence.get(j).getSrcWord())){
					if(ImportSentimentDictionary.compara_objects.get(subSentence.get(j).getSrcWord())<0)
					s=s*(-1);
					}
			}
			//�񶨴���ȽϹؼ��ʵľ���
			for(int neg = 0; neg < negPos.size(); neg++)
			{//�񶨴��롰�ǰ�ľ�����5���ڣ����򲻿���
				if(Math.abs(poszui - negPos.get(neg)) < 5){
					foundN = true;
					negcount++;
				}
			}
			for(int neg = 0; neg < negPos1.size(); neg++)
			{//�񶨴��롰���ľ�����5���ڣ����򲻿���
				if(Math.abs(poszui - negPos1.get(neg)) < 5){
					//foundN1 = true;
					negcount1++;
					s = -1*s;
					//System.out.println(s);
				}
			}
			if(foundT){
				//�������д�
				if(foundS&&s<0)
				{	//�������д��Ҳ������񶨴��������Ϊ����
					if(!foundN&&p>=0||(negcount%2==0)&&p>=0){
						r.add(new SentiResult(topic,sentiword,-5+s*2*(negcount+negcount1+1)));
					}
					//�������д��Ҳ������񶨴��������Ϊ����
					else if(!foundN&&p<0||(negcount%2==0)&&p<0){
						r.add(new SentiResult(topic,sentiword,5+s*(-1)*(negcount+negcount1+1)));
					}
					//�������д��Ұ����񶨴��������Ϊ����
					else if(foundN&&p>=0||(negcount%2==1)&&p>=0){
						r.add(new SentiResult(topic,sentiword,5+s*2*(negcount+negcount1+1)));
					}
					//�������д��Ұ����񶨴��������Ϊ����
					else if(foundN&&p<0||(negcount%2==1)&&p<0){
						r.add(new SentiResult(topic,sentiword,-5+s*(-4)*(negcount+negcount1+1)));
					}
				}
				//�������д�
				else if(foundS&&s>0){
					//�������д��Ҳ������񶨴��������Ϊ����
					if(!foundN&&p>=0||(negcount%2==0)&&p>=0){
						r.add(new SentiResult(topic,sentiword,5+s*2*(negcount+negcount1+1)));
					}
					//�������д��Ҳ������񶨴��������Ϊ����
					else if(!foundN&&p<0||(negcount%2==0)&&p<0){
						r.add(new SentiResult(topic,sentiword,-5+s*(-4)*(negcount+negcount1+1)));
					}
					//�������д��Ұ����񶨴��������Ϊ����
					else if(foundN&&p>=0||(negcount%2==1)&&p>=0){
						r.add(new SentiResult(topic,sentiword,-5+s*2*(negcount+negcount1+1)));
					}
					//�������д��Ұ����񶨴��������Ϊ����
					else if(foundN&&p<0||(negcount%2==1)&&p<0){
						r.add(new SentiResult(topic,sentiword,-5+s*(-1)*(negcount+negcount1+1)));
					}
				}
			}
		}
		catch(Exception e)
		{
			log.info("superiorAnalyze() ERROR!");
			e.printStackTrace();
		}

		return r;
		
	}

	/**
	 * �ȱȾ����з�������
	 * @param review 
	 * 			�ֺôʵ��ַ���
	 * @return LinkedList<SentiResult> 
	 * 			�ȱȾ����е÷ּ�
	 */
	public LinkedList<SentiResult> equalityAnalyze(String review) {
		LinkedList<SentiResult> r = new LinkedList<SentiResult>();//�ȱȾ����е÷ּ�
		LinkedList<Integer> negPos = new LinkedList<Integer>(); //��һ����֮ǰ�ķ񶨴����ڷ־��е�λ�ü���
		LinkedList<Integer> negPos1 = new LinkedList<Integer>(); //��һ����֮��񶨴����ڷ־��е�λ�ü���
		int p = 0;//�ȽϿ���ļ���
		int k = 0;//�Ƚ�����ļ���
		int s = 0;//��дʼ���
		int c = 0;//�Ƚϴʼ���
		int negcount = 0;
		int negcount1 = 0;
		int tPOS=0;
		boolean foundT = false;//�Ƿ��ҵ������
		boolean foundC = false;//�Ƿ��ҵ��Ƚϴ�
		boolean foundS = false;//�Ƿ��ҵ���д�
		boolean foundN = false;//�Ƿ��ҵ��񶨴�
		boolean foundY = false;//�Ƿ��ҵ���һ����
		boolean foundD = false;//�Ƿ��ҵ�����
		int posy = 0;//��һ������λ��
		String topic = null;
		String sentiword  = null;
		try {
			String[] sent = review.split(Punctuation.SPACE);
			ArrayList<Word> subSentence = new ArrayList<Word>();
			for(int i = 0 ;  i < sent.length ; i++)
			{
				if(sent[i].indexOf(Punctuation.SLASH) != -1)//����ִʿ������/��
				{
				//�ֱ�Ѵʺʹ��Ա�ǩ����word��
				String srcWord = sent[i].substring(0, sent[i].indexOf(Punctuation.SLASH));
				String tag = sent[i].substring(sent[i].indexOf(Punctuation.SLASH) + 1,sent[i].indexOf(Punctuation.SLASH) + 2);
				Word word = new Word(srcWord, tag);
				subSentence.add(word);}
				
			}
			for(int j = 0; j < subSentence.size(); j++)
			{
				//�����
				if(ImportSentimentDictionary.compara_objects.containsKey(subSentence.get(j).getSrcWord()))
				{
					foundT = true;//�ҵ������
					
					//δ�ҵ��Ƚϴ�
					if(!foundC)
					{
						k = ImportSentimentDictionary.compara_objects.get(subSentence.get(j).getSrcWord());
											
						//��¼�Ƚ�����ļ���
						topic = subSentence.get(j).getSrcWord();//��¼�Ƚ����������
						tPOS=j;
						}
					//�ҵ��Ƚϴ�
					else if(foundC&&p==0&&!foundY)
					{
						p = ImportSentimentDictionary.compara_objects.get(subSentence.get(j).getSrcWord());
						//��¼�ȽϿ���ļ���
					}
				}
				//�Ƚϴ�
				else if(ImportSentimentDictionary.compara_words.containsKey(subSentence.get(j).getSrcWord())&&!foundY)
				{
					foundC = true;
					c = ImportSentimentDictionary.compara_words.get(subSentence.get(j).getSrcWord());
				}
				else if(((subSentence.get(j).getSrcWord().equals(COMPAREWORD_YIYANG_A))||(subSentence.get(j).getSrcWord().equals(COMPAREWORD_YIYANG_A2)))&&foundC){
					foundY = true;
					posy = j;
				}
				else if(ImportSentimentDictionary.sentiment_words.containsKey(subSentence.get(j).getSrcWord())&&foundC&&!foundD&&!foundS)
				{
					foundS = true;
					s=ImportSentimentDictionary.sentiment_words.get(subSentence.get(j).getSrcWord());
					if(s>0&&!foundN)s = 1;
					else if(s>0&&foundN) s = 2;
					else if(s<0) s = -3;
					sentiword = subSentence.get(j).getSrcWord();
					}
				else if(subSentence.get(j).getSrcWord().contains(Punctuation.COMMA_FULL)&&foundC&&!foundD)
				{
					foundD = true;
				}
				else if(ImportSentimentDictionary.negations.contains(subSentence.get(j).getSrcWord())&&!foundS&&!foundD)
				{
					foundN = true;
					if(!(!foundT&&(subSentence.get(j).getSrcWord().equals("û��")))){
						if(!foundY){
						negPos.add(j);//��¼��һ����֮ǰ�ķ񶨴�
						}
						else negPos1.add(j);//��¼��һ����֮��񶨴�
				}
				}
			}
			for(int neg = 0;( (neg < negPos.size())); neg++)
			{//�񶨴��롰һ����ǰ�ľ�����5���ڣ����򲻿��ǣ����߷񶨴��ڱȽ������ǰ
				
				if((Math.abs(posy - negPos.get(neg)) < 5)||( Math.abs(negPos.get(neg)-tPOS)<5)){
					foundN = true;
					negcount++;
				}
			}
			for(int neg = 0; neg < negPos1.size(); neg++)
			{//�񶨴��롰һ������ľ�����5���ڣ����򲻿���
				if(Math.abs(posy - negPos1.get(neg)) < 5){
					negcount1++;
					s = -1*s;
				}
			}
	
			
			if(foundT){
				//û����д�
				if(!foundS&&s==0)
				{	//�Ƚ�����ͱȽϿ��嶼Ϊ����
					if(k>=0&&p>=0){
						r.add(new SentiResult(topic,sentiword,1));
					}
					//�Ƚ������ȽϿ���������һ��Ϊ����
					else {
						r.add(new SentiResult(topic,sentiword,-6));
					}
				}
				//�������д�
				else if(foundS&&s>0){
					//�ȽϿ���Ϊ����
					if(p<0){
						r.add(new SentiResult(topic,sentiword,-4*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����ȽϿ���Ϊ������û�з�����
					else if(c>0&&k<=0&&p>=0&&!foundN||c>0&&k<0&&p>=0&&(negcount%2==0)){
						r.add(new SentiResult(topic,sentiword,-4*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����ȽϿ���Ϊ������û�з�����
					else if(c>0&&k>=0&&p>=0&&!foundN||c>0&&k>=0&&p>=0&&(negcount%2==0)){
						r.add(new SentiResult(topic,sentiword,2*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����ȽϿ���Ϊ�������з�����
					else if(c<0&&k<=0&&p>=0&&!foundN||c>0&&k<0&&p>=0&&(negcount%2==1)){
						r.add(new SentiResult(topic,sentiword,s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����ȽϿ���Ϊ�������з�����
					else if(c<0&&k>=0&&p>=0&&!foundN||c>0&&k>=0&&p>=0&&(negcount%2==1)){
						r.add(new SentiResult(topic,sentiword,-2*s*(negcount+negcount1+1)));
					}
				}
				//�������д�
				else if(foundS&&s<0){
					//�Ƚ�����Ϊ���ȽϿ���Ϊ�������з�����
					if(c<0&&k<=0&&p<0&&!foundN||c>0&&k<0&&p<0&&(negcount%2==1)){
						r.add(new SentiResult(topic,sentiword,s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ���ȽϿ���Ϊ�������з�����
					else if(c<0&&k>=0&&p<0&&!foundN||c>0&&k>0&&p<0&&(negcount%2==1)){
						r.add(new SentiResult(topic,sentiword,(-1)*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����ȽϿ���Ϊ������û�з�����
					else if(c>0&&k<=0&&p<0&&!foundN||c>0&&k<0&&p<0&&(negcount%2==0)){
						r.add(new SentiResult(topic,sentiword,(-2)*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����ȽϿ���Ϊ������û�з�����
					else if(c>0&&k>=0&&p<0&&!foundN||c<0&&k>=0&&p<0&&(negcount%2==0)){
						r.add(new SentiResult(topic,sentiword,4*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ���ȽϿ���Ϊ����
					else if(k<=0&&p>=0&&(negcount%2==1)){
						r.add(new SentiResult(topic,sentiword,-2*s*(negcount+negcount1+1)));
					}
					else if(k<=0&&p>=0&&(negcount%2==0)){
						r.add(new SentiResult(topic,sentiword,2*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ���ȽϿ���Ϊ�������з�����
					else if(k>=0&&p>=0&&(negcount%2==1)){
						r.add(new SentiResult(topic,sentiword,-1*s*(negcount+negcount1+1)));
					}
					else if(k>=0&&p>=0&&(negcount%2==0)){
						r.add(new SentiResult(topic,sentiword,1*s*(negcount+negcount1+1)));
					}
				}
			}
		}
		catch(Exception e)
		{
			log.info("equalityAnalyze() ERROR!");
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * ���־����з�������
	 * @param review 
	 * 			�ֺôʵ��ַ���
	 * @return LinkedList<SentiResult> 
	 * 			���־����е÷ּ�
	 */
	public  LinkedList<SentiResult> biAnalyze(String review) {
		LinkedList<SentiResult> r = new LinkedList<SentiResult>();
		LinkedList<String> ts = new LinkedList<String>(); //��дʼ���
		LinkedList<Integer> negPos = new LinkedList<Integer>(); //�񶨴����ڷ־��е�λ�ü���
		LinkedList<Integer> negPos1 = new LinkedList<Integer>(); //�񶨴����ڷ־��е�λ�ü���
		int p = 0;
		int s = 0;
		int k = 0;
		int c = 0;
		int negcount = 0;
		int negcount1 = 0;
		boolean foundT = false;
		boolean foundN = false;
		boolean foundC = false;
		boolean foundD = false;
		boolean hasDun = false;
		int pos = 0;
		String topic  = null;
		String sentiword = null;
		try {
			String[] sent = review.split(Punctuation.SPACE);//���ո��з�
			ArrayList<Word> subSentence = new ArrayList<Word>();
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
				String tag = sent[i].substring(sent[i].indexOf(Punctuation.SLASH) + 1,sent[i].indexOf(Punctuation.SLASH) + 2);
				Word word = new Word(srcWord, tag);
				subSentence.add(word);}
			}
			for(int j = 0; j < subSentence.size(); j++)
			{
					//�����
					if(ImportSentimentDictionary.compara_objects.containsKey(subSentence.get(j).getSrcWord()))
					{
						foundT = true;
						//�ڡ��ȡ���ǰ���ֵĴ�Ϊ�Ƚ�����
						if(!foundC&&!foundD)
						{
							k = ImportSentimentDictionary.compara_objects.get(subSentence.get(j).getSrcWord());
							topic = subSentence.get(j).getSrcWord();
							System.out.println("topic"+topic);
							}
						//�ڡ��ȡ��ֺ���ֵĴ�Ϊ�ȽϿ���
						else if(foundC&&!foundD)
						{	
							p = ImportSentimentDictionary.compara_objects.get(subSentence.get(j).getSrcWord());
						
						}
					}
					//�񶨴�
					//���ȡ��ֳ��ֵķ־�֮ǰ�����з񶨴�
					else if(ImportSentimentDictionary.negations.contains(subSentence.get(j).getSrcWord())&&!foundD)
					{
						
						//System.out.println("��д�Ϊ"+subSentence.get(j).getSrcWord()+"��дʵ�����Ϊ"+s);
						foundN = true;
						if(!(!foundT&&(subSentence.get(j).getSrcWord()=="û��"))){
						if(!foundC){							
							negPos.add(j);//��¼���ȡ�֮ǰ�ķ񶨴�
							}
							else negPos1.add(j);//��¼���ȡ�֮��񶨴�
						
					}
					}
					//�Ƚϴ�
					else if(ImportSentimentDictionary.compara_words.containsKey(subSentence.get(j).getSrcWord())&&!foundD)
					{
						if(subSentence.get(j).getSrcWord().equals(COMPAREWORD_BI_A)){
							foundC = true;
							c = ImportSentimentDictionary.compara_words.get(subSentence.get(j).getSrcWord());
							pos = j;
						}
					}
					else if(subSentence.get(j).getSrcWord().contains(Punctuation.COMMA_FULL)&&foundC&&!foundD)
					{
						foundD = true;
					
					}
					//��д�
					else if(ImportSentimentDictionary.sentiment_words.containsKey(subSentence.get(j).getSrcWord())&&foundT)
					{	//�Ƚϴ�֮�󵽶���֮ǰ�ķ־��е���д�
						if(foundC&&!foundD){
						ts.add(sent[j]);
						s = ImportSentimentDictionary.sentiment_words.get(subSentence.get(j).getSrcWord());
						if(s>0&&!foundN)s = 1;
						else if(s>0&&foundN) s = 2;
						else if(s<0) s = -3;						
						sentiword = subSentence.get(j).getSrcWord();
						}
					}
				
				}
				if(hasDun)
				{
//					review = review.replaceFirst(".+��", ts.get(0)+Punctuation.PAUSE);
					review = review.replaceAll("��.+��", Punctuation.EMPTY);
				}
				for(int neg = 0; neg < negPos.size(); neg++)
				{
					//�񶨴��롰�ȡ��ľ�����5���ڣ����򲻿���
					if((Math.abs(pos - negPos.get(neg)) < 5)){
						foundN = true;
						negcount++;
					}
				}
				for(int neg = 0; neg < negPos1.size(); neg++)
				{//�񶨴��롰�ȡ���ľ�����5���ڣ����򲻿���
					if(Math.abs(pos - negPos1.get(neg)) < 5){
						negcount1++;
						s = -1*s;
					}
				}
				//�ȽϿ���Ϊ��������д�����
				if(foundT&&p<0&&s>0)
				{	//�Ƚ�����Ϊ����
					if(k>=0)
					{
						//û�з�����
						if(c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,2*s*(negcount+negcount1+1)));}
						//�з�����
						else if(negcount%2==1||(c<0&&!foundN))r.add(new SentiResult(topic,sentiword,(-4)*s*(negcount+negcount1+1)));
					}
					//�Ƚ�����Ϊ����
					else if(k<0){
						//û�з�����
						if((c>0&&!foundN)||(negcount%2==0&&foundN)){
						r.add(new SentiResult(topic,sentiword,(-4)*s*(negcount+negcount1+1)));}
						//�з�����
						else if((negcount%2==1)||(c<0&&!foundN))r.add(new SentiResult(topic,sentiword,s*(negcount+negcount1+1)));
					}
				}
				//�ҵ�������ұȽϿ���Ϊ��������д�����
				else if (foundT&&p>=0&&s>0)
				{
					//�Ƚ�����Ϊ����
					if(k>=0)
					{
						if(c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,s*(negcount+negcount1+1)));}
						else if(negcount%2==1||c<0&&!foundN)r.add(new SentiResult(topic,sentiword,(-1)*s*(negcount+negcount1+1)));
					}
					else if(k<0){
						if(c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,(-4)*s*(negcount+negcount1+1)));}
						else if(negcount%2==1||c<0&&!foundN)r.add(new SentiResult(topic,sentiword,s*(negcount+negcount1+1)));
					}
				}
				//�ҵ�������ұȽϿ���Ϊ��������дʸ���
				else if(foundT&&p<0&&s<0)
				{
					if(k>=0){
						if(c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,4*s*(negcount+negcount1+1)));}
						else if(negcount%2==1||c<0&&!foundN)r.add(new SentiResult(topic,sentiword,(-1)*s*(negcount+negcount1+1)));
					}
					else if(k<0){
						if(c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,(-2)*s*(negcount+negcount1+1)));}
						else if(negcount%2==1||c<0&&!foundN)r.add(new SentiResult(topic,sentiword,s*(negcount+negcount1+1)));
					}
				}
				//�ҵ�������ұȽϿ���Ϊ��������дʸ���
				else if(foundT&&(p>=0)&&(s<0))
				{
					if(k>=0){
						if( c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,2*s*(negcount+negcount1+1)));}
						else if((negcount%2==1)||(c<0&&!foundN))r.add(new SentiResult(topic,sentiword,(-1)*s*(negcount+negcount1+1)));
						}
					else if(k<0){
						if(c>0&&!foundN||negcount%2==0&&foundN){
						r.add(new SentiResult(topic,sentiword,(-1)*s*(negcount+negcount1+1)));}
						else if(negcount%2==1||c<0&&!foundN)r.add(new SentiResult(topic,sentiword,1*s*(negcount+negcount1+1)));
						}
				}
				
			}
		catch(Exception e)
		{
			log.info("biAnalyze() ERROR!");
			e.printStackTrace();
		}
		return r;
	}
}
