package ICTCLAS.kevin.zhang;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//import ate.util.Constant;

/**
 * �ִ�
 * @author BUPT
 *
 */
public class WordSplitter {
//	private static final int ICT_POS_MAP_FIRST =1; //������һ����ע��
	private static final int ICT_POS_MAP_SECOND = 0; //������������ע��
//	private static final int PKU_POS_MAP_SECOND =2;  //���������ע��						                
//	private static final int PKU_POS_MAP_FIRST =3;  //����һ����ע��
	//������Ķ���  
    private ICTCLAS2010 ictclas = null;
    private boolean available ;
   
    public WordSplitter() throws IOException {
       this.ictclas = new ICTCLAS2010();
       this.available = init();
    }
    
/**
 * ��ʼ��
 * @return
 */
    private boolean init() {
       String argu = ".";
       boolean result = false;
       try {
		if (ictclas.ICTCLAS_Init(argu.getBytes("GB2312")) == false) {
		       System.out.println("Init Fail!");
//		       return false;
		   } else
			   {ictclas.ICTCLAS_SetPOSmap(ICT_POS_MAP_SECOND);// ���ô��Ա�ע��
			   result = true;}
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return result;
    }
    
/**
 *  �����û��ֵ�
 * @param userDict
 *         �û��ֵ�·��
 * @return
 * @throws UnsupportedEncodingException
 */
    public int importUserDict(String userDict) {
    	   int nCount = 0;
    	try {
    		if (available) {
    		// ���ص����û��������������Ϊ�û��ֵ�ı�������
			nCount = ictclas.ICTCLAS_ImportUserDict(userDict
			          .getBytes("GB2312"));
           ictclas.ICTCLAS_SaveTheUsrDic();
           }
       else
    	   nCount=0;
    }
     catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return nCount;
    }
/**
 * �ִ�
 * @param str
 *        ���ִ��ַ���
 * @param tag
 *        �����������ʽ
 * @return
 * @throws IOException
 */
    public String split(String str, int tag) throws IOException {
       if (available) {
           String string;
          // ictclas.ICTCLAS_ImportUserDict(Constant.USERDICT.getBytes());
           byte[] bytes = ictclas.ICTCLAS_ParagraphProcess(str
                  .getBytes("GB2312"), tag);
           string = new String(bytes, 0, bytes.length, "GB2312");
           return string.trim();
           
       } else
           return null;
    }
    //�˳�
    public void exit() {
       ictclas.ICTCLAS_Exit();
    }
   
}