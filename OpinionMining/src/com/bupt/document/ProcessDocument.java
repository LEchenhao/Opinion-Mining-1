package com.bupt.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ICTCLAS.kevin.zhang.SentenceSplitter;
import ICTCLAS.kevin.zhang.WordSplitter;
import com.bupt.document.comparative.IdentifyComparative;
import com.bupt.document.comparative.ProcessComparativeSentence;
import com.bupt.document.complex.ProcessComplexSentence;
import com.bupt.document.complex.IdentifyComplex;
import com.bupt.document.simple.ProcessSimpleSentence;
import com.bupt.utility.ImportSentimentDictionary;
import com.bupt.utility.Punctuation;
import com.bupt.utility.SVMResult;
import com.bupt.utility.SearchTopic;
import com.bupt.utility.SentiResult;
import com.bupt.utility.XMLResult;
import com.bupt.zconfigfactory.ConfigFactory;
/**
 * ƪ�¼���з���
 * @author BUPT
 *
 */
public class ProcessDocument {
	private ProcessSimpleSentence ps = new ProcessSimpleSentence(); // �����ᵽ����������Ա����
	private IdentifyComparative search = new IdentifyComparative();
	private IdentifyComplex search1 = new IdentifyComplex();
	static WordSplitter wsplitter;
	private boolean first = false;
	private static Log log = LogFactory.getLog(ProcessDocument.class); //д��־
	//��ʼ��
	ProcessDocument(){
		ConfigFactory.init("AnalysisConfig.xml");//�����ļ���ʼ��
		System.out.println("�ѳ�ʼ�������ļ���");
		ImportSentimentDictionary.init();//����ʵ�
		System.out.println("�ѵ�����дʴʵ䣡");

		try {
			wsplitter = new WordSplitter();
			//�����û��ִʴʵ�                           
		    int n = wsplitter.importUserDict(ConfigFactory.getString("UserDic.name"));
		    System.out.println("�û��ʵ�������"+n);
		} catch (IOException e) {
			log.info("�ִʹ��ߵ����û��ʵ�ʧ�ܣ�");
			e.printStackTrace();
		}
	}

	/**
	 * ��������ָ����ı�����з���
	 * @param inputFile
	 *            �����ļ�
	 * @param outputFile
	 *            ����ļ�
	 * @return ���ĵ�����е÷�
	 */
	public  int documentSemanticProcess(String inputFile, String outputFile){
	
		int nLine = 0; // �����з�ǰ������
		int countSenti = 0; // ��ж��ۺϼ���
		int sentiNeg=0;
		int sentiPos=0;
		int sentiZer=0;
		int sentiClass=-3;
		String rev = "";
		LinkedList<SentiResult> sresult = new LinkedList<SentiResult>();//�򵥾���з������
		LinkedList<SentiResult> cresult = new LinkedList<SentiResult>();//�ȽϾ���з������
		LinkedList<XMLResult> res = new LinkedList<XMLResult>(); //���Ͼ�ʶ����
		 
		// �ļ���д����
		BufferedWriter outf;
		try {
		if (first) {
		
				outf = new BufferedWriter(new FileWriter(outputFile, true));
			
		} else {
			outf = new BufferedWriter(new FileWriter(outputFile));
			first = true;// ��һ����������д�ļ�
		}
		File dir = new File(inputFile);
		FileReader fr = new FileReader(dir);		
		BufferedReader br = new BufferedReader(fr);
		String tmp="";

		while ((rev = br.readLine()) != null) {
			// ����ı��еĿո񣬻��з�
			if (rev.trim().equals(Punctuation.EMPTY) || rev.trim().equals(Punctuation.LINEFEED)) {
				continue;
				}

			// ��ƪ�¼�����з�,����10000���˳�
			nLine++;
			if (nLine > 10000) {
				break;
			}
			// �����з�
			// ��Ϊ��������һ�����������ӵ�������˼Ϊ��λ�з�
			
			
			String[] ReviewAll = SentenceSplitter.splitSentence(rev);
			//System.out.println("ReviewAll.length"+ReviewAll.length);
	
			

			for (int nLength = 0; nLength < ReviewAll.length; nLength++) {
				ReviewAll[0]=ReviewAll[0]+tmp;
				if(ReviewAll.length>1) 
					tmp=ReviewAll[ReviewAll.length-1];				
				else
					tmp="";
				

				// �ִ�
				String newstr1 = wsplitter.split(ReviewAll[nLength], 1);
				// �ȿ�һ�仰���Ƿ��������,������ʾͼ���������
				boolean haveTopic = SearchTopic.haveTopic(newstr1);
				if (haveTopic) {
					// ���Ӿ��ʹ���
					res = search1.identifyComplex(newstr1, ConfigFactory.getString("Complex"));
					if (res.size() > 0) {
						for (int i = 0; i < res.size(); i++) {
							newstr1 = (String) ProcessComplexSentence
							
									.processComplexSentence(newstr1, res
											.get(i).type);
						}
					}
					// �ڶ������Զ��ŵȷ־��Ϊ��λ�з�
                     
					String[] Review = SentenceSplitter.splitSubSentence(newstr1);
					for (int counter = 0; counter < Review.length; counter++) {
						// ����ֿ���ӳ���С��2����˵�/
						if (Review[counter].length() <= 2) {
							continue;
						}
						// �ֿ���ӳ��ȴ���,������ӳ��ȴ���50��ȡǰ50
						else if (Review[counter].length() > 100)
							Review[counter] = Review[counter].substring(0, 99);
				

						// �Ƿ��бȽϹؼ���
						 
						boolean haveKeyword = search.haveComparativeKeyword(Review[counter]);
						
						if (haveKeyword) {
							
                            
							// �ж��Ƿ�Ϊ�ȽϾ�
						LinkedList<SVMResult> iscomparative  = search.identifyComparativeByXML(Review[counter], 
									ConfigFactory.getString("XMLComparative"));//XML����
					
							if (iscomparative.size() > 0) {
							
								for (int i = 0; i < iscomparative.size(); i++) {
									// ���ǱȽϾ�ľ��ӽ�����з���
									cresult = ProcessComparativeSentence
											.processcomparativesentence(
													iscomparative.get(i).type,
													newstr1);
									// ������
									if (cresult.size() > 0) {
										for (int j = 0; j < cresult.size(); j++) {
											outf.write("<content>"
													+ Review[counter]
													+ "</content>");
											outf.newLine();										
											outf.write("<sentiment>"
															+ cresult.get(j).topic
															+ " "
															+ cresult.get(j).sentiment_word
															+ " "
															+ cresult.get(j).polarity
															+ "</sentiment>");
											outf.newLine();
                                            if(cresult.get(j).polarity>0)
                                            	sentiPos++;
                                            else if(cresult.get(j).polarity==0)
                                            	sentiZer++;
                                            else
                                                sentiNeg++;
											//countSenti += cresult.get(j).polarity;											
										//	outf.write("<countSenti>" + " "
											//		+ countSenti);
											//outf.newLine();
										}
									}
									cresult.clear();
								}
							} else
								continue;
						} else {

							// ���ǱȽϾ�����
							// ��з���,��������ָ�����з���ģ��
							sresult = ps.syntaxAnalyzeForSemantic(Review[counter]);
							// ������
							if (sresult.size() > 0) {
								for (int i = 0; i < sresult.size(); i++) {
									outf.write("<content>" + Review[counter]
											+ "</content>");
									outf.newLine();
									outf.write("<sentiment>"
											+ sresult.get(i).topic + " "
											+ sresult.get(i).sentiment_word
											+ " " + sresult.get(i).polarity
											+ "</sentiment>");
									outf.newLine();
                            if(sresult.get(i).polarity>0)
                            	sentiPos++;
                            else if(sresult.get(i).polarity==0)
                            	sentiZer++;
                            else
                            	sentiNeg++;
									//countSenti += sresult.get(i).polarity;
									//outf.write("<countSenti>" + " "
									//		+ countSenti);
									//outf.newLine();
								}
							}

							sresult.clear();
						}
					}
				}
			}

			// �ı�������жȱ��ʵ���ģ��

			// ��ж��ж����
			outf.write("========================================================================");
			outf.newLine();
			/*outf.write("Total count sentiment: " + " " + countSenti);
		    outf.newLine();
			
			if (countSenti < 0) // ����
			{
				outf.write(dir.getName() + " is negative.");
				outf.newLine();
			} else // �Ǹ���
			{
				outf.write(dir.getName() + " isn't negative.");
				outf.newLine();
			}
			outf.write("========================================================================");
			outf.newLine();*/
			if(sentiPos==0&&sentiNeg==0)
				sentiClass=0;
			else if(sentiPos==0)
				sentiClass=-2;
			else if(sentiNeg==0)
				sentiClass=2;
			else if(sentiPos>sentiNeg)
				sentiClass=1;
			else 
				sentiClass=-1;
			outf.write(dir.getName() + " is level "+sentiClass+".");
			outf.newLine();
			
			/*
			outf.write("********************");
			outf.newLine();
			outf.write("<result>"+dir.getName()+" "+sentiClass);
			outf.newLine();
			outf.write("********************");
			outf.newLine();
			*/
			outf.flush();

		}
		} catch (IOException e) {
			log.info("ProcessDoucument ERROR!");
			e.printStackTrace();

		}
		return sentiClass;
	}
}
