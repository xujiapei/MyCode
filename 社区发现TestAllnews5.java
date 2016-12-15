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
	int pointNum;//社团总点数
	//double ceDu;//团测度
	StringBuffer dian=null;//用于存社团中全部的点
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
		ArrayList<String> vec_relationship  = new ArrayList<String>();//记录有链接的边
		ArrayList seedlist  = new ArrayList();//记录种子
		Map map = new HashMap();//存点和对应点的邻居节点
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
		map3.putAll(map);//存放点的社区数量
			int m = vec_relationship.size();
			Iterator mapit = map.keySet().iterator();
			PrintWriter out=new PrintWriter(new FileWriter("out.txt"));
			while(mapit.hasNext()){
				String point = mapit.next().toString();
				int count=0;String link="";
				for(int i=0,n=vec_relationship.size();i<n;i++){
					String[] line = vec_relationship.get(i).split(",");
					if(point.equals(line[0])||point.equals(line[1])){
						count++;//计算某点的度值
						if(point.equals(line[0])){
							link=link+","+line[1];//存邻居结点
						}else{link=link+","+line[0];}
					}
				}
				//System.out.println(point+":"+count+";"+link+",");
				map.put(point, count+";"+link+",");
				out.println(point+":"+count+";"+link+",");
				count=0;link="";
				
			}
			out.close();
			
			//按节点的度降序放到种子队列中
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
			
			int communityCount=0;double zz=0.8;//0.6到0.75得的社区比较准确
			double e_c_int=0,e_c_out=0;
			String groupClu = "";
			PrintWriter out1=new PrintWriter(new FileWriter("result.txt"));
			while(seedlist.size()>0){
				//以特定的某点为种子 开始LFM算法
				
				StringBuffer dian=new StringBuffer();
				int pointNum=1;
				dian.append(seedlist.get(0));
				System.out.println("****"+dian);
				//ground ground_c=new ground(seedlist.get(0));//升序0或降序seedlist.size()-1获取种子
				map3.put(seedlist.get(0), Integer.valueOf(map3.get(seedlist.get(0)).toString())+1);//点的社区数量
				seedlist.remove(0);
				//计算社团C邻居节点测度 当有测度大于0时把测度最大的点加入到社团中
				//当全部的邻居节点的测度都小于0时，计算社团C内部每一个点节点测度，把小于0的点从社团C中移除
				int e_in=0,e_out=0,e_in_side=0,e_out_all=0,m3=0;//初始化各变量初值
				double ceDu_max=0,ceDu=0;//社团测度的中间值 ceDuTemp记录没加入点i时的测度
				String point_tem="";//团测度ceDu_max暂时最大时的点，后面没有比此测度再大的话则把此点加入社团C
				//StringBuffer bb = new StringBuffer(","+dian+",");
				do{
					m3=dian.toString().split(",").length;
					String bb = ","+dian+",";//记录社团中各点已算过节点测度的点，避免重复计算同一个邻居节点的节点测度
					//System.out.println("社团中的点为："+ground_c.dian);
					double xx=0,yy=0;
					//计算初始社团的团测度 当团内点数改变时才再重新计算一次
					for(int k2=0;k2<m3;k2++){
							e_out_all=e_out_all+Integer.valueOf(map.get(dian.toString().split(",")[k2]).toString().split(";")[0]);
						}
						for(int iii=0;iii<m3-1;iii++){
							for(int j=iii+1;j<m3;j++){
								if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].contains(","+dian.toString().split(",")[j]+",")){
									e_in_side++;//两端点都在社团C内的边的数目和
									if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].substring(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].indexOf(","+dian.toString().split(",")[j]+",")+1).contains(","+dian.toString().split(",")[j]+",")){
										e_in_side++;
									}
								}
							}
					}
						e_in=2*e_in_side;//没加入点aa时社区的团内度值
						e_out=e_out_all-e_in;//没加入点aa时社区的团外度值
						yy=Double.valueOf(e_in)/java.lang.Math.pow((Double.valueOf(e_in+e_out)),zz);
						//System.out.println("yy = "+yy);
						
				 for(int i=0;i<m3;i++){
					String dianTem = dian.toString().split(",")[i];//点dianTem是社团C中的点
					//找社团C中某点的邻居节点 并计算它们的测度
					String[] pointarray = map.get(dianTem).toString().split(";");
					String[] point_link = pointarray[1].split(",");//存放点dianTem的邻居节点
					//int dyg = 0;//社区每加入一个点i yy的值只需要计算一次
					
					for(int ii=1,nn=point_link.length;ii<nn;ii++){
							//StringBuffer dianTemp =new StringBuffer(dian);
							String aa =point_link[ii];//dianTem的邻居节点
							if(bb.contains(","+aa+",")){
								continue;
							}else{bb=bb+","+aa+",";}
							boolean o=false;
							for(int u=0;u<m3;u++){if(dian.toString().split(",")[u].equals(aa))o=true;}
							if(o){continue;}//确保找到的点不在社团C中
							dian.append(","+aa);//社团加入邻居节点aa
							m3=m3+1;
							e_in=0;e_out=0;e_out_all=0;e_in_side=0;
							for(int k2=0;k2<m3;k2++){
								e_out_all=e_out_all+Integer.valueOf(map.get(dian.toString().split(",")[k2]).toString().split(";")[0]);
							}
							for(int iii=0;iii<m3-1;iii++){
								for(int j=iii+1;j<m3;j++){
									if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].contains(","+dian.toString().split(",")[j]+",")){
										e_in_side++;//两端点都在社团C内的边的数目和
										if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].substring(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].indexOf(","+dian.toString().split(",")[j]+",")+1).contains(","+dian.toString().split(",")[j]+",")){
											e_in_side++;
										}
									}
								}
							}
							m3=m3-1;
							e_in=2*e_in_side;//加入点aa时社区的团内度值
							e_out=e_out_all-e_in;//加入点aa时社区的团外度值
							xx=Double.valueOf(e_in)/java.lang.Math.pow((Double.valueOf(e_in+e_out)),zz);
							ceDu=xx-yy;
							
							//System.out.println(ground_c.dian+" 加入点 "+aa+" 的e_in= "+e_in+" e_out= "+e_out+" 团测度xx="+xx);
							e_in=0;e_out=0;e_in_side=0;e_out_all=0;
							dian.delete(dian.lastIndexOf(","), dian.length());//移除点aa，计算其它邻居节点的测度
							
							//System.out.println("dian= "+dian);
							if(ceDu>ceDu_max){
								ceDu_max=ceDu;
								point_tem=aa;
							}
					}
				}
				if(ceDu_max>0){//把团测度最大的点加入到社团C中
					dian.append(","+point_tem);
					System.out.println("dian= "+dian);
					bb=bb+","+point_tem+",";
					pointNum++;
					e_in=0;e_out=0;
					map3.put(point_tem, Integer.valueOf(map3.get(point_tem).toString())+1);//点的社区数量
					e_in_side=0;e_out_all=0;ceDu_max=0;ceDu=0;
					seedlist.remove(point_tem);
				}else{ceDu_max=-1;}
				}while(ceDu_max>=0);
			
			System.out.println("总点数: "+pointNum);
			System.out.println("社团C里的全部点为: "+dian);
			out1.println(dian);
			groupClu = groupClu+";"+dian;
				communityCount++;
				
				
				//计算Q值中的e_in（团内度值） 和 e_out的值（团外度值）
				e_out_all=e_in_side=e_in=e_out=0;
				for(int k2=0;k2<m3;k2++){
					e_out_all=e_out_all+Integer.valueOf(map.get(dian.toString().split(",")[k2]).toString().split(";")[0]);
				}
				for(int iii=0;iii<m3-1;iii++){
					for(int j=iii+1;j<m3;j++){
						if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].contains(","+dian.toString().split(",")[j]+",")){
							e_in_side++;//两端点都在社团C内的边的数目和
							if(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].substring(map.get(dian.toString().split(",")[iii]).toString().split(";")[1].indexOf(","+dian.toString().split(",")[j]+",")+1).contains(","+dian.toString().split(",")[j]+",")){
								e_in_side++;
							}
						}
					}
				}
				e_in=2*e_in_side;//没加入点aa时社区的团内度值
				e_out=e_out_all-e_in;//没加入点aa时社区的团外度值
				e_c_int=e_c_int+e_in;
				e_c_out=e_c_out+java.lang.Math.pow(e_out,2);
				
		}
			out1.close();		
		
		double Q=(e_c_int/(2*m))-(e_c_out/(4*java.lang.Math.pow(m, 2)));
		System.out.println("网络模块度Q= "+Q);
		Long end=System.currentTimeMillis();
		System.out.println("1共有："+communityCount+" 个社团");
		groupClu = groupClu.substring(groupClu.indexOf(";")+1);
		String[] groupCluarray = groupClu.split(";");
		System.out.println("2共有："+groupCluarray.length+" 个社团");
		//计算重叠点
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
							//System.out.println("社区 "+(a+1)+" 和社区 "+(b+1)+" 重叠的点为 "+pointa[kk]);
							count++;
						}
						}
					
			}
		}*/
		System.out.println("总的重叠点数为= "+count);
		
		//计算EQ值
		double EQ = 0,EQ_temp=0;int m3=0;
		for(int i = 0,n=groupCluarray.length;i<n;i++){
			String[] grouppoint = groupCluarray[i].split(",");//获取第I个社区全部的点
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
						Avw=1.0;//两端点都在社团C内的边的数目和
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
		
		
		System.out.println("EQ："+EQ);
		System.out.println("算法运行时间为："+(end-start)+" ms");
	} catch (IOException e) {
		e.printStackTrace();
	}

 }
}
