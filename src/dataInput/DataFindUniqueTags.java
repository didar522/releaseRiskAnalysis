package dataInput;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dataInput.*;
import dataTemplates.*;

public class DataFindUniqueTags {
	
	
	ArrayList<String> strlistUniqueTags; 
	ArrayList<DataIssueTemplate> IssueData; 
	String fileName, filePath, sheetName; 
	
	public DataFindUniqueTags (String tmp_strFilePath, String tmp_strFileName, String tmp_sheetName, ArrayList<DataIssueTemplate> tmp_IssueData, ArrayList<String> tmp_strlistUniqueTags){
		this.fileName=tmp_strFileName;
		this.filePath=tmp_strFilePath;
		this.sheetName=tmp_sheetName; 
		this.strlistUniqueTags=tmp_strlistUniqueTags; 
		this.IssueData=tmp_IssueData; 
	}
	
	
	
	
	public ArrayList<String>  identifyUniqueLabels (){
		for (int i=0;i<IssueData.size();i++){
			boolean flagUniqueLabel = true; 
			String tempLabel = IssueData.get(i).getStrIssueType(); 
			for (int j=0;j<strlistUniqueTags.size();j++){
				
				System.out.println("--"+i+"--"+ tempLabel + "--" + strlistUniqueTags.get(j));
				if (strlistUniqueTags.get(j).equalsIgnoreCase(tempLabel)){
					
					flagUniqueLabel = false; 
				}
			}
			
			if (flagUniqueLabel==true){
				strlistUniqueTags.add(tempLabel); 	
			}
		}
		
		return strlistUniqueTags; 
		
		
	}
	
	public void writeTagsInExcel (){
		//-------------------- Writing all unique tags in a UniqueTags.xlsx file for user to read and differentiate between bug and feature related tags. 
		//		Default is Bug and others in terms of tag division.
				
				try {
					FileOutputStream fileOut = new FileOutputStream(filePath+"UniqueTags.xlsx");
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet worksheetBug = workbook.createSheet("Bug");
					XSSFSheet worksheetFeature = workbook.createSheet("Feature");			
					int bugShtCounter=0, ftrShtCounter=0; 
					
					for (int i=0;i<strlistUniqueTags.size();i++){
						XSSFRow row; 
						
						if (strlistUniqueTags.get(i).contains("Bug") || strlistUniqueTags.get(i).contains("bug")|| strlistUniqueTags.get(i).contains("BUG")){
							row = worksheetBug.createRow(bugShtCounter);
							bugShtCounter++;
						}
						else {
							row = worksheetFeature.createRow(ftrShtCounter);
							ftrShtCounter++;
						}
						
						XSSFCell cell = row.createCell(0);
						cell.setCellValue(strlistUniqueTags.get(i));
					}
					
					workbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
//					System.out.println("Success: Unique labels written");
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
//					System.out.println("Error: Unique labels writing");
				}catch (IOException e2){
					e2.printStackTrace();
//					System.out.println("Error: Unique labels writing");
				}
				
				//-------------------- Writing all unique tags in a UniqueTags.xlsx file for user to read and differentiate between bug and feature related tags. 
			
	}
	
	
	
	

}  // End of Class