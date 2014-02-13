package ICTCLAS.kevin.zhang;

public class ICTCLAS2010 {
   
    static {
       System.loadLibrary("ICTCLAS2010");
    }
   
    //��ʼ��
    public native boolean ICTCLAS_Init(byte[] sPath);
    //�˳�
    public native boolean ICTCLAS_Exit();
    //�����û��ʵ�
    public native int ICTCLAS_ImportUserDict(byte[] sPath);
    //��ȡuni����
    public native float ICTCLAS_GetUniProb(byte[] sWord);
    //�жϴʵ�����û�������
    public native boolean ICTCLAS_IsWord(byte[] sWord);
    //һ�����ֵķִ�
    public native byte[] ICTCLAS_ParagraphProcess(byte[] sSrc, int bPOSTagged);
    //һ���ı��ļ��ķִ�
    public native boolean ICTCLAS_FileProcess(byte[] sSrcFilename,
           byte[] sDestFilename, int bPOSTagged);
    public native byte[] nativeProcAPara(byte[] src);
    public native int ICTCLAS_AddUserWord(byte[] sWord);
    public native int ICTCLAS_SaveTheUsrDic();
    public native int ICTCLAS_DelUsrWord(byte[] sWord);
    public native int ICTCLAS_KeyWord(byte[] resultKey, int nCountKey);
    public native long ICTCLAS_FingerPrint();
    public native int ICTCLAS_SetPOSmap(int nPOSmap);
    public native int ICTCLAS_GetElemLength(int nIndex);
 
}