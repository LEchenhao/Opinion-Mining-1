package com.bupt.utility;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.bupt.utility.Step;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

/**
 * ������ϵ���䷨·��������
 * @author BUPT
 * @version 1.0
 */
public class PathProcessor {
	private HashMap<String,Integer> sentiment_words; //��дʼ���
	private HashMap<String,Integer> topics; //����ʼ���
	private LexicalizedParser lp; //stanford�ʻ����������ʵ��
	private Tree root; //������ϵ��ʵ��
	//private boolean foundTopic; //�Ƿ��ҵ������flag
	//private boolean foundSentiment; //�Ƿ��ҵ���д�flag
	private LinkedList<Step> way1 = new LinkedList<Step>(); //�䷨·��1
	private LinkedList<Step> way2 = new LinkedList<Step>(); //�䷨·��2
	
	String relativelyPath=System.getProperty("user.dir"); 
	public PathProcessor( )
	{
		lp = new LexicalizedParser(relativelyPath+"\\xinhuaPCFG.ser.gz");
	}
	
	public PathProcessor(HashMap<String,Integer> S, HashMap<String,Integer> T )
	{
		sentiment_words = S;
		topics = T;
		lp = new LexicalizedParser(relativelyPath+"\\xinhuaPCFG.ser.gz");
	}
	public void setSenti(HashMap<String,Integer> S)
	{
		sentiment_words = S;
	}
	public void setTopic(HashMap<String,Integer> T)
	{
		topics = T;
	}
	
	public LinkedList<Step> getPath(String r)
	{
		LinkedList<Step> result = new LinkedList<Step>();
		String reviewTagged = r.trim();
		way1.clear();
		way2.clear();
		String[] sent =  reviewTagged.split(" ");
		Tree parse = lp.apply(Arrays.asList(sent));
		 root = parse.preOrderNodeList().get(0);
		//parse.pennPrint();
		//foundTopic = getPath1(parse,topics);
		//foundSentiment = getPath2(parse,sentiment_words);
		
		int i = way1.size()-1;
		int j = way2.size()-1;
		for(;i>0&&j>0;i--,j--)
		{
			if(way1.get(i).id!=way2.get(j).id)
			{
				i++;
				
				for(int k = 0 ; k <= i ; k++)
				{
					result.add(way1.get(k));
				}
				for( ; j>=0 ; j--)
				{
					result.add(way2.get(j));
				}
				break;
			}
		}	
		return result;
	}
	
	/**
	 * ���䷨ģ�����Ƿ������Ե�����ʺ���д�
	 * @param r �־�õ��ı���
	 * @param t �����
	 * @param s ��д�
	 * @return ��������ʺ���дʵľ䷨ģ��·��
	 */
	public String getPathOfCertainWords(String r,String t,String s)
	{
		LinkedList<Step> result = new LinkedList<Step>();
		String reviewTagged = r.trim();
		String[] sent =  reviewTagged.split(" ");
		//����������ϵ��
		Tree parse = lp.apply(Arrays.asList(sent));
		//��������õ����ڵ�
		 root = parse.preOrderNodeList().get(0);
		 //����������ϵ���õ������
		//foundTopic = getPath1(parse,t);
		//����������ϵ���õ���д�
		//foundSentiment = getPath2(parse,s);
		
		//�ӵ����ڶ��ʼ�����˵�sentiment
		int i = way1.size()-1;
		int j = way2.size()-1;
		for(;i>0&&j>0;i--,j--)
		{
			//���Ӵ�����ʵ���дʵ�·��
			if(way1.get(i).id!=way2.get(j).id)
			{
				i++;
				
				//�ӵڶ��ʼ�����˵�topic
				for(int k = 1 ; k <= i ; k++)
				{
					result.add(way1.get(k));
				}
				for( ; j>0 ; j--)
				{
					result.add(way2.get(j));
				}
				break;
			}
		}	
		way1.clear();
		way2.clear();
		
		return patternToString(result);
	}
	
	/**
	 * ����������ϵ���õ������
	 * @param t ������ϵ��ʵ��
	 * @param h �����ֵ��
	 * @return �Ƿ��ҵ������
	 */
	public boolean getPath1( Tree t,HashMap<String,Integer> h )//topics
	{
		if(h.containsKey(t.value()))
		{
			//System.out.println(way1);
			way1.add(new Step("TOPIC",t.value(),t.nodeNumber(root)));
			return true;
		}
		else if( t.isLeaf() )
		{
			return false;
		}
		else
		{
			boolean ret = false;
			List <Tree>lk = t.getChildrenAsList();
			for(int wl = 0;wl<lk.size();wl++)
			{
				if(getPath1(lk.get(wl),h))
					ret = true;
			}
			if(ret)
			{
				way1.add(new Step("UP",t.value(),t.nodeNumber(root)));
				return true;
			}
			else
				return false;
		}
	}
	
