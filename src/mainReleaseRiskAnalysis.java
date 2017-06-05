

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
import graphGeneration.graphBugFtr;
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
	
	
	public static void main(String[] args) {
		
		
		ReadingExcelsheet (IssueData, intStartingRowofData, strFilePath, strFileName, strSheetName); 
		
	
//----------------------Optional: If we want to select only bugs and perform analysis within this subset. 
//-------------------- Writing all unique tags in a UniqueTags.xlsx file for user to read and differentiate between bug and feature related tags. 
//-------------------- Default is Bug and others in terms of tag division.
		
//		IdentifyingUniqueTagsFromAllIssues ();
//		DistributingIssuesInMultipleTags ();		
//		ReadingExcelsheet (bugIssueData, intStartingRowofData, strFilePath, strFileName, "Issue_Bug"); 
//		ReadingExcelsheet (ftrIssueData, intStartingRowofData, strFilePath, strFileName, "Issue_Ftr"); 
		
//----------------------Optional: If we want to select only bugs and perform analysis within this subset. 
		
		directFtrBugDifferentiate (); 
		getWeeklyRateData (); 
				
	}
	
	
	public static void ReadingExcelsheet (ArrayList<DataIssueTemplate> tmp_IssueData, 
			int tmp_intStartingRowofData, 
			String tmp_strFilePath, 
			String tmp_strFileName, 
			String tmp_strSheetName){
		
		DataReadExcelFiles objDataReadExcelFiles = new DataReadExcelFiles(tmp_IssueData, tmp_intStartingRowofData, tmp_strFilePath, tmp_strFileName, tmp_strSheetName); 
		excelFileIndex = objDataReadExcelFiles.createColumnIndex(0); 
		objDataReadExcelFiles.readExcelFiles(false);
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
	
	public static void directFtrBugDifferentiate (){
		for (DataIssueTemplate iterator:IssueData){
			if(iterator.getStrIssueType().contains("Bug")|| iterator.getStrIssueType().contains("bug")|| iterator.getStrIssueType().contains("BUG")){
				bugIssueData.add(iterator);
			}
			else{
				ftrIssueData.add(iterator); 
			}
		}
	}
	
	public static void getWeeklyRateData(){
		
		collectedReleasInfo();
		WeekDateCalc objWeekDateCalc_BFR = new WeekDateCalc (weekRateData_BFR, releaseInfo); 
		objWeekDateCalc_BFR.weekDateCalc();
		
		WeekDateCalc objWeekDateCalc_DFR = new WeekDateCalc (weekRateData_DFR, releaseInfo); 
		objWeekDateCalc_DFR.weekDateCalc();
		
		WeekDateCalc objWeekDateCalc_FCR = new WeekDateCalc (weekRateData_FCR, releaseInfo); 
		objWeekDateCalc_FCR.weekDateCalc();
		
		WeekDateCalc objWeekDateCalc_FOR = new WeekDateCalc (weekRateData_FOR, releaseInfo); 
		objWeekDateCalc_FOR.weekDateCalc();
		
		String outputFileName,  outputSheetName; 
		
		
		outputFileName = "WeeklyRate_BFR.xls";
		outputSheetName = "BFR"; 
		WeekRateCalc objWeekRateCalc_BFR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, bugIssueData, weekRateData_BFR, true, true, true); 
		objWeekRateCalc_BFR.weeklyRateCalc();
		
		outputFileName = "WeeklyRate_DFR.xls";
		outputSheetName = "DFR"; 
		WeekRateCalc objWeekRateCalc_DFR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, bugIssueData, weekRateData_DFR, true, false, false); 
		objWeekRateCalc_DFR.weeklyRateCalc();
		
		outputFileName = "WeeklyRate_FCR.xls";
		outputSheetName = "FCR"; 
		WeekRateCalc objWeekRateCalc_FCR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, ftrIssueData, weekRateData_FCR, true, true, false); 
		objWeekRateCalc_FCR.weeklyRateCalc();
		
		outputFileName = "WeeklyRate_FOR.xls";
		outputSheetName = "FOR"; 
		WeekRateCalc objWeekRateCalc_FOR = new WeekRateCalc (strFilePath, strFileName, outputFileName, outputSheetName, ftrIssueData, weekRateData_FOR, true, false, false); 
		objWeekRateCalc_FOR.weeklyRateCalc();
		
	}
	
	graphBugFtr obj_graphBugFtr = new graphBugFtr(weekRateData_BFR, weekRateData_FCR, weekRateData_Commit, releaseInfo, strFilePath); 
	
	
	
		
}//end of classs




