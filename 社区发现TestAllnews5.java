package LFM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/*class ground{
	int pointNum;//�����ܵ���
	//double ceDu;//�Ų��
	StringBuffer dian=null;//���ڴ�������ȫ���ĵ�
	ground(String a){
		//this.ceDu=0;
		this.dian.append(a);
		System.out.println("a =  "+a);
		this.pointNum=1;
	}
}*/
public class TestAllnews5 {
	public static void main(String[] arg){
		try {
		Long start=System.currentTimeMillis();
		File file = new File("football_input.txt");
		ArrayList<String> vec_relationship  = new ArrayList<String>();//��¼�����ӵı�
		ArrayList seedlist  = new ArrayList();//��¼����
		Map map = new HashMap();//���Ͷ�Ӧ����ھӽڵ�
		if(file.isFile() && file.exists()){
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"gbk");
			BufferedReader bufferedreader = new BufferedReader(read);
			String Text_line = null;
			String leftId="",rightId="";
			while((Text_line = bufferedreader.readLine())!= null){
					String[] version2 = Text_line.split("\t");
					leftId=version2[0];
					rightId=version2[1];
					if(!map.containsKey(leftId)){map.put(leftId, "0");}
					if(!map.containsKey(rightId)){map.put(rightId, "0");}
					vec_relationship.add(leftId+","+rightId);
			}
			read.close();
			bufferedreader.close();
		}
		Map map3 = new HashMap();
		map3.putAll(map);//��ŵ����������
			int m = vec_relationship.size();
			Iterator mapit = map.keySet().iterator();
			PrintWriter out=new PrintWriter(new FileWriter("out.txt"));
			while(mapit.hasNext()){
				String point = mapit.next().toString();
				int count=0;String link="";
				for(int i=0,n=vec_relationship.size();i<n;i++){
					String[] line = vec_relationship.get(i).split(",");
					if(point.equals(line[0])||point.equals(line[1])){
						count++;//����ĳ��Ķ�ֵ
						if(point.equals(line[0])){
							link=link+","+line[1];//���ھӽ��
						}else{link=link+","+line[0];}
					}
				}
				//System.out.println(point+":"+count+";"+link+",");
				map.put(point, count+";"+link+",");
				out.println(point+":"+count+";"+link+",");
				count=0;link="";
				
			}
			out.close();
			
			//���ڵ�ĶȽ���ŵ����Ӷ�����
			int mapsize = map.keySet().size();
			Map map2 = new HashMap();
			map2.putAll(map);
			while(seedlist.size()<mapsize){
				String dian="",dianTemp2;int degreed=0,degreedTemp=0;
				Iterator mapit2 = map2.keySet().iterator();
				while(mapit2.hasNext()){
					dianTemp2 = mapit2.next().toString();
					 degreedTemp = Integer.valueOf( map2.get(dianTemp2).toString().split(";")[0]);
					 if(degreedTemp>degreed){
						 degreed=degreedTemp;
						 dian=dianTemp2;
						 }
				}
				map2.remove(dian);
				seedlist.add(dian);
			}
			System.out.println("***************"+seedlist);
			//System.out.println("seedlist= "+seedlist.toString());
			
			int communityCount=0;double zz=0.8;//0.6��0.75�õ������Ƚ�׼ȷ
			double e_c_int=0,e_c_out=0;
			String groupClu = "";
			PrintWriter out1=new PrintWriter(new FileWriter("result.txt"));
			while(seedlist.size()>0){
				//���ض���ĳ��Ϊ���� ��ʼLFM�㷨
				
				StringBuffer dian=new StringBuffer();
				int pointNum=1;
				dian.append(seedlist.get(0));
				System.out.println("****"+dian);
				//ground ground_c=new ground(seedlist.get(0));//����0����seedlist.size()-1��ȡ����
				map3.put(seedlist.get(0), Integer.valueOf(map3.get(seedlist.get(0)).toString())+1);//�����������
				seedlist.remove(0);
				//��������C�ھӽڵ��� ���в�ȴ���0ʱ�Ѳ�����ĵ���뵽������
				//��ȫ�����ھӽڵ�Ĳ�ȶ�С��0ʱ����������C�ڲ�ÿһ����ڵ��ȣ���С��0�ĵ������C���Ƴ�
				int e_in=0,e_out=0,e_in_side=0,e_out_all=0,m3=0;//��ʼ����������ֵ
				double ceDu_max=0,ceDu=0;//���Ų�ȵ��м�ֵ ceDuTemp��¼û�����iʱ�Ĳ��
				String point_tem="";//�Ų��ceDu_max��ʱ���ʱ�ĵ㣬����û�бȴ˲���ٴ�Ļ���Ѵ˵��������C
				//StringBuffer bb = new StringBuffer(","+dian+",");
				do{
					m3=dian.toString().split(",").length;
					String bb = ","+dian+",";//��¼�����и���������ڵ��ȵĵ㣬�����ظ�����ͬһ���ھӽڵ�Ľڵ���
					//System.out.println("�����еĵ�Ϊ��"+ground_c.dian);
					double xx=0,yy=0;
					//�����ʼ���ŵ��Ų�� �����ڵ����ı�ʱ�������¼���һ��
					for(int k2=0;k2<m3;k2++){
							e_out_all=e_out_all+Integer.valueOf(map.get(dian.toString().split(",")[k2]).toString().split(";")[0]);
						}
						for(int iii=0;iii<m3-1;iii++){
							for(int j=iii+1;j<m3;j++){
								if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].contains(","+dian.toString().split(",")[j]+",")){
									e_in_side++;//���˵㶼������C�ڵıߵ���Ŀ��
									if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].substring(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].indexOf(","+dian.toString().split(",")[j]+",")+1).contains(","+dian.toString().split(",")[j]+",")){
										e_in_side++;
									}
								}
							}
					}
						e_in=2*e_in_side;//û�����aaʱ���������ڶ�ֵ
						e_out=e_out_all-e_in;//û�����aaʱ�����������ֵ
						yy=Double.valueOf(e_in)/java.lang.Math.pow((Double.valueOf(e_in+e_out)),zz);
						//System.out.println("yy = "+yy);
						
				 for(int i=0;i<m3;i++){
					String dianTem = dian.toString().split(",")[i];//��dianTem������C�еĵ�
					//������C��ĳ����ھӽڵ� ���������ǵĲ��
					String[] pointarray = map.get(dianTem).toString().split(";");
					String[] point_link = pointarray[1].split(",");//��ŵ�dianTem���ھӽڵ�
					//int dyg = 0;//����ÿ����һ����i yy��ֵֻ��Ҫ����һ��
					
					for(int ii=1,nn=point_link.length;ii<nn;ii++){
							//StringBuffer dianTemp =new StringBuffer(dian);
							String aa =point_link[ii];//dianTem���ھӽڵ�
							if(bb.contains(","+aa+",")){
								continue;
							}else{bb=bb+","+aa+",";}
							boolean o=false;
							for(int u=0;u<m3;u++){if(dian.toString().split(",")[u].equals(aa))o=true;}
							if(o){continue;}//ȷ���ҵ��ĵ㲻������C��
							dian.append(","+aa);//���ż����ھӽڵ�aa
							m3=m3+1;
							e_in=0;e_out=0;e_out_all=0;e_in_side=0;
							for(int k2=0;k2<m3;k2++){
								e_out_all=e_out_all+Integer.valueOf(map.get(dian.toString().split(",")[k2]).toString().split(";")[0]);
							}
							for(int iii=0;iii<m3-1;iii++){
								for(int j=iii+1;j<m3;j++){
									if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].contains(","+dian.toString().split(",")[j]+",")){
										e_in_side++;//���˵㶼������C�ڵıߵ���Ŀ��
										if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].substring(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].indexOf(","+dian.toString().split(",")[j]+",")+1).contains(","+dian.toString().split(",")[j]+",")){
											e_in_side++;
										}
									}
								}
							}
							m3=m3-1;
							e_in=2*e_in_side;//�����aaʱ���������ڶ�ֵ
							e_out=e_out_all-e_in;//�����aaʱ�����������ֵ
							xx=Double.valueOf(e_in)/java.lang.Math.pow((Double.valueOf(e_in+e_out)),zz);
							ceDu=xx-yy;
							
							//System.out.println(ground_c.dian+" ����� "+aa+" ��e_in= "+e_in+" e_out= "+e_out+" �Ų��xx="+xx);
							e_in=0;e_out=0;e_in_side=0;e_out_all=0;
							dian.delete(dian.lastIndexOf(","), dian.length());//�Ƴ���aa�����������ھӽڵ�Ĳ��
							
							//System.out.println("dian= "+dian);
							if(ceDu>ceDu_max){
								ceDu_max=ceDu;
								point_tem=aa;
							}
					}
				}
				if(ceDu_max>0){//���Ų�����ĵ���뵽����C��
					dian.append(","+point_tem);
					System.out.println("dian= "+dian);
					bb=bb+","+point_tem+",";
					pointNum++;
					e_in=0;e_out=0;
					map3.put(point_tem, Integer.valueOf(map3.get(point_tem).toString())+1);//�����������
					e_in_side=0;e_out_all=0;ceDu_max=0;ceDu=0;
					seedlist.remove(point_tem);
				}else{ceDu_max=-1;}
				}while(ceDu_max>=0);
			
			System.out.println("�ܵ���: "+pointNum);
			System.out.println("����C���ȫ����Ϊ: "+dian);
			out1.println(dian);
			groupClu = groupClu+";"+dian;
				communityCount++;
				
				
				//����Qֵ�е�e_in�����ڶ�ֵ�� �� e_out��ֵ�������ֵ��
				e_out_all=e_in_side=e_in=e_out=0;
				for(int k2=0;k2<m3;k2++){
					e_out_all=e_out_all+Integer.valueOf(map.get(dian.toString().split(",")[k2]).toString().split(";")[0]);
				}
				for(int iii=0;iii<m3-1;iii++){
					for(int j=iii+1;j<m3;j++){
						if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].contains(","+dian.toString().split(",")[j]+",")){
							e_in_side++;//���˵㶼������C�ڵıߵ���Ŀ��
							if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].substring(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].indexOf(","+dian.toString().split(",")[j]+",")+1).contains(","+dian.toString().split(",")[j]+",")){
								e_in_side++;
							}
						}
					}
				}
				e_in=2*e_in_side;//û�����aaʱ���������ڶ�ֵ
				e_out=e_out_all-e_in;//û�����aaʱ�����������ֵ
				e_c_int=e_c_int+e_in;
				e_c_out=e_c_out+java.lang.Math.pow(e_out,2);
				
		}
			out1.close();		
		
		double Q=(e_c_int/(2*m))-(e_c_out/(4*java.lang.Math.pow(m, 2)));
		System.out.println("����ģ���Q= "+Q);
		Long end=System.currentTimeMillis();
		System.out.println("1���У�"+communityCount+" ������");
		groupClu = groupClu.substring(groupClu.indexOf(";")+1);
		String[] groupCluarray = groupClu.split(";");
		System.out.println("2���У�"+groupCluarray.length+" ������");
		//�����ص���
		int count=0;
		Iterator map3it = map3.keySet().iterator();
		while(map3it.hasNext()){
			String aaa= map3it.next().toString();
			if(Integer.valueOf(map3.get(aaa).toString())>1){count++;}
		}/*
		for(int a=0;a<groupCluarray.length-1;a++){
			String pointalis = groupCluarray[a];
			String[] pointa = pointalis.split(",");
			for(int b=a+1;b<groupCluarray.length;b++){
				String pointblis = groupCluarray[b];
					for(int kk=0;kk<pointa.length;kk++){
						if((","+pointblis+",").contains(","+pointa[kk]+",")){
							//System.out.println("���� "+(a+1)+" ������ "+(b+1)+" �ص��ĵ�Ϊ "+pointa[kk]);
							count++;
						}
						}
					
			}
		}*/
		System.out.println("�ܵ��ص�����Ϊ= "+count);
		
		//����EQֵ
		double EQ = 0,EQ_temp=0;int m3=0;
		for(int i = 0,n=groupCluarray.length;i<n;i++){
			String[] grouppoint = groupCluarray[i].split(",");//��ȡ��I������ȫ���ĵ�
			m3 = grouppoint.length;
			
			for(int iii=0;iii<m3-1;iii++){
				double Qv = Double.valueOf(map3.get(grouppoint[iii]).toString());
				double Kv = Double.valueOf(map.get(grouppoint[iii]).toString().split(";")[0]);
				double Qw = 0;
				double Kw = 0;
				double Avw= 0;
				for(int j=iii+1;j<m3;j++){
					Qw = Double.valueOf(map3.get(grouppoint[j]).toString());
					Kw = Double.valueOf(map.get(grouppoint[j]).toString().split(";")[0]);
					String grouplink = map.get(grouppoint[iii]).toString().split(";")[1];
					if(grouplink.contains(","+grouppoint[j]+",")){
						Avw=1.0;//���˵㶼������C�ڵıߵ���Ŀ��
						if(grouplink.substring(grouplink.indexOf(","+grouppoint[j]+",")+1).contains(","+grouppoint[j]+",")){
							Avw=2.0;
						}
					}
					
					EQ_temp = EQ_temp+(Avw-(Kv*Kw)/(Double.valueOf(2*m)))/(Qv*Qw);
					//System.out.println("grouppoint[iii]="+grouppoint[iii]+" EQ_temp="+EQ_temp+" Avw="+Avw+" Kv="+Kv+" Kw="+Kw+" Qv="+Qv+" Qw="+Qw);
				}
				
			}
		}
		EQ=EQ_temp/(2*m);
		
		
		System.out.println("EQ��"+EQ);
		System.out.println("�㷨����ʱ��Ϊ��"+(end-start)+" ms");
	} catch (IOException e) {
		e.printStackTrace();
	}

 }
}
