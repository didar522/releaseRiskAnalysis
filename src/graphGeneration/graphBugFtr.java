package graphGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataTemplates.ReleaseCalendarTemplate;
import dataTemplates.WeekCalcTemplate;
import dataTemplates.releaseInfoTemplate;

public class graphBugFtr {

	 ArrayList<WeekCalcTemplate> weekRateData_BFR= new ArrayList <WeekCalcTemplate>();
	 ArrayList<WeekCalcTemplate> weekRateData_FCR= new ArrayList <WeekCalcTemplate> ();
	 ArrayList<WeekCalcTemplate> weekRateData_Commit= new ArrayList <WeekCalcTemplate>();
	 ArrayList<ReleaseCalendarTemplate> releaseInfo = new ArrayList<ReleaseCalendarTemplate> (); 
	 String strFilePath; 
	 
	 ArrayList<releaseInfoTemplate> al_releaseInfoTemplate = new ArrayList <releaseInfoTemplate> (); 
	 
	public graphBugFtr(
			ArrayList<WeekCalcTemplate> weekRateData_BFR, 
			 ArrayList<WeekCalcTemplate> weekRateData_FCR,
			 ArrayList<WeekCalcTemplate> weekRateData_Commit,
			 ArrayList<ReleaseCalendarTemplate> releaseInfo,
			 String strFilePath
			){
		
		this.weekRateData_BFR = weekRateData_BFR; 
		this.weekRateData_FCR = weekRateData_FCR; 
		this.weekRateData_Commit = weekRateData_Commit; 
		this.releaseInfo = releaseInfo;  
		this.strFilePath = strFilePath; 
	}
	
	
	public void performCalculation(){
		
		for (int i=0;i<releaseInfo.size();i++){
			al_releaseInfoTemplate.add(new releaseInfoTemplate (i,releaseInfo.get(i).getReleaseName(), releaseInfo.get(i).getDateReleasEnd(), releaseInfo.get(i).getDblTotalDurationofRelease()));
		}
		
		
		
		//---------------Calculate total reported bugs, features and duration
		int totalBug=0, totalFtr=0; 
		
		for (WeekCalcTemplate iterator:weekRateData_BFR){
			if(iterator.releaseDuration == iterator.releaseCompletion){
				al_releaseInfoTemplate.get(iterator.getReleaseNum()).bugs_reported = (iterator.inO+iterator.inOinC+iterator.inOltC); 
			}
		}	
			
		for (WeekCalcTemplate iterator:weekRateData_FCR){
			if(iterator.releaseDuration == iterator.releaseCompletion){
				al_releaseInfoTemplate.get(iterator.getReleaseNum()).ftr_reported = (iterator.inO+iterator.inOinC+iterator.inOltC);
			}
		}
			
		for (WeekCalcTemplate iterator:weekRateData_BFR){
			if(iterator.releaseDuration>=7){
				if(iterator.releaseCompletion==7){
					al_releaseInfoTemplate.get(iterator.getReleaseNum()).bugsSevenDays = (iterator.inO+iterator.inOinC+iterator.inOltC);
				}
			}
			else if (iterator.releaseDuration==iterator.releaseCompletion)  {
				al_releaseInfoTemplate.get(iterator.getReleaseNum()).bugsSevenDays = (iterator.inO+iterator.inOinC+iterator.inOltC);
			}
		}
		
		for (WeekCalcTemplate iterator:weekRateData_FCR){
			if(iterator.releaseDuration>=7){
				if(iterator.releaseCompletion==7){
					al_releaseInfoTemplate.get(iterator.getReleaseNum()).ftrSevenDays = (iterator.inO+iterator.inOinC+iterator.inOltC);
				}
			}
			else if (iterator.releaseDuration==iterator.releaseCompletion)  {
				al_releaseInfoTemplate.get(iterator.getReleaseNum()).ftrSevenDays = (iterator.inO+iterator.inOinC+iterator.inOltC);
			}
		}
	}		
			
			
			
			
			
			
			
		
	
	
	
	
	
	
	public void printCSVfiles (){
//		try{	
//			
//			File printdest = new File(strFilePath+"/similarity.csv");
//			BufferedWriter out = new BufferedWriter(new FileWriter(printdest));
//			
//			for (int count=0;count<10;count++){
//				out.write(matchedList.get(count).getissueId()+","+matchedList.get(count).similarityvalue );
//				out.newLine();
////				out.write("Similarity -> "+matchedList.get(count).similarityvalue+"\n"); out.newLine();
////				out.write("Priority -> "+matchedList.get(count).getIssuePriority()+"\n"); out.newLine();
//				
//			}
//			
//			out.close(); 
//			
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
	}
	
	
	public static void pythonGraphCreation (){
		
		
		try {
			
			String pythonCodePath = System.getProperty("user.dir")+ "/src/main/java/resources/pythonCodes/"; 
			String command = "";
			command = "py " + pythonCodePath + "barchart.py";
//			System.out.println(command);
			Runtime.getRuntime().exec(command);
			
			
			
//			String pythonCodePath = System.getProperty("user.dir")+ "/src/main/java/resources/pythonCodes/"; 
////			String command = "";
////			command = "python " + pythonCodePath + "barchart.py";
////			
////			System.out.println(command);
//			Runtime.getRuntime().exec("cd "+pythonCodePath);
//			Runtime.getRuntime().exec("python barchart.py");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("problem");
		}
		
//		System.out.println("done");
}
	
	

	
	
	
	
	
}
