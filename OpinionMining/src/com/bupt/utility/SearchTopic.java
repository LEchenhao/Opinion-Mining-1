package com.bupt.utility;


import java.util.Iterator;


/**
 * ����ʲ���
 * @author susie
 *
 */
public class SearchTopic {

/**
 * ����ʲ���
 * @param content
 *        �ִʺ���ַ���
 * @return
 */
@SuppressWarnings("unchecked")
public static boolean haveTopic(String content){
	boolean foundT = false;
	@SuppressWarnings("unused")
	Object val = null;
	Object key = null;

		for (Iterator iter = ImportSentimentDictionary.topics.keySet().iterator(); iter.hasNext();) {
			key = iter.next();
		    val = ImportSentimentDictionary.topics.get(key);
			if(content.contains((String)key)){
				foundT = true;
			}
		}
			return foundT;
	}


}