	/**
	 * ����������ϵ���õ������
	 * @param t ������ϵ��ʵ��
	 * @param top �����
	 * @return �Ƿ��ҵ������
	 */
	public boolean getPath1( Tree t,String top )//topics
	{
		//�ҵ������
		if(t.value().equals(top))
		{
			way1.add(new Step("TOPIC",t.value(),t.nodeNumber(root)));
			return true;
		}
		//Ҷ�ӽڵ�
		else if( t.isLeaf() )
		{
			return false;
		}
		else
		{
			boolean ret = false;
			List <Tree>lk = t.getChildrenAsList();
			for(int wl = 0;wl<lk.size();wl++)
			{
				//ѭ������ÿ���ӽڵ�
				if(getPath1(lk.get(wl),top))
					ret = true;
			}
			if(ret)
			{
				//�������ϵĽڵ�
				way1.add(new Step("UP",t.value(),t.nodeNumber(root)));
				return true;
			}
			else
				return false;
		}
	}
	
	/**
	 * ����������ϵ���õ���д�
	 * @param t ������ϵ��ʵ��
	 * @param h ��д�ֵ��
	 * @return �Ƿ��ҵ���д�
	 */
	public boolean getPath2( Tree t,HashMap<String,Integer> h )//sentiment
	{
		if(h.containsKey(t.value()))
		{
			//System.out.println(t.value());
			way2.add( new Step("SENTIMENT",t.value(),t.nodeNumber(root)) );
			return true;
		}
		else if( t.isLeaf() )
		{
			return false;
		}
		else if(t.value().equals("ADVP"))
		{
			return false;
		}
		else
		{
			boolean ret = false;
			List <Tree>lk = t.getChildrenAsList();
			for(int wl = 0;wl<lk.size();wl++)
			{
				if(getPath2(lk.get(wl),h))
					ret = true;
			}
			if(ret)
			{
				way2.add(new Step("DOWN",t.value(),t.nodeNumber(root)));
				return true;
			}
			else
				return false;
		}
	}
	
	/**
	 * ����������ϵ���õ���д�
	 * @param t ������ϵ��ʵ��
	 * @param s ��д�
	 * @return �Ƿ��ҵ���д�
	 */
	public boolean getPath2( Tree t,String s )//sentiment
	{
		//�ҵ���д�
		if(t.value().equals(s))
		{
			way2.add( new Step("SENTIMENT",t.value(),t.nodeNumber(root)) );
			return true;
		}
		//Ҷ�ӽڵ�
		else if( t.isLeaf() )
		{
			return false;
		}
		//�ų�����
		else if(t.value().equals("ADVP"))
		{
			return false;
		}
		else
		{
			boolean ret = false;
			List <Tree>lk = t.getChildrenAsList();
			for(int wl = 0;wl<lk.size();wl++)
			{
				//ѭ������ÿ���ӽڵ�
				if(getPath2(lk.get(wl),s))
					ret = true;
			}
			if(ret)
			{
				//�������µĽڵ�
				way2.add(new Step("DOWN",t.value(),t.nodeNumber(root)));
				return true;
			}
			else
				return false;
		}
	}
	
	/**
	 * �ѽڵ�·��ת�����ַ���
	 * @param ����ڵ�·���б�
	 * @return ���ذ���·�����ַ���
	 */
	public String patternToString(LinkedList<Step> s)
	{
		String ret = "";
		for(int i = 0 ; i < s.size() ; i++)
		{
			if(i==0)
			{
				ret+=s.get(i).dir+"@"+s.get(i).value;
			}
			else
			{
				ret+="#"+s.get(i).dir+"@"+s.get(i).value;
			}
		}
		
		
		return ret;
	}
	
	/**
	 * ���ַ���ת���ɽڵ�·��
	 * @param patt �������·�����ַ���
	 * @return ����·���ڵ��б�
	 */
	public LinkedList<Step> stringToPattern(String patt)
	{
		LinkedList<Step> steps = new LinkedList<Step>();
		String[] stps = patt.split("#");
		for(int i = 0 ; i < stps.length ; i++)
		{
			String[] temp  = stps[i].split("@");
			
			steps.add(new Step(temp[0],temp[1],0));
		}
		return steps;
	}
}
