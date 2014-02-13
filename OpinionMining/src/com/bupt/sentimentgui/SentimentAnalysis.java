/********************************************************************************
 ** Form generated from reading ui file 'sentimentAnalysis.jui'
 **
 ** Created: ���� һ�� 4 17:40:02 2012
 **      by: Qt User Interface Compiler version 4.5.2
 **
 ** WARNING! All changes made in this file will be lost when recompiling ui file!
 ********************************************************************************/

package com.bupt.sentimentgui;
import com.bupt.document.TestDocuments;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTextBrowser;
import com.trolltech.qt.gui.QMessageBox;


public class SentimentAnalysis extends QDialog {

	public QDialogButtonBox om;
	public static QTextBrowser result;
	public QLineEdit chooseFileLineEdit;
	public QLineEdit saveFileLineEdit;
	public QLabel label;
	public QLabel label_2;
	public QPushButton chooseFileBtn;
	public QPushButton saveFileBtn;
	public QLabel label_3;
	public QLabel label_4;
	public QPushButton startBtn;
	public QPushButton exitBtn;

	public SentimentAnalysis() {
		super();
	}

	public void setupUi(QDialog SentimentAnalysis) {
		SentimentAnalysis.setObjectName("SentimentAnalysis");
		SentimentAnalysis.setEnabled(true);
		SentimentAnalysis.resize(new QSize(567, 449)
				.expandedTo(SentimentAnalysis.minimumSizeHint()));
		result = new QTextBrowser(SentimentAnalysis);
		result.setGeometry(new QRect(110, 221, 401, 141));
		chooseFileLineEdit = new QLineEdit(SentimentAnalysis);
		chooseFileLineEdit.setGeometry(new QRect(110, 110, 291, 22));
		saveFileLineEdit = new QLineEdit(SentimentAnalysis);
		saveFileLineEdit.setGeometry(new QRect(110, 170, 291, 22));
		label = new QLabel(SentimentAnalysis);
		label.setGeometry(new QRect(20, 110, 81, 21));
		QFont font = new QFont();
		font.setFamily("\u5b8b\u4f53");
		label.setFont(font);
		label_2 = new QLabel(SentimentAnalysis);
		label_2.setGeometry(new QRect(20, 160, 81, 41));
		chooseFileBtn = new QPushButton(SentimentAnalysis);
		chooseFileBtn.setGeometry(new QRect(420, 110, 88, 24));
		chooseFileBtn.clicked.connect(this, "chooseFileBtnWork()");
		
		saveFileBtn = new QPushButton(SentimentAnalysis);
		saveFileBtn.setGeometry(new QRect(420, 170, 88, 24));
		saveFileBtn.clicked.connect(this, "saveFileBtnWork()");

		label_3 = new QLabel(SentimentAnalysis);
		label_3.setGeometry(new QRect(20, 230, 72, 131));
		label_4 = new QLabel(SentimentAnalysis);
		label_4.setGeometry(new QRect(150, 10, 301, 51));
		QFont font1 = new QFont();
		font1.setFamily("\u5fae\u8f6f\u96c5\u9ed1");
		font1.setPointSize(18);
		label_4.setFont(font1);

		startBtn = new QPushButton(SentimentAnalysis);
		startBtn.setGeometry(new QRect(310, 390, 88, 24));
		startBtn.clicked.connect(this, "startBtnWork()");
		exitBtn = new QPushButton(SentimentAnalysis);
		exitBtn.setGeometry(new QRect(420, 390, 88, 24));
		exitBtn.clicked.connect(this, "exitBtnWork()");
		retranslateUi(SentimentAnalysis);
	} // setupUi

	public void startBtnWork() {
		
		QMessageBox msgBox=new QMessageBox();
		

	
		
		//startBtn.setEnabled(false);
		String input = chooseFileLineEdit.text().replaceAll("\\\\", "\\\\\\\\");
		String output = saveFileLineEdit.text().replaceAll("\\\\", "\\\\\\\\");
	
		if(input.isEmpty()||output.isEmpty()){
			msgBox.setText("·��δ��д");
					msgBox.show();}
		
		else{
		ProcessTask.input = input;
		ProcessTask.output = output;
		ProcessTask.result = null;

		//��̨�����߳�
		Thread processThread = new Thread(new Runnable() {
			@Override
			public void run() {
				TestDocuments sp = new TestDocuments();
				try {
					sp.documentsTest(ProcessTask.input,
							ProcessTask.output);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		processThread.start();
		
	    //ѭ����ش�����
		while (true) {
			if (ProcessTask.result != null) {
				result.clear();
				result.append(ProcessTask.result);
				break;
			} else {
				result.clear();
				result.append("The system is running, please wait... ");
				QCoreApplication.processEvents();
			}
		}
		startBtn.setEnabled(true);
		
		}
	}

	public void exitBtnWork() {
		dispose();
		System.exit(0);
	}

	public void chooseFileBtnWork() {
		String txtSet = QFileDialog.getExistingDirectory(this, "ѡ��������ļ�",
				chooseFileLineEdit.text());
		chooseFileLineEdit.setText(txtSet);
	}

	public void saveFileBtnWork() {
		String savePath = QFileDialog.getSaveFileName(this, "ѡ�񱣴�Ŀ¼",
				saveFileLineEdit.text());
		saveFileLineEdit.setText(savePath);
		
	}

	public QTextBrowser getQTextBrowser() {
		return result;
	}

	void retranslateUi(QDialog SentimentAnalysis) {
		SentimentAnalysis.setWindowTitle(com.trolltech.qt.core.QCoreApplication
				.translate("SentimentAnalysis", "SentimentAnalysis", null));
		label.setText("�������ı�");
		label_2.setText("�����ļ���");
		chooseFileBtn.setText("ѡ��...");
		saveFileBtn.setText("���...");
		label_3.setText("�������");
		label_4.setText("���������Ʒ������Ϣ����з���ģ��");
		startBtn.setText("����");
		exitBtn.setText("�˳�");
	} // retranslateUi

}
