package weekRateCalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import dataTemplates.*;


public class WeekRateCalc {
	
	String filePath; 
	String fileName; 
	String outputFileName; 
	String outputSheetName;
	ArrayList<WeekCalcTemplate> weekRateData; 
	ArrayList<DataIssueTemplate> tempIssueData = new ArrayList<DataIssueTemplate>();
	boolean fileCreate; 
	boolean boolCalculationTypeisRate, booloutputLastValFileCreate; 
	ArrayList <DataReleaseCategoryTemplate> objDataReleaseCategoryTemplate = new ArrayList<DataReleaseCategoryTemplate> (); 
	
	
	
	public WeekRateCalc (
			String filePath, 
			String tmpFileName, 
			String tmpOutputFileName, 
			String tmpOutputSheetName, 
			ArrayList<DataIssueTemplate> IssueData, 
			ArrayList<WeekCalcTemplate> tmpweekRateData, 
			boolean tmpFileCreate, 
			boolean tmp_boolCalculationTypeisRate, 
			boolean temp_booloutputLastValFileCreate
	){
		
		this.filePath = filePath; 
		this.fileName= tmpFileName;
		this.outputFileName = tmpOutputFileName; 
		this.tempIssueData = IssueData; 
		this.weekRateData = tmpweekRateData;
		this.fileCreate = tmpFileCreate;
		this.boolCalculationTypeisRate = tmp_boolCalculationTypeisRate;
		this.outputSheetName = tmpOutputSheetName; 
		this.booloutputLastValFileCreate = temp_booloutputLastValFileCreate; 
	}
	
	

//	public void weeklyRateCalc (String fileName, ArrayList<WeekCalcStr> weekRateData, ArrayList<IssueListStr> tempIssueData, boolean fileCreate){
	public void weeklyRateCalc (){
		String startDateCompare=null; 
		String closeDateCompare=null; 
		String startWKDateCompare=null; 
		
		Date issueCreated, issueClosed, weekStart, weekEnd, lastWeekEnd=null; 
		
		for (int i=0;i<weekRateData.size();i++){
			for (int j=1;j<tempIssueData.size();j++){
				
				issueCreated = tempIssueData.get(j).getDateCreated(); 
				issueClosed = tempIssueData.get(j).getDateResolved(); 
				weekStart = weekRateData.get(i).getWeekStart(); 
				weekEnd = weekRateData.get(i).getWeekEnd(); 
				if (weekRateData.get(i).getWeekNum()>1){
					lastWeekEnd= weekRateData.get(i-1).getWeekEnd();
				}
				else if (weekRateData.get(i).getWeekNum()==1) {
					lastWeekEnd = weekStart;
				}
				
				
				if (issueCreated.before(weekStart)) startDateCompare="before";  // -1 means issue created before week
				else if (issueCreated.after (weekEnd)) startDateCompare="after";
				else if (issueCreated.after(weekStart) && 	issueCreated.before (weekEnd))  startDateCompare="inWeek"; 
				else if (issueCreated.equals(weekStart) || issueCreated.equals(weekEnd)) startDateCompare="inWeek"; 		
						
				
				if (issueClosed == null) closeDateCompare="open"; 
				else if (issueClosed.before(weekStart)) closeDateCompare="before";  // -1 means issue created before week
				else if (issueClosed.after (weekEnd)) closeDateCompare="after";
				else if (issueClosed.after(weekStart) && 	issueClosed.before (weekEnd))  closeDateCompare="inWeek"; 
				else if (issueClosed.equals(weekStart) || issueClosed.equals(weekEnd)) closeDateCompare="inWeek"; 	
				
				//// this is something to fix ----------------------------XXXXXXX
				if(boolCalculationTypeisRate==false){
					if (issueCreated.before(lastWeekEnd)) startWKDateCompare="sngbefore";  // -1 means issue created before week
					else if (issueCreated.after (weekEnd)) startWKDateCompare="sngafter";
					else if (issueCreated.after(lastWeekEnd) && 	issueCreated.before (weekEnd))  startWKDateCompare="snginWeek"; 
					else if (issueCreated.equals(lastWeekEnd) || issueCreated.equals(weekEnd)) startWKDateCompare="snginWeek"; 
					if (startWKDateCompare.equals("snginWeek")) weekRateData.get(i).weeklyVal++;
				}	
				
				if (startDateCompare.equals("inWeek") && closeDateCompare.equals("inWeek"))  {
					weekRateData.get(i).inOinC++;
					
					weekRateData.get(i).dblTotalFiles =  weekRateData.get(i).dblTotalFiles + tempIssueData.get(j).getDblNumberofFiles();
					weekRateData.get(i).dblTotalAddition =  weekRateData.get(i).dblTotalAddition + tempIssueData.get(j).getDblAdditionChurn(); 
					weekRateData.get(i).dblTotalDeletion = weekRateData.get(i).dblTotalDeletion + tempIssueData.get(j).getDblDeletionChurn(); 
				}
				if (startDateCompare.equals("before") && closeDateCompare.equals("inWeek")) weekRateData.get(i).erOinC++;
				if (startDateCompare.equals("inWeek") && closeDateCompare.equals("after")) weekRateData.get(i).inOltC++;
				if (startDateCompare.equals("before") && closeDateCompare.equals("after")) weekRateData.get(i).erOltC++;
				if (startDateCompare.equals("inWeek") && closeDateCompare.equals("open")) weekRateData.get(i).inO++; 
				if (startDateCompare.equals("before") && closeDateCompare.equals("open")) weekRateData.get(i).erO++;
				
				
			}
		
//			double tmpTotalInWeekValues=0, tmpTotalAllInOutWeekValues=0, tmpTotalTransferFromEarlyRelease=0,tmpTotalOpenThisRelease=0,tmpTotalerOleftOpen=0,tmpTotalinOleftOpen=0; 
			
			// this is to be fixed -----------------------XXXXXXX 
			if (boolCalculationTypeisRate==false) {
				weekRateData.get(i).tmpTotalInWeekValues = weekRateData.get(i).weeklyVal; 
			}
			else {
				weekRateData.get(i).tmpTotalInWeekValues = (weekRateData.get(i).inOinC+weekRateData.get(i).erOinC); // total close this rel
				weekRateData.get(i).tmpTotalTransferFromEarlyRelease = weekRateData.get(i).erO + weekRateData.get(i).erOinC+weekRateData.get(i).erOltC; 
				weekRateData.get(i).tmpTotalOpenThisRelease = weekRateData.get(i).inOinC+ weekRateData.get(i).inOltC + weekRateData.get(i).inO; 
			
			// early open in close ---- weekRateData.get(i).erOinC
			// in open in close ---- weekRateData.get(i).inOinC
				weekRateData.get(i).tmpTotalerOleftOpen =  weekRateData.get(i).erOltC + weekRateData.get(i).erO; 
				weekRateData.get(i).tmpTotalinOleftOpen = weekRateData.get(i).inOltC + weekRateData.get(i).inO; 
			
			
			
			}
			weekRateData.get(i).tmpTotalAllInOutWeekValues = (weekRateData.get(i).inOinC+weekRateData.get(i).erOinC+ weekRateData.get(i).inOltC+weekRateData.get(i).erOltC+weekRateData.get(i).inO+weekRateData.get(i).erO);
			
			weekRateData.get(i).settotalRateVal(weekRateData.get(i).tmpTotalInWeekValues/weekRateData.get(i).tmpTotalAllInOutWeekValues); /// This is the rate value 
			
			if (weekRateData.get(i).gettotalRateVal()>=0){
				
			}else{
				weekRateData.get(i).settotalRateVal(0); 
			}
		}
		
	}
		
