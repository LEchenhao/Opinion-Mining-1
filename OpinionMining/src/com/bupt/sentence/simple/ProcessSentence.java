package com.bupt.sentence.simple;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.bupt.utility.ImportSentimentDictionary;
import com.bupt.utility.PathProcessor;
import com.bupt.utility.Punctuation;
import com.bupt.utility.SentiResult;
import com.bupt.utility.Word;
import com.bupt.zconfigfactory.ConfigFactory;
import ICTCLAS.kevin.zhang.ICTCLAS2010;
import ICTCLAS.kevin.zhang.WordSplitter;
import edu.stanford.nlp.trees.Tree;

/**
 * ��з���ģ��
 * @author BUPT
 * @version 1.0
 */
public class ProcessSentence {
	private ICTCLAS2010 wordsplitter = new ICTCLAS2010(); //�ִ�ģ��ʵ��
	private PathProcessor PR = new PathProcessor(); //������ϵ���䷨·��������ʵ��
	public int orientation = 0;

	public ProcessSentence() {
			ConfigFactory.init("D:\\AnalysisConfig.xml");//�����ļ���ʼ��
			System.out.println("�ѳ�ʼ�������ļ���");
			ImportSentimentDictionary.init();//����ʵ�
			System.out.println("�ѵ�����дʴʵ䣡");


			}
	
