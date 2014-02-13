package com.bupt.document;

import java.io.File;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * ��ƪ�ĵ����ĵ�����з����������
 * @author BUPT
 *
 */
public class TestDocument {
	ProcessDocument pd = new ProcessDocument();
	private static Log log = LogFactory.getLog(TestDocument.class);

	/**
	 * ��ƪ�ĵ����ĵ�����з���
	 * @param inputFilePath
	 *       �ļ���
	 * @param outputFile
	 *      �������������ļ���
	 */
	public void documentTest(String inputFilePath, String outputFile){
		int countSenti = 0;
		File file = new File(inputFilePath);

		// �ж��Ƿ����ļ�
		if ((!file.isDirectory()) && (file.getPath().endsWith(".txt"))) {

			// ���û�������ָ�������з���ģ��
			countSenti = pd.documentSemanticProcess(file.getPath(), outputFile);
			System.out.println(countSenti);
		}
		else log.info("INPUT FILE ERROR!");
	}	
	
	public static void main(String[] args) throws Exception
	{
		TestDocument sp = new TestDocument();
		//sp.documentTest("D:\\input\\1n.txt", "D:\\output1.txt");
		sp.documentTest("D:\\1.txt", "D:\\1n.txt");
	
	}
	
	
}
