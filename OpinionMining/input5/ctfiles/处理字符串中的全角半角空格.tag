����/n ����/v :/wp 
ȥ��/v �ַ���/n ��/b ��β/n ��/ude1 ȫ/a ��/n ��/m ��/q �ո�/n ��/wj 

���/v ����/n ��/wp ȫ/a ��/q �ո�/n ��/m λ/q ֵ/n ��/d ��/vshi -/wp 95/m ,/wd ֻҪ/c ��/pba ���/rz ֵ/n ��/vd ��/v 32/m ���/rz ��/m ��/q ��/ude1 �ո�/n ֵ/n ��/wd Ȼ��/c ����/d ����/v �ַ���/n ��/wj 




String/x  a/x  =/m  "/n �� ��/n  1/m "/n ;/wf 
//n //n ��/m ��/q ��/vshi ȫ/a ��/q �ո�/n ��/wd �м�/f Ϊ/v ��/m ��/q �ո�/n  

byte/x [/wkz ]/wky  bytes/x  =/m  a.getBytes/x (/wkz )/wky ;/wf 
for/x  (/wkz int/x  i/x  =/m  0/m ;/wf  i/x  </n  bytes.length/x ;/wf  i++/x )/wky  {/n 
	if/x  (/wkz bytes/x [/wkz i/x ]/wky  ==/m  -/wp 95/m )/wky  
		 bytes/x [/wkz i/x ]/wky  =/m  32/m ;/wf 
		 
}/n 
a=/x  new/x  String/x (/wkz bytes/x )/wky ./wj trim/x (/wkz )/wky  ;/wf 
System.out.println/x (/wkz a/x )/wky ;/wf 





import/x  java.io.BufferedReader/x ;/wf 
import/x  java.io.File/x ;/wf 
import/x  java.io.FileReader/x ;/wf 
import/x  java.io.IOException/x ;/wf 

public/x  class/x  tiqu/x  {/n 

	//n */n */n 
	 */n  @param/x  args/x 
	 */n //n 
	public/x  static/x  void/x  main/x (/wkz String/x [/wkz ]/wky  args/x )/wky  {/n 
		        
				File/x  file/x  =/m  new/x  File/x (/wkz "/n 1.txt/x "/n )/wky ;/wf 
		        BufferedReader/x  reader/x  =/m  null/x ;/wf 
		        try/x  {/n 
		            System.out.println/x (/wkz "/n ��/p ��Ϊ/n ��λ/n ��ȡ/vn �ļ�/n ����/n ��/wd һ/m ��/qv ��/v һ/m ��/m ��/q ��/wp "/n )/wky ;/wf 
		            reader/x  =/m  new/x  BufferedReader/x (/wkz new/x  FileReader/x (/wkz file/x )/wky )/wky ;/wf 
		            String/x  tempString/x  =/m  null/x ;/wf 
		            //n //n  һ/m ��/qv ��/v ��/v һ/m ��/q ��/wd ֱ��/v ����/v null/x Ϊ/p �ļ�/n ����/v 
		            while/x  (/wkz (/wkz tempString/x  =/m  reader.readLine/x (/wkz )/wky )/wky  !/wt =/m  null/x )/wky  {/n 
		                //n //n  ��ʾ/v ��/ng ��/n 
		                System.out.println/x (/wkz tempString/x )/wky ;/wf 
		                String/x  abs/x  =/m  tempString.trim/x (/wkz )/wky ;/wf 
		        		String/x  str2/x  =/m  abs.replaceAll/x (/wkz "/n  "/n ,/wd  "/n \/n \/n |/n "/n )/wky ;/wf 
		        		String/x [/wkz ]/wky  ary/x  =/m  str2.split/x (/wkz "/n \/n \/n |/n "/n )/wky ;/wf 
		        		if/x  (/wkz ary/x [/wkz 0/m ]/wky ./wj equals/x (/wkz "/n ��"/n )/wky )/wky 
		        		{/n 
		        		String/x  s1/x  =/m  ary/x [/wkz 1/m ]/wky ;/wf 
		        		String/x  s2/x  =/m  ary/x [/wkz 2/m ]/wky ;/wf 
		        		System.out.println/x (/wkz "/n s1/x  =/m "/n  +/m  s1/x )/wky ;/wf 
		        		System.out.println/x (/wkz "/n s2/x  =/m "/n  +/m  s2/x )/wky ;/wf 
		        		}/n 
		        		else/x 
		        		{/n 
		        			String/x  s1/x  =/m  ary/x [/wkz 0/m ]/wky ;/wf 
			        		String/x  s2/x  =/m  ary/x [/wkz 1/m ]/wky ;/wf 
			        		System.out.println/x (/wkz "/n s1/x  =/m "/n  +/m  s1/x )/wky ;/wf 
			        		System.out.println/x (/wkz "/n s2/x  =/m "/n  +/m  s2/x )/wky ;/wf 
		        		}/n 
		        		

		            }/n 
		            reader.close/x (/wkz )/wky ;/wf 
		        }/n  catch/x  (/wkz IOException/x  e/x )/wky  {/n 
		            e.printStackTrace/x (/wkz )/wky ;/wf 
		        }/n  finally/x  {/n 
		            if/x  (/wkz reader/x  !/wt =/m  null/x )/wky  {/n 
		                try/x  {/n 
		                    reader.close/x (/wkz )/wky ;/wf 
		                }/n  catch/x  (/wkz IOException/x  e1/x )/wky  {/n 
		                }/n 
		            }/n 
		        }/n 
		

	}/n 
}/n 