		public void exportWeeklyDataExcel (){
		
		
		//================================ Info: Output for weekly rates per attribute ===================================
		
		try {
			
			
			FileOutputStream fileOut=null; 
			FileInputStream inputWeeklyRateData=null; 
			HSSFWorkbook workbook=null; 
			
			if (fileCreate==true){
				fileOut = new FileOutputStream(filePath+outputFileName);
				workbook = new HSSFWorkbook();
			}
			else {
				inputWeeklyRateData = new FileInputStream(new File(filePath+outputFileName));
				workbook = new HSSFWorkbook(inputWeeklyRateData);
				
			}
			
			System.out.println("------------------"+outputFileName);
			HSSFSheet worksheetRate = workbook.createSheet(outputFileName);
			HSSFRow row; 
			HSSFCell cell;
			
			row= worksheetRate.createRow(0); 
			cell= row.createCell(0);	cell.setCellValue("Week Num");
			cell= row.createCell(1);	cell.setCellValue("Week Start");
			cell= row.createCell(2);	cell.setCellValue("Week End");
			cell= row.createCell(3);	cell.setCellValue("inOinC");
			cell= row.createCell(4);	cell.setCellValue("erOinC");
			cell= row.createCell(5);	cell.setCellValue("inOltC");
			cell= row.createCell(6);	cell.setCellValue("erOltC");
			cell= row.createCell(7);	cell.setCellValue("inO");
			cell= row.createCell(8);	cell.setCellValue("erO");
			cell= row.createCell(9);	cell.setCellValue("Total Rate Val");
			cell= row.createCell(10);	cell.setCellValue("tmpTotalTransferFromEarlyRelease");
			cell= row.createCell(11);	cell.setCellValue("tmpTotalOpenThisRelease");
			cell= row.createCell(12);	cell.setCellValue("tmpTotalerOleftOpen");
			cell= row.createCell(13);	cell.setCellValue("tmpTotalinOleftOpen");
			cell= row.createCell(14);	cell.setCellValue("early open in close");
			cell= row.createCell(15);	cell.setCellValue("in open in close");
			cell= row.createCell(16);	cell.setCellValue("total close");
//			cell= row.createCell(10);	cell.setCellValue(weekRateData.get(i).normalTotalVal);
			cell= row.createCell(17); cell.setCellValue("Release Number");
			cell= row.createCell(18); cell.setCellValue("Release Start");
			cell= row.createCell(19); cell.setCellValue("Release End");
			cell= row.createCell(20); cell.setCellValue("Release Duration");
			cell= row.createCell(21); cell.setCellValue("Release Completion");
			cell= row.createCell(22); cell.setCellValue("Release Category");
			cell= row.createCell(23); cell.setCellValue("Normal value based on Duration");
			cell= row.createCell(24); cell.setCellValue("Project Name");
			cell= row.createCell(25); cell.setCellValue("Project Name & Release Num");
			
			cell= row.createCell(26); cell.setCellValue("Total Number of Files");
			cell= row.createCell(27); cell.setCellValue("Total Addition Churn");
			cell= row.createCell(28); cell.setCellValue("Total Deletion Churn");
			
			
			objDataReleaseCategoryTemplate.add(new DataReleaseCategoryTemplate ()); 
			objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setIntReleaseNum(1);
			
			for (int i=1;i<weekRateData.size();i++){
				
				if (objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).getIntReleaseNum()==weekRateData.get(i).getReleaseNum()){
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setStrReleaseName(tempIssueData.get(1).getStrProject()+"-"+weekRateData.get(i).getReleaseNum());	
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setDblReleaseAttLastValRecorded(weekRateData.get(i).totalRateVal);
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setDblReleaseAttLastOpenRecorded(weekRateData.get(i).inO + weekRateData.get(i).erO);
				}
				
				else if (objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).getIntReleaseNum()!=weekRateData.get(i).getReleaseNum()){
					objDataReleaseCategoryTemplate.add(new DataReleaseCategoryTemplate ()); 
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setIntReleaseNum(weekRateData.get(i).getReleaseNum());
					
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setStrReleaseName(tempIssueData.get(1).getStrProject()+"-"+weekRateData.get(i).getReleaseNum());		
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setDblReleaseAttLastValRecorded(weekRateData.get(i).totalRateVal);
					objDataReleaseCategoryTemplate.get(objDataReleaseCategoryTemplate.size()-1).setDblReleaseAttLastOpenRecorded(weekRateData.get(i).inO + weekRateData.get(i).erO);
				}
				
				row= worksheetRate.createRow(i); 
				cell= row.createCell(0);	cell.setCellValue(weekRateData.get(i).getWeekNum());
				cell= row.createCell(1);	cell.setCellValue(weekRateData.get(i).getWeekStart());
				cell= row.createCell(2);	cell.setCellValue(weekRateData.get(i).getWeekEnd());
				cell= row.createCell(3);	cell.setCellValue(weekRateData.get(i).inOinC);
				cell= row.createCell(4);	cell.setCellValue(weekRateData.get(i).erOinC);
				cell= row.createCell(5);	cell.setCellValue(weekRateData.get(i).inOltC);
				cell= row.createCell(6);	cell.setCellValue(weekRateData.get(i).erOltC);
				cell= row.createCell(7);	cell.setCellValue(weekRateData.get(i).inO);
				cell= row.createCell(8);	cell.setCellValue(weekRateData.get(i).erO);
				cell= row.createCell(9);	cell.setCellValue(weekRateData.get(i).totalRateVal);
//				cell= row.createCell(10);	cell.setCellValue(weekRateData.get(i).normalTotalVal);
				cell= row.createCell(10);	cell.setCellValue(weekRateData.get(i).tmpTotalTransferFromEarlyRelease);
				cell= row.createCell(11);	cell.setCellValue(weekRateData.get(i).tmpTotalOpenThisRelease);
				cell= row.createCell(12);	cell.setCellValue(weekRateData.get(i).tmpTotalerOleftOpen);
				cell= row.createCell(13);	cell.setCellValue(weekRateData.get(i).tmpTotalinOleftOpen);
				cell= row.createCell(14);	cell.setCellValue(weekRateData.get(i).erOinC);
				cell= row.createCell(15);	cell.setCellValue(weekRateData.get(i).inOinC);
				cell= row.createCell(16);	cell.setCellValue(weekRateData.get(i).tmpTotalInWeekValues);
				cell= row.createCell(17); cell.setCellValue(weekRateData.get(i).getReleaseNum());
				cell= row.createCell(18); cell.setCellValue(weekRateData.get(i).getReleaseStart());
				cell= row.createCell(19); cell.setCellValue(weekRateData.get(i).getReleaseEnd());
				cell= row.createCell(20); cell.setCellValue(weekRateData.get(i).getReleaseDuration());
				cell= row.createCell(21); cell.setCellValue(weekRateData.get(i).getReleaseCompletion());
				cell= row.createCell(22); cell.setCellValue(weekRateData.get(i).getReleaseCategory());
				cell= row.createCell(23); cell.setCellValue(weekRateData.get(i).gettotalRateVal()/weekRateData.get(i).getReleaseDuration());
				cell= row.createCell(24); cell.setCellValue(tempIssueData.get(1).getStrProject());
				cell= row.createCell(25); cell.setCellValue(tempIssueData.get(1).getStrProject()+"-"+weekRateData.get(i).getReleaseNum());
				
				cell= row.createCell(26); cell.setCellValue(weekRateData.get(i).dblTotalFiles);
				cell= row.createCell(27); cell.setCellValue(weekRateData.get(i).dblTotalAddition);
				cell= row.createCell(28); cell.setCellValue(weekRateData.get(i).dblTotalDeletion);
				
				//need to be corrected -----------------------XXXXXXXXXXXXXXX
//				if (boolCalculationTypeisRate==false){
//					cell= row.createCell(10);	cell.setCellValue(weekRateData.get(i).weeklyVal/7);
//				}
				
			}
			
			
			
			if (fileCreate!=true){
				inputWeeklyRateData.close();
				fileOut = new FileOutputStream(filePath+outputFileName);
			}
			
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			System.out.println("Success: written "+ outputFileName);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Error: writing "+ outputFileName);
		}catch (IOException e2){
			e2.printStackTrace();
			System.out.println("Error: writing "+ outputFileName);
		}
		
		
	}// end of weekly rate method. 
	
}
	