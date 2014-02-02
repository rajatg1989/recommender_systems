package edu.umn.cs.recsys.svd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class AnalysisOfResults {
	
	private static final String WD = "D:\\Classes\\Recommender Systems\\Module 7\\Assignments\\svd-assignment\\target\\analysis";

	public static void main(String args[]) {
		File f=null;
		FileReader fr=null;
		BufferedReader br=null;
		File of=null;
		PrintWriter pw=null;
		
		HashMap<String, List<Double>> RMSEByR=new HashMap<String, List<Double>>();
		HashMap<String, List<Double>> RMSEByU=new HashMap<String, List<Double>>();
		HashMap<String, List<Double>> nDCG=new HashMap<String, List<Double>>();
		HashMap<String, List<Double>> TopN=new HashMap<String, List<Double>>();
		
		TreeMap<String, HashMap<String, Double>> means=new TreeMap<String, HashMap<String, Double>>();
		
		System.out.println("Parsing file...");
		try {
			f=new File(WD+"\\evalresforanalysis.csv");
			of=new File(WD+"\\analout.csv");
			
			String line=null;
			
			fr=new FileReader(f);
			br=new BufferedReader(fr);
			
			pw=new PrintWriter(of);
			int lineCount=0;
			while((line=br.readLine())!=null) {
				if(lineCount==0) {
					lineCount++;
					continue;
				}
				System.out.println("On line "+lineCount+1);
				String key="";
				StringTokenizer st=new StringTokenizer(line, ",");
				String algo=st.nextToken();
				String dataSet=st.nextToken();
				String nnbrs="";
				if(!algo.equalsIgnoreCase("PersMean") && !algo.equalsIgnoreCase("ItemItem")) {
					nnbrs=st.nextToken();
				}
				//key=algo+","+dataSet+","+nnbrs;
				//key=algo+","+dataSet;
				key=nnbrs;
				System.out.println("Analysing "+key);
				List<Double> hm=null;
				if(RMSEByR.containsKey(key)) {
					hm=RMSEByR.get(key);
				} else {
					hm=new ArrayList<Double>();
				}
				hm.add(Double.parseDouble(st.nextToken()));
				RMSEByR.put(key, hm);
				hm=null;
				if(RMSEByU.containsKey(key)) {
					hm=RMSEByU.get(key);
				} else {
					hm=new ArrayList<Double>();
				}
				hm.add(Double.parseDouble(st.nextToken()));
				RMSEByU.put(key, hm);
				hm=null;
				if(nDCG.containsKey(key)) {
					hm=nDCG.get(key);
				} else {
					hm=new ArrayList<Double>();
				}
				hm.add(Double.parseDouble(st.nextToken()));
				nDCG.put(key, hm);
				if(st.hasMoreTokens()) {
					hm=null;
					if(TopN.containsKey(key)) {
						hm=TopN.get(key);
					} else {
						hm=new ArrayList<Double>();
					}
					hm.add(Double.parseDouble(st.nextToken()));
					TopN.put(key, hm);
				}
				lineCount++;
			}
			br.close();
			fr.close();
			System.out.println("Calculating means..");
			for(String key:RMSEByR.keySet()) {
				List<Double> vals=RMSEByR.get(key);
				int size=vals.size();
				double sum=0d;
				for(int i=0;i<size;i++) {
					sum=sum+vals.get(i);
				}
				double mean=sum/size;
				if(means.containsKey(key)) {
					means.get(key).put("RMSEByR", mean);
				} else {
					HashMap<String, Double> meanMap=new HashMap<String, Double>();
					meanMap.put("RMSEByR", mean);
					means.put(key, meanMap);
				}
			}
			for(String key:RMSEByU.keySet()) {
				List<Double> vals=RMSEByU.get(key);
				int size=vals.size();
				double sum=0d;
				for(int i=0;i<size;i++) {
					sum=sum+vals.get(i);
				}
				double mean=sum/size;
				if(means.containsKey(key)) {
					means.get(key).put("RMSEByU", mean);
				} else {
					HashMap<String, Double> meanMap=new HashMap<String, Double>();
					meanMap.put("RMSEByU", mean);
					means.put(key, meanMap);
				}
			}
			for(String key:nDCG.keySet()) {
				List<Double> vals=nDCG.get(key);
				int size=vals.size();
				double sum=0d;
				for(int i=0;i<size;i++) {
					sum=sum+vals.get(i);
				}
				double mean=sum/size;
				if(means.containsKey(key)) {
					means.get(key).put("nDCG", mean);
				} else {
					HashMap<String, Double> meanMap=new HashMap<String, Double>();
					meanMap.put("nDCG", mean);
					means.put(key, meanMap);
				}
			}
			for(String key:TopN.keySet()) {
				List<Double> vals=TopN.get(key);
				int size=vals.size();
				double sum=0d;
				for(int i=0;i<size;i++) {
					sum=sum+vals.get(i);
				}
				double mean=sum/size;
				if(means.containsKey(key)) {
					means.get(key).put("TopN", mean);
				} else {
					HashMap<String, Double> meanMap=new HashMap<String, Double>();
					meanMap.put("TopN", mean);
					means.put(key, meanMap);
				}
			}
			System.out.println("Writing to file...");
			//pw.println("Algorithm,DataSet,NNbrs,RMSEByRating,RMSEByUser,nDCG,TopNnDCG");
			//pw.println("Algorithm,DataSet,RMSEByRating,RMSEByUser,nDCG,TopNnDCG");
			//pw.println("Algorithm,DataSet,FeatureCount,RMSEByRating,RMSEByUser,nDCG,TopNnDCG");
			pw.println("FeatureCount,RMSEByRating,RMSEByUser,nDCG,TopNnDCG");
			for(String key:means.keySet()) {
				HashMap<String, Double> meanMap=means.get(key);
				pw.print(key+",");
				if(meanMap.containsKey("RMSEByR")) {
					pw.print(meanMap.get("RMSEByR"));
				}
				pw.print(",");
				if(meanMap.containsKey("RMSEByU")) {
					pw.print(meanMap.get("RMSEByU"));
				}
				pw.print(",");
				if(meanMap.containsKey("nDCG")) {
					pw.print(meanMap.get("nDCG"));
				}
				pw.print(",");
				if(meanMap.containsKey("TopN")) {
					pw.print(meanMap.get("TopN"));
				}
				//pw.print(",");
				pw.println("");
			}
			
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
