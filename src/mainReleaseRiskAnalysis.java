

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import dataInput.*;
import dataTemplates.*;
import weekRateCalc.*;




public class mainReleaseRiskAnalysis {

	//-------------------------- Details related to input file location -----------------------------
	
	public static String strFilePath = System.getProperty("user.dir")+ "/src/resources/InputOutput/";
	public static String strFileName = "Summary_Out.xlsx"; 
	public static String strSheetName = strFileName; 
	public static int intStartingRowofData = 1;
	
	//Key input string for analyzing in this program------------------------
	public static String searchString = ""; 
	
	//-------------------------- Details related to input file location -----------------------------
	
	
	//-------------------------- Declaration of all key Arraylists used throughout the program  -----------------------------
	
	public static HashMap<String, Integer> excelFileIndex = new HashMap <String, Integer> ();  
	public static HashMap<String, Integer> bur_ftr_FileIndex = new HashMap <String, Integer> ();  
	public static ArrayList<DataIssueTemplate> IssueData = new ArrayList<DataIssueTemplate>();  
	public static ArrayList<String> strlistUniqueTags = new ArrayList<String> (); 
	public static ArrayList<DataIssueTemplate> bugIssueData = new ArrayList<DataIssueTemplate>();  
	public static ArrayList<DataIssueTemplate> ftrIssueData = new ArrayList<DataIssueTemplate>();  
	
	public static ArrayList<ReleaseCalendarTemplate> releaseInfo = new ArrayList<ReleaseCalendarTemplate> ();
	public static ArrayList<WeekCalcTemplate> weekRateData_BFR = new ArrayList <WeekCalcTemplate> ();
	public static ArrayList<WeekCalcTemplate> weekRateData_DFR = new ArrayList <WeekCalcTemplate> ();
	public static ArrayList<WeekCalcTemplate> weekRateData_FCR = new ArrayList <WeekCalcTemplate> ();
	public static ArrayList<WeekCalcTemplate> weekRateData_FOR = new ArrayList <WeekCalcTemplate> ();
	public static ArrayList<WeekCalcTemplate> weekRateData_Commit = new ArrayList <WeekCalcTemplate> ();
		
	//-------------------------- Declaration of all key Arraylists used throughout the program  -----------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//-------------------------- Declaration of Topic Model & TFIDF Setup  -----------------------------
//	public static int intAnalysisStart4mWeek=1;   
//	public static int AnalysisUptoWeek=getBugIssueData().size();
//	public static int numTopics=3; 
//	public static double taAlpha = 0.1; 
//	public static int taIteration = 1; 
//	public static String topicFilesFilePath = strFilePath+ "/Topic files"; 
//	public static double  luceneThreshold =0.1; 
	
	// The en-token.bin file need to be placed in the file-path declared in variable strFilePath = "C:/Users/Didar/Desktop/Summary/";
	// The en-token file is available is resources folder. Or you can use getResource as well. 
	
	//-------------------------- Declaration of Topic Model & TFIDF Setup -----------------------------
	
	
	
	public static void main(String[] args) {
		
		
		ReadingExcelsheet (IssueData, intStartingRowofData, strFilePath, strFileName, strSheetName); 
	
		//----------------------Optional: If we want to select only bugs and perform analysis within this subset. 
		//-------------------- Writing all unique tags in a UniqueTags.xlsx file for user to read and differentiate between bug and feature related tags. 
		//-------------------- Default is Bug and others in terms of tag division.
		
		IdentifyingUniqueTagsFromAllIssues ();
		DistributingIssuesInMultipleTags ();
		
		PerformCalculation (); 
//		ReadingExcelsheet (bugIssueData, intStartingRowofData, strFilePath, strFileName, "Issue_Bug"); 
//		ReadingExcelsheet (ftrIssueData, intStartingRowofData, strFilePath, strFileName, "Issue_Ftr"); 
		
		System.out.println("============================reported");
		calc_reportedbugs (); 
		System.out.println("=====================7=======reported");
		calc_reportedbugs7day (); 
		System.out.println("============================ratio");
		calc_bugftrratio (); 
		
		//----------------------Optional: If we want to select only bugs and perform analysis within this subset. 
		
		
		
		//----------------------Optional: If we want to select only bugs and perform analysis within this subset. 
		
//		TopicModeling ();
		
		// resultJSONObject holds the final results for the program.  
//		JSONObject resultJSONObject = performingTFIDF ();
		
//		pythonGraphCreation (); 
		
		
		
	}
	
	
	public static void ReadingExcelsheet (ArrayList<DataIssueTemplate> tmp_IssueData, 
			int tmp_intStartingRowofData, 
			String tmp_strFilePath, 
			String tmp_strFileName, 
			String tmp_strSheetName){
		
		DataReadExcelFiles objDataReadExcelFiles = new DataReadExcelFiles(tmp_IssueData, tmp_intStartingRowofData, tmp_strFilePath, tmp_strFileName, tmp_strSheetName); 
		excelFileIndex = objDataReadExcelFiles.createColumnIndex(0); 
		objDataReadExcelFiles.readExcelFiles(false);
		
		for (DataIssueTemplate iterator:IssueData){
			if(iterator.getStrIssueType().contains("Bug")|| iterator.getStrIssueType().contains("bug")|| iterator.getStrIssueType().contains("BUG")){
				bugIssueData.add(iterator);
			}
			else{
				ftrIssueData.add(iterator); 
			}
		}
		
		
		
		
		
		
//		---------------Debug Sensor console output - For sake of checking excel index output only 0---------------------
//		Iterator<String> keySetIterator = excelFileIndex.keySet().iterator(); 
//		while(keySetIterator.hasNext()){ 
//			String key = keySetIterator.next(); 
//			System.out.println("key: " + key + " value: " + excelFileIndex.get(key)); 
//		}
		
//		---------------Debug Sensor console output - for sake of checking output of the issue data list ------------------
//		for (DataIssueTemplate issueCounter:IssueData ){
//			System.out.println(issueCounter.getStrAffectVersion());	
//		}
	}
	
	
	public static void IdentifyingUniqueTagsFromAllIssues (){
		
		ReadingExcelsheet (IssueData, intStartingRowofData, strFilePath, strFileName, strSheetName); 
		
		DataFindUniqueTags objDataFindUniqueTags = new DataFindUniqueTags (strFilePath, strFileName, strFileName, IssueData, strlistUniqueTags); 
		strlistUniqueTags = objDataFindUniqueTags.identifyUniqueLabels();
		
		
		//-------------------- Writing all unique tags in a UniqueTags.xlsx file for user to read and differentiate between bug and feature related tags. (Default is Bug and others in terms of tag division.)
		objDataFindUniqueTags.writeTagsInExcel();
		
		
		
//		---------------Debug Sensor console output - printing all tags ------------------
//		 for (String counter:strlistUniqueTags){
//			 System.out.println(counter);
//		 }
	}
	
	
	public static void DistributingIssuesInMultipleTags (){
		int intStartingRowofData = 1;
		DataReadExcelFiles objDataReadExcelFiles = new DataReadExcelFiles(IssueData,intStartingRowofData, strFilePath, strFileName, strFileName); 
		excelFileIndex = objDataReadExcelFiles.createColumnIndex(0); 
		objDataReadExcelFiles.readExcelFiles(false);
		
		
		String uniqueTagFileName = "UniqueTags.xls";
		ArrayList<String> unTagSheetName = new ArrayList <String> ();
		unTagSheetName.add("Bug"); 
		unTagSheetName.add("Feature");
		DataReadUniqueTags objDataReadUniqueTags = new DataReadUniqueTags (strFilePath, strFileName, uniqueTagFileName, unTagSheetName, IssueData,  strlistUniqueTags); 
		
		//---------------if any modification done by user in tag distribution. We can read it from the excel file after modification. Excel wiriting need to be on for that.
//		objDataReadUniqueTags.readUniqueLabels();
		
		objDataReadUniqueTags.differentiateBugVsFtr();
		
		
		
		
		
		
		
	}
	
	
	public static void PerformCalculation (){
//		================== Explanation: Declaration of different variable to pass and store values so that they are available to all the functions. ===============================
//		ArrayList<String> unTagSheetName = new ArrayList <String> ();
//		unTagSheetName.add("Issue_Bug"); 
//		unTagSheetName.add("Issue_Ftr");	
//		unTagSheetName.add("Issue_Commit");
//		int intStartingRowofData;
//		
//		HashMap<String, Integer> excelFileIndex_bug = new HashMap <String, Integer> ();  
//		HashMap<String, Integer> excelFileIndex_ftr = new HashMap <String, Integer> ();  
//		HashMap<String, Integer> excelFileIndex_commit = new HashMap <String, Integer> (); 
//		
////		=============Explanation: Reading main issue file to perform action on this file ================== 
//		intStartingRowofData = 3;
//		DataReadExcelFiles objDataReadExcelFiles = new DataReadExcelFiles(IssueData, intStartingRowofData, strFilePath, strFileName, strFileName); 
//		excelFileIndex = objDataReadExcelFiles.createColumnIndex(1); 
//		objDataReadExcelFiles.readExcelFiles(true);
//		
//		
////		=========================Explanation: Reading issue and feature tabs to get and utilize the data ======================
//		intStartingRowofData = 1;
//		DataReadExcelFiles objDataReadExcelFiles_bug = new DataReadExcelFiles(bugIssueData, intStartingRowofData, strFilePath, strFileName, unTagSheetName.get(0)); 
//		excelFileIndex_bug = objDataReadExcelFiles_bug.createColumnIndex(0); 
//		objDataReadExcelFiles_bug.readExcelFiles(false);
//		
//		intStartingRowofData = 1;
//		DataReadExcelFiles objDataReadExcelFiles_ftr = new DataReadExcelFiles(ftrIssueData, intStartingRowofData, strFilePath, strFileName, unTagSheetName.get(1)); 
//		excelFileIndex_ftr = objDataReadExcelFiles_ftr.createColumnIndex(0); 
//		objDataReadExcelFiles_ftr.readExcelFiles(false);
		
		
		//turn on for commit 
		
//		intStartingRowofData = 1;
//		DataReadExcelFiles objDataReadExcelFiles_commit = new DataReadExcelFiles(commitIssueData, intStartingRowofData, strFilePath, strFileName, unTagSheetName.get(2)); 
//		excelFileIndex_commit = objDataReadExcelFiles_commit.createColumnIndex(0); 
//		objDataReadExcelFiles_commit.readExcelFiles(false);
		

//		------------------For sake of debugging ------------------
//		System.out.println("+++++++++++++++"+bugIssueData.get(1).getStrKey());
//		System.out.println("+++++++++++++++"+bugIssueData.get(1).getDateCreated());
//		System.out.println("+++++++++++++++"+bugIssueData.get(1).getDateResolved());

//		============================Explanation: Perform week date calculation ==========================
		collectedReleasInfo();
		WeekDateCalc objWeekDateCalc_BFR = new WeekDateCalc (weekRateData_BFR, releaseInfo); 
		objWeekDateCalc_BFR.weekDateCalc();
		
		WeekDateCalc objWeekDateCalc_DFR = new WeekDateCalc (weekRateData_DFR, releaseInfo); 
		objWeekDateCalc_DFR.weekDateCalc();
		
		WeekDateCalc objWeekDateCalc_FCR = new WeekDateCalc (weekRateData_FCR, releaseInfo); 
		objWeekDateCalc_FCR.weekDateCalc();
		
		WeekDateCalc objWeekDateCalc_FOR = new WeekDateCalc (weekRateData_FOR, releaseInfo); 
		objWeekDateCalc_FOR.weekDateCalc();
		
		
//		for commit start this
		
//		WeekDateCalc objWeekDateCalc_Commit = new WeekDateCalc (weekRateData_Commit, releaseInfo); 
//		objWeekDateCalc_Commit.weekDateCalc();
		
		
//		===========================Explanation: calculation of different rate files ===================
		String outputFileName,  outputSheetName, outputLastValFileName; 
		
		
		outputLastValFileName = "LastValues_AllAtt.xls";
		
		outputFileName = "WeeklyRate_BFR.xls";
		outputSheetName = "BFR"; 
		WeekRateCalc objWeekRateCalc_BFR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, bugIssueData, weekRateData_BFR, true, true, outputLastValFileName, true); 
		objWeekRateCalc_BFR.weeklyRateCalc();
		
		outputFileName = "WeeklyRate_DFR.xls";
		outputSheetName = "DFR"; 
		WeekRateCalc objWeekRateCalc_DFR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, bugIssueData, weekRateData_DFR, true, false, outputLastValFileName, false); 
		objWeekRateCalc_DFR.weeklyRateCalc();
		
		outputFileName = "WeeklyRate_FCR.xls";
		outputSheetName = "FCR"; 
		WeekRateCalc objWeekRateCalc_FCR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, ftrIssueData, weekRateData_FCR, true, true, outputLastValFileName, false); 
		objWeekRateCalc_FCR.weeklyRateCalc();
		
		outputFileName = "WeeklyRate_FOR.xls";
		outputSheetName = "FOR"; 
		WeekRateCalc objWeekRateCalc_FOR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, ftrIssueData, weekRateData_FOR, true, false, outputLastValFileName, false); 
		objWeekRateCalc_FOR.weeklyRateCalc();
		
		
//		for commit start this
		
//		outputFileName = "WeeklyRate_Commit.xls";
//		outputSheetName = "Commit"; 
//		WeekRateCalc objWeekRateCalc_Commit = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, commitIssueData , weekRateData_Commit, true, true, outputLastValFileName, false); 
//		objWeekRateCalc_Commit.weeklyRateCalc();
		
		
		
//		------------for sake of debugging---------
//		for (WeekCalcTemplate counter_WeekCalcTemplate: weekRateData){
//			System.out.println(counter_WeekCalcTemplate.getWeekStart());
//			System.out.println(counter_WeekCalcTemplate.getWeekEnd());
//		}
				

	}
	
	
	public static void calc_reportedbugs (){
		for (WeekCalcTemplate iterator:weekRateData_BFR){
			if(iterator.releaseDuration == iterator.releaseCompletion){
				System.out.println(iterator.getReleaseNum() + "," + iterator.releaseDuration+","+(iterator.inO+iterator.inOinC+iterator.inOltC));
			}
		}
	}
	
	public static void calc_reportedbugs7day (){
		for (WeekCalcTemplate iterator:weekRateData_BFR){
			
			if(iterator.releaseDuration>=7){
				if(iterator.releaseCompletion==7){
					System.out.println(iterator.getReleaseNum() + "," + iterator.releaseDuration+","+(iterator.inO+iterator.inOinC+iterator.inOltC));
				}
			}
			else if (iterator.releaseDuration==iterator.releaseCompletion)  {
				System.out.println(iterator.getReleaseNum() + "," + iterator.releaseDuration+","+(iterator.inO+iterator.inOinC+iterator.inOltC));
			}
			
			
		}
	}
	
	public static void calc_bugftrratio (){
		
		int totalBug=0, totalFtr=0; 
		
		for (WeekCalcTemplate iterator:weekRateData_BFR){
			if(iterator.releaseDuration == iterator.releaseCompletion){
				System.out.println(iterator.getReleaseNum() + "," + iterator.releaseDuration+","+(iterator.inO+iterator.inOinC+iterator.inOltC));
				
				
			}
		}
		
		for (WeekCalcTemplate iterator:weekRateData_FCR){
			if(iterator.releaseDuration == iterator.releaseCompletion){
				System.out.println(iterator.getReleaseNum() + "," + iterator.releaseDuration+","+(iterator.inO+iterator.inOinC+iterator.inOltC));
			}
		}
		
		System.out.println(totalBug/(totalBug+totalFtr)+","+ totalFtr/(totalBug+totalFtr));
		
	}
	
	
	
	
	
	
public static void collectedReleasInfo(){
		
		ReleaseInfoCollection obj_ReleaseInfoCollection = new ReleaseInfoCollection (releaseInfo); 
	
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-03-22", true, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-04-08", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-05-14", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-05-21", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-06-01", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-06-17", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-07-28", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-08-20", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-09-04", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-09-17", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-10-15", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-11-05", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-11-26", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-12-10", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2015-12-17", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-01-21", false, "R");


}
	



















	
	
	/*
	
	
	
	
	
public static void pythonGraphCreation (){
			
	
			try {
				
				String pythonCodePath = System.getProperty("user.dir")+ "/src/main/java/resources/pythonCodes/"; 
				String command = "";
				command = "py " + pythonCodePath + "barchart.py";
//				System.out.println(command);
				Runtime.getRuntime().exec(command);
				
				
				
//				String pythonCodePath = System.getProperty("user.dir")+ "/src/main/java/resources/pythonCodes/"; 
////				String command = "";
////				command = "python " + pythonCodePath + "barchart.py";
////				
////				System.out.println(command);
//				Runtime.getRuntime().exec("cd "+pythonCodePath);
//				Runtime.getRuntime().exec("python barchart.py");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("problem");
			}
			
//			System.out.println("done");
}

*/
	
	
}//end of classs




