package com.bupt.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.bupt.zconfigfactory.ConfigFactory;
/**
 * �����ڴʵ䡢�Ƚ�ģʽ���
 * @author susie
 *
 */
public class ImportSentimentDictionary {	
	public static HashMap<String,Integer> sentiment_words = new HashMap<String,Integer>(); //��дʼ���
	public static LinkedList<String> patterns = new LinkedList<String>(); //�䷨ģ�鼯��
	public static HashMap<String,Integer> topics = new HashMap<String,Integer>(); //����ʼ���
	public static HashMap<String,Integer> compara_objects = new HashMap<String,Integer>(); //����ʼ���
	public static LinkedList<String> negations = new LinkedList<String>(); //�񶨴ʼ���
	public static LinkedList<String> action_words = new LinkedList<String>(); //�����Դʼ���
	public static HashMap<String,Integer> compara_words = new HashMap<String,Integer>(); //�Ƚϴʼ���
	public static HashMap<String,Integer> keywords = new HashMap<String,Integer>(); //�ȽϹؼ��ּ���
	public static ArrayList<String> birules = new ArrayList<String>();//"��"�־����
	public static ArrayList<String> zuirules = new ArrayList<String>(); //"��"�־����
	public static ArrayList<String> yiyangrules = new ArrayList<String>(); //"һ��"�����
	
public	static void init() 
{
			try {
				sentiment_words=TextToDict.getHashMap(ConfigFactory.getString("TextToHash.sentiment_words"));
			    topics=TextToDict.getHashMap(ConfigFactory.getString("TextToHash.topics"));
			    compara_objects = TextToDict.getHashMap(ConfigFactory.getString("TextToHash.compare_objects"));
			    compara_objects.putAll(topics);
			    negations=TextToDict.getLinkList(ConfigFactory.getString("TextToHash.negation_words"));
			    patterns=TextToDict.getLinkList(ConfigFactory.getString("TextToHash.patterns"));
			    action_words=TextToDict.getLinkList(ConfigFactory.getString("TextToHash.action_words"));
			    negations.addAll(action_words);
			    compara_words=TextToDict.getHashMap(ConfigFactory.getString("TextToHash.compare_words"));
			    keywords=TextToDict.getHashMap(ConfigFactory.getString("TextToHash.compare_keywords"));	
			    birules = TextToDict.getArrayList(ConfigFactory.getString("TextToHash.bi"));
			    yiyangrules = TextToDict.getArrayList(ConfigFactory.getString("TextToHash.yiyang"));
			    zuirules = TextToDict.getArrayList(ConfigFactory.getString("TextToHash.zui"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

	}

}






     		