	/**
	 * ����������ϵ������з���ģ��
	 * @param review �Ѿ��зֺõľ����ı���
	 * @return ��ж������б�
	 * @throws IOException 
	 */
	public LinkedList<SentiResult> syntaxAnalyze(String review) throws IOException
	{
		LinkedList<SentiResult> r = new LinkedList<SentiResult>();
		try {
			WordSplitter wsplitter = new WordSplitter();
			//�����û��ִʴʵ�
		    int n = wsplitter.importUserDict(ConfigFactory.getString("UserDic.name"));
		    System.out.println("�û��ʵ�������"+n);
			//�־�
			String taggedReview = wsplitter.split(review, 0);
			System.out.println(taggedReview);
			String[] sent = taggedReview.split(Punctuation.SPACE);
			LinkedList<String> ts = new LinkedList<String>();//�����
			LinkedList<String> ss = new LinkedList<String>();//��д�
			LinkedList<Integer> negs = new LinkedList<Integer>();//�񶨴�
			boolean found = false;
			boolean hasDun = false;
			//�Ƿ�����ٺ�
			if(taggedReview.contains(Punctuation.PAUSE))
			{
				hasDun = true;
			}
			int pos = -1; //�񶨴ʺ�����ʵľ���
			int neg = 1;
			for(int i = 0 ; i < sent.length ; i++)
			{
				//����ʴ���
				if(ImportSentimentDictionary.topics.containsKey(sent[i]))
				{
					found = true;
					ts.add(sent[i]);
				}
				//��дʴ���
				else	if(ImportSentimentDictionary.sentiment_words.containsKey(sent[i]))
				{
					int a =0;
					if(!ss.contains(sent[i]))
					{
						//�������дʴ���
						if(ImportSentimentDictionary.sentiment_words.get(sent[i])>0)
						{
							ss.addLast(sent[i]);
							negs.addLast(new Integer(1));
							a = 1;
						}
						//�������дʴ���
						else
						{
							ss.addFirst(sent[i]);
							negs.addFirst(new Integer(1));
							a = 2;
						}
					}
					
					//�з񶨴ʣ����Ƿ񶨴ʵ�Ӱ��
					if(pos!=-1)
					{
						if(i-pos<=3)
						{
							if(a ==1 )
							{
								int neN = negs.getLast()*-1;
								negs.removeLast();
								negs.addLast(neN);
							}
							else
							{
								int neN = negs.getFirst()*-1;
								negs.removeFirst();
								negs.addFirst(neN);
							}
						}
						pos = -1;
					}
				}
				//�񶨴ʴ���
				else	if(ImportSentimentDictionary.negations.contains(sent[i]))
				{
					//�Ѿ����˷񶨴ʣ��ж�������ϴεķ񶨴ʵľ���
					if(pos!=-1)
					{
						//�������3������
						if(i-pos>3)
						{
							pos = i;
							neg = -1;
						}
						else
						{
							pos = i;
							neg *=-1;
						}
					}
					//��һ���񶨴ʣ����þ���
					else
					{
						pos = i;
						neg = -1;
					}
				}
			}
			//�ҵ�����ʣ��ٺź���ĺ���
			if(found&&hasDun)
			{
				taggedReview = taggedReview.replaceFirst(".+��", ts.get(0)+Punctuation.PAUSE);
				taggedReview = taggedReview.replaceAll("��.+��", Punctuation.EMPTY);
			}
			//δ�ҵ�����ʣ����Ը��к���
			if(!found&&hasDun)
			{
				taggedReview = taggedReview.replaceAll("[һ-��]+��", Punctuation.EMPTY);
			}
			//����ʡ���дʹ���
			for(int i = 0 ; i < ts.size() ; i++)
			{
				if(!taggedReview.contains(ts.get(i)))
				{
					ts.remove(i);
					i--;
				}
			}
			for(int i = 0 ; i < ss.size() ; i++)
			{
				if(!taggedReview.contains(ss.get(i)))
				{
					ss.remove(i);
					i--;
				}
			}
			
			//���������䷨ģ�����Ƿ������Ե�����ʺ���д�
			for(int i = 0 ; i < ts.size() ; i++)
			{
				for(int j = 0 ; j < ss.size() ; j++)
				{
					if(ImportSentimentDictionary.patterns.contains(PR.getPathOfCertainWords(taggedReview.trim(), ts.get(i), ss.get(j))))
					{
						r.add(new SentiResult(ts.get(i),ss.get(j),negs.get(j)*(ImportSentimentDictionary.sentiment_words.get(ss.get(j)))));
						ts.remove(i);
						ss.remove(j);
						i--;
						break;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	

	/**
	 * ���������ռ�ľ��ͽṹ����з���ģ��
	 * @param review �Ѿ��зֺõľ����ı���
	 * @return ��ж������б�
	 */
	public LinkedList<SentiResult>  windowAnalyze(String review)
	{
		orientation = 0;
		int sentiment = 0;
		int negation = 1;
		String topicw = "";
		ArrayList<Word> subSentence = new ArrayList<Word>();
		try 
		{
			//������ע
			byte nativeBytes[] = wordsplitter.ICTCLAS_ParagraphProcess(review.getBytes("GB2312"), 1);
			String taggedReview = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			//�Կո���ʽ�ִ�
			String[] arrTaggedReview = taggedReview.split("\\s");
			//split by space
			
			for(int i = 0; i < arrTaggedReview.length - 1; i++)
			{
				if(arrTaggedReview[i].indexOf("/") != -1)
				{
					//����ʺͱ�ע
					String srcWord = arrTaggedReview[i].substring(0, arrTaggedReview[i].indexOf("/"));
					String tag = arrTaggedReview[i].substring(arrTaggedReview[i].indexOf("/") + 1,arrTaggedReview[i].indexOf("/") + 2);
					Word word = new Word(srcWord, tag);
					subSentence.add(word);
								
					//��עָʾ��β��ʵ����һ��
					if(tag.equals("w") || i == arrTaggedReview.length - 2) 
					{
						//�����ķ�ʽ����
						for(int j = 0; j < subSentence.size(); j++)
						{
							//we search for a noun, possibly the topic of the sentence
							if(subSentence.get(j).getTag().equals("n"))
							{
								//ȷ���Ƿ�Ϊ��ѡ�������
								int topicPolarity = checkFeature(subSentence.get(j).getSrcWord());
								//if it is a candidate topic
								topicw = subSentence.get(j).getSrcWord();
								if(topicPolarity != 0)
								{								
									subSentence.get(j).setSTag("/topic");
									//��ǰ������д�������ʵľ���
									int indexOfUpFirstModifier = upFirstModifierIndex(subSentence, j);
									//��������д�������ʵľ���
									int indexOfDownModifier = downModifierIndex(subSentence, j);
										
									int up_Orientation = 0;
									int down_Orientation = 0;
									int up_Positive = 1;
									int down_Positive = 1;
									
									//ǰ�������δ�
									if((indexOfUpFirstModifier != -1)&&(indexOfDownModifier != -1))
									{										
										int upDistance = j - indexOfUpFirstModifier;
										int downDistance = indexOfDownModifier - j;
										//��������δ�������ʽ�һЩ
										if(upDistance > downDistance)
										{
											subSentence.get(indexOfDownModifier).setSTag("/modifier");
											if(subSentence.get(indexOfDownModifier).getTag().equals("a") || subSentence.get(indexOfDownModifier).getTag().equals("b"))
											{
												down_Orientation = checkAdjective(subSentence.get(indexOfDownModifier).getSrcWord());
											}
											if(subSentence.get(indexOfDownModifier).getTag().equals("v"))
											{
												down_Orientation = checkVerb(subSentence.get(indexOfDownModifier).getSrcWord());
											}
											down_Positive = isPositive(subSentence, j, indexOfDownModifier);
											down_Orientation *= down_Positive;
										}
										//ǰ������δ�������ʽ�һЩ
										else if (upDistance < downDistance)
										{
											subSentence.get(indexOfUpFirstModifier).setSTag("/modifier");
											if(subSentence.get(indexOfUpFirstModifier).getTag().equals("a") || subSentence.get(indexOfUpFirstModifier).getTag().equals("b"))
											{
												up_Orientation = checkAdjective(subSentence.get(indexOfUpFirstModifier).getSrcWord());
											}
											if(subSentence.get(indexOfUpFirstModifier).getTag().equals("v"))
											{
												up_Orientation = checkVerb(subSentence.get(indexOfUpFirstModifier).getSrcWord());
											}
												
											//��Ҫ��һ����ǰ�����дʺ������֮������д�
											int indexOfUpSecondModifier = upSecondModifierIndex(subSentence, indexOfUpFirstModifier);
											//���������дʺ������֮��ķ��ж�
											up_Positive = isPositive(subSentence, indexOfUpSecondModifier, indexOfUpFirstModifier);
											up_Orientation *= up_Positive;
										}
										//ǰ������δ�������ʵľ�����ȣ�����һ��
										else
										{
											subSentence.get(indexOfDownModifier).setSTag("/modifier");
											subSentence.get(indexOfUpFirstModifier).setSTag("/modifier");
											if(subSentence.get(indexOfDownModifier).getTag().equals("a")|| subSentence.get(indexOfDownModifier).getTag().equals("b"))
											{
												down_Orientation = checkAdjective(subSentence.get(indexOfDownModifier).getSrcWord());
											}
											if(subSentence.get(indexOfDownModifier).getTag().equals("v"))
											{
												down_Orientation = checkVerb(subSentence.get(indexOfDownModifier).getSrcWord());
											}
											down_Positive = isPositive(subSentence, j, indexOfDownModifier);
											down_Orientation *= down_Positive;
												
											if(subSentence.get(indexOfUpFirstModifier).getTag().equals("a") || subSentence.get(indexOfUpFirstModifier).getTag().equals("b"))
											{
												up_Orientation = checkAdjective(subSentence.get(indexOfUpFirstModifier).getSrcWord());
											}
											if(subSentence.get(indexOfUpFirstModifier).getTag().equals("v"))
											{
												up_Orientation = checkVerb(subSentence.get(indexOfUpFirstModifier).getSrcWord());
											}
											
											int indexOfUpSecondModifier = upSecondModifierIndex(subSentence, indexOfUpFirstModifier);
											up_Positive = isPositive(subSentence, indexOfUpSecondModifier, indexOfUpFirstModifier);
											up_Orientation *= up_Positive;
										}										
									}
									//ֻ�к�������δ�
									else if(indexOfUpFirstModifier == -1 && indexOfDownModifier != -1)
									{
										subSentence.get(indexOfDownModifier).setSTag("/modifier");
										if(subSentence.get(indexOfDownModifier).getTag().equals("a") || subSentence.get(indexOfDownModifier).getTag().equals("b"))
										{
											down_Orientation = checkAdjective(subSentence.get(indexOfDownModifier).getSrcWord());
										}
										if(subSentence.get(indexOfDownModifier).getTag().equals("v"))
										{
											down_Orientation = checkVerb(subSentence.get(indexOfDownModifier).getSrcWord());
										}
										down_Positive = isPositive(subSentence, j, indexOfDownModifier);
										down_Orientation *= down_Positive;										
									}
									//ֻ��ǰ������δ�
									else if(indexOfDownModifier == -1 && indexOfUpFirstModifier != -1)
									{
										
										subSentence.get(indexOfUpFirstModifier).setSTag("/modifier");
										if(subSentence.get(indexOfUpFirstModifier).getTag().equals("a") || subSentence.get(indexOfUpFirstModifier).getTag().equals("b"))
										{
											up_Orientation = checkAdjective(subSentence.get(indexOfUpFirstModifier).getSrcWord());
										}
										if(subSentence.get(indexOfUpFirstModifier).getTag().equals("v"))
										{
											up_Orientation = checkVerb(subSentence.get(indexOfUpFirstModifier).getSrcWord());
										}
											
										int indexOfUpSecondModifier = upSecondModifierIndex(subSentence, indexOfUpFirstModifier);
										up_Positive = isPositive(subSentence, indexOfUpSecondModifier, indexOfUpFirstModifier);
										up_Orientation *= up_Positive;
									}
										
									orientation = up_Orientation + down_Orientation;
									/**/
									if(orientation != 0)
									{
										orientation*=topicPolarity;
									}
									else
									{
										continue;
									}

								}
							}
						}
						subSentence.clear();
					}
				}
			}			
		}
		catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		LinkedList<SentiResult> ret = new LinkedList<SentiResult>();
		if(orientation != 0)
		{
			SentiResult sr = new SentiResult(topicw,orientation);
			ret.add(sr);	
		}
		return ret;
	}
	/**
	 * ��ǰ�����������������дʵľ���
	 * @param midSen ���жϵĴ�
	 * @param featureIndex ��������
	 * @return ����ʵ�ʾ���
	 */
	public int upFirstModifierIndex(ArrayList<Word> midSen, int featureIndex)
	{
		int indexOfUpFirstModifier = -1;
		int orientation = 0;
		//�ݼ��ƽ�������ֱ����һ������
		for(int i = 1; (featureIndex - i >= 0) && (!midSen.get(featureIndex - i).getTag().equals("n")) ; i++)
		{
			//���ݴʻ������
			if(midSen.get(featureIndex - i).getTag().equals("a") || midSen.get(featureIndex - i).getTag().equals("b"))
			{
				orientation = checkAdjective(midSen.get(featureIndex - i).getSrcWord());
			}
			//����
			if(midSen.get(featureIndex - i).getTag().equals("v"))
			{
				orientation = checkVerb(midSen.get(featureIndex - i).getSrcWord());
			}
			if(orientation != 0)
			{
				//�ҵ���ѡ����дʣ����¾���
				indexOfUpFirstModifier = featureIndex - i;
				return indexOfUpFirstModifier;
			}				
		}
		return indexOfUpFirstModifier;
	}
	
	/**
	 * �������������������дʵľ���
	 * @param midSen ���жϵĴ�
	 * @param featureIndex ��������
	 * @return ����ʵ�ʾ���
	 */
	public int downModifierIndex(ArrayList<Word> midSen, int featureIndex)
	{
		int indexOfDownModifier = -1;
		int orientation = 0;
		//�����ƽ�������ֱ����һ������
		for(int i = 1; (featureIndex + i < midSen.size()) && (!midSen.get(featureIndex + i).getTag().equals("n")); i++)
		{
			//���ݴʻ������
			if(midSen.get(featureIndex + i).getTag().equals("a") || midSen.get(featureIndex + i).getTag().equals("b"))
			{
				orientation = checkAdjective(midSen.get(featureIndex + i).getSrcWord());
			}
			//����
			if(midSen.get(featureIndex + i).getTag().equals("v"))
			{
				orientation = checkVerb(midSen.get(featureIndex + i).getSrcWord());
			}
			if(orientation != 0)
			{
				//�ҵ���ѡ����дʣ����¾���
				indexOfDownModifier = featureIndex + i;
				return indexOfDownModifier;
			}				
		}
		return indexOfDownModifier;
	}
	
	/**
	 * ��ǰ�����������������дʵľ���
	 * @param midSen
	 * @param indexOfUpFirstModifier
	 * @return
	 */
	public int upSecondModifierIndex(ArrayList<Word> midSen, int indexOfUpFirstModifier)
	{
		int indexOfUpSecondModifier = -1;
		int orientation = 0;
		for(int i = 1; (indexOfUpFirstModifier - i >= 0) && (! midSen.get(indexOfUpFirstModifier - i).getTag().equals("n")) ; i++)
		{
			if(midSen.get(indexOfUpFirstModifier - i).getTag().equals("a") || midSen.get(indexOfUpFirstModifier - i).getTag().equals("b"))
			{
				orientation = checkAdjective(midSen.get(indexOfUpFirstModifier - i).getSrcWord());
			}
			if(midSen.get(indexOfUpFirstModifier - i).getTag().equals("v"))
			{
				orientation = checkVerb(midSen.get(indexOfUpFirstModifier - i).getSrcWord());
			}
			if(orientation != 0)
			{
				indexOfUpSecondModifier = indexOfUpFirstModifier - i;
				return indexOfUpSecondModifier;
			}				
		}
		return indexOfUpSecondModifier;
	}
	
	/**
	 * ��鵱ǰ�ʺ���д�֮��ķ񶨴�
	 * @param midSen
	 * @param preIndex ��ǰ��λ��
	 * @param postIndex ��д�λ��
	 * @return ����
	 */
	public int isPositive(ArrayList<Word> midSen, int preIndex, int postIndex)
	{
		int isPositive = 1;
		if(preIndex == -1)
		{
			for(int i = preIndex + 1; i < postIndex; i++)
			{
				//����
				if(midSen.get(i).getTag().equals("d"))
				{
					isPositive *= checkNegative(midSen.get(i).getSrcWord());
				}
				//���ݴ�
				else if(midSen.get(i).getTag().equals("a"))
				{
					isPositive *= checkAdjective(midSen.get(i).getSrcWord());
				}
			}
		}
		else
		{
			for(int i = preIndex; i < postIndex; i++)
			{
				if(midSen.get(i).getTag().equals("d"))
				{
					isPositive *= checkNegative(midSen.get(i).getSrcWord());
				}
				else if(midSen.get(i).getTag().equals("a"))
				{
					isPositive *= checkAdjective(midSen.get(i).getSrcWord());
				}
			}
		}
		return isPositive;
	}

	/**
	 * ������ݴ��Ƿ�Ϊ��д�
	 * @param adjWord
	 * @return ����дʵ�����
	 */
	public int checkAdjective(String adjWord)
	{
		if(ImportSentimentDictionary.sentiment_words.containsKey(adjWord))
			return ImportSentimentDictionary.sentiment_words.get(adjWord);
		else
			return 0;
	}
	
	/**
	 * ��������Ƿ�Ϊ�����
	 * @param nounWord 
	 * @return ������ʵ�����
	 */
	private int checkFeature(String nounWord)
	{
		if(ImportSentimentDictionary.topics.containsKey(nounWord))
			return ImportSentimentDictionary.topics.get(nounWord);
		else
			return 0;
	}
	
	/**
	 * ����Ƿ�Ϊ�����
	 * @param advWord
	 * @return
	 */
	public int checkNegative(String advWord)
	{
		for(int i = 0; i < ImportSentimentDictionary.negations.size();i++)
		{
			if(ImportSentimentDictionary.negations.get(i).equals(advWord))
			{
				return -1;
			}	
		}
		return 1;
	}
	
	/**
	 * ��鸱���Ƿ�Ϊ��д�
	 * @param verbWord
	 * @return ����дʵ�����
	 */
	public int checkVerb(String verbWord)
	{
		if(ImportSentimentDictionary.sentiment_words.containsKey(verbWord))
			return ImportSentimentDictionary.sentiment_words.get(verbWord);
		else
			return 0;
	}

	/**
	 * 
	 * @param cur
	 * @param root
	 * @param way
	 * @return
	 */
	public String getValue(Tree cur,Tree root,String way)
	{
		
		String [] com = way.split("%");
		//System.out.println(way);
		try{
			for(int i = 0 ; i < com.length ; i++)
			{
				if(com[i].equals("$"))
				{
					//System.out.println(6191937);
					cur = cur.parent(root);
					//System.out.println(cur.value());
				}
				else if(com[i].contains("@"))
				{
					String[] temp = com[i].split("@");
					List<Tree> ts = null;
					if(temp[0].equals("s"))
					{
						ts = cur.siblings(root);
					}
					else if(temp[0].equals("c"))
					{
						ts = cur.getChildrenAsList();
					}
					if(temp[1].contains("="))
					{
						String[] temp2 = temp[1].split("=");
						int at = Integer.parseInt(temp2[0]);
						if(at>0)
						{
							if(ts.get(at-1).value().equals(temp2[1]))
							{
								cur = ts.get(at-1);
							//	System.out.println(cur.value());
							}
						}
						else if (at==0)
						{
							boolean found = false;
							for(int wl = 0 ; wl < ts.size() ; wl++)
							{
								if(ts.get(wl).value().equals(temp2[1]))
								{
									cur = ts.get(wl);
								//	System.out.println(cur.value());
									found = true;
									break;
								}
							}
							if(!found)
							{
								return null;
							}
						}
					}
					else
					{
						int at = Integer.parseInt(temp[1]);
						cur = ts.get(at-1);
						//System.out.println(cur.value());
					}
				}
				else if(com[i].contains("l")&&com[i].contains("#"))
				{
					if(cur.isLeaf())
						return cur.value();
					else
						return null;
				}
				
			}

		}catch(Exception e)
		{
			return null;
		}
		return null;
	}
	


	/**
	 * ����ͳ�Ƶ���з���ģ��
	 * @param review 
	 *        �Ѿ��зֺõľ����ı���
	 * @return 
	 *        ��ж������б�
	 */
	public LinkedList<SentiResult> simpleAnalyze(String review) {
		// TODO Auto-generated method stub
		LinkedList<SentiResult> r = new LinkedList<SentiResult>();
		byte nativeBytes[];
		try {
			//�ִ�
			nativeBytes = wordsplitter.ICTCLAS_ParagraphProcess(review.getBytes("GB2312"), 0);
			String taggedReview = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			String[] sent = taggedReview.split(Punctuation.SPACE);
			boolean foundT = false;
			boolean foundS = false;
			boolean foundV = false;
			for(int i = 0 ;  i < sent.length ; i++)
			{
				//�����
				if(ImportSentimentDictionary.topics.containsKey(sent[i]))
					foundT = true;
				//�����
				else if(ImportSentimentDictionary.action_words.contains(sent[i]))
				{
					foundV = true;
				}
				//�������д��Ҳ����������
				else if(ImportSentimentDictionary.sentiment_words.containsKey(sent[i])&&ImportSentimentDictionary.sentiment_words.get(sent[i])<0)
				{	
					if(!foundV)
					{
						foundS = true;
					}
				}
				
			}
			if(foundT&&foundS)
			{
				r.add(new SentiResult(-9));
			}
		}
		catch(Exception e)
		{
			
		}

		return r;
	}
}
