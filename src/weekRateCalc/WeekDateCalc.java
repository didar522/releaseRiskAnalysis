package weekRateCalc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dataTemplates.*;



public class WeekDateCalc {

	ArrayList<WeekCalcTemplate> weekRateData; 
	ArrayList<ReleaseCalendarTemplate> releaseInfo; 
	
	public WeekDateCalc (ArrayList<WeekCalcTemplate> tmp_weekRateData, ArrayList<ReleaseCalendarTemplate> tmp_releaseInfo){
		this.weekRateData = tmp_weekRateData;
		this.releaseInfo = tmp_releaseInfo;
	}
	
	
	
	
	public void weekDateCalc (){
		
		Date weekStart= releaseInfo.get(0).getDateReleasEnd(); 
		Date endDate = releaseInfo.get(releaseInfo.size()-1).getDateReleasEnd();
		
		int weekNumber = 0,ctrWeekRateData=-1, releaseNum=1; 
		
		
		
		while (addDate((weekNumber+1), weekStart).compareTo(endDate)<=0){
			weekRateData.add(new WeekCalcTemplate());
			ctrWeekRateData++;
			
			
			weekRateData.get(ctrWeekRateData).setWeekStart(weekStart);			
			//System.out.print(" "+ DateFormat.getDateInstance(DateFormat.SHORT).format(weekRateData.get(ctrWeekRateData).getWeekStart()));
			
			
			weekRateData.get(ctrWeekRateData).setWeekNum(ctrWeekRateData+1);
			//System.out.print("--wn-- "+ weekRateData.get(ctrWeekRateData).getWeekNum());
			
			weekNumber++; 
			//System.out.print("--rW -- " + weekNumber);
			//System.out.println("checking date ------"+ addDate(7*weekNumber, weekStart));
			if (addDate(weekNumber, weekStart).compareTo(releaseInfo.get(releaseNum).getDateReleasEnd())<0){
				weekRateData.get(ctrWeekRateData).setWeekEnd(addDate(weekNumber, weekStart));
				
				
				
				//-----New calculaation section for CBR
				
				weekRateData.get(ctrWeekRateData).setReleaseNum(releaseNum);
				weekRateData.get(ctrWeekRateData).setReleaseEnd(releaseInfo.get(releaseNum).getDateReleasEnd());
				weekRateData.get(ctrWeekRateData).setReleaseStart(releaseInfo.get(releaseNum-1).getDateReleasEnd());
				weekRateData.get(ctrWeekRateData).durationCompletionCalculation();
				weekRateData.get(ctrWeekRateData).setReleaseCategory(releaseInfo.get(releaseNum).getStrCategoryofRelease());
				
				
				
				
				
				
				
				
				//System.out.print("----"+ DateFormat.getDateInstance(DateFormat.SHORT).format(weekRateData.get(ctrWeekRateData).getWeekEnd()));
				//System.out.println();
			}
			else {
				weekRateData.get(ctrWeekRateData).setWeekEnd(releaseInfo.get(releaseNum).getDateReleasEnd()); 
				//System.out.print("-//--"+ DateFormat.getDateInstance(DateFormat.SHORT).format(weekRateData.get(ctrWeekRateData).getWeekEnd()));
				//System.out.println();
				
				//-----New calculaation section for CBR
				weekRateData.get(ctrWeekRateData).setReleaseNum(releaseNum);
				weekRateData.get(ctrWeekRateData).setReleaseEnd(releaseInfo.get(releaseNum).getDateReleasEnd());
				weekRateData.get(ctrWeekRateData).setReleaseStart(releaseInfo.get(releaseNum-1).getDateReleasEnd());
				weekRateData.get(ctrWeekRateData).durationCompletionCalculation();
				weekRateData.get(ctrWeekRateData).setReleaseCategory(releaseInfo.get(releaseNum).getStrCategoryofRelease());
				
				
				weekNumber=0; 
				weekStart=releaseInfo.get(releaseNum).getDateReleasEnd();
				releaseNum++;
				
				//System.out.println("changing for release --------- " + releaseNum);
			}
		}
		
		
//		for (WeekCalcTemplate counterWeekRateData: weekRateData) {
//			System.out.println(counterWeekRateData.getReleaseNum());
//			System.out.println(counterWeekRateData.getWeekNum());
//			System.out.println(counterWeekRateData.getReleaseStart() + "-----"+ counterWeekRateData.getReleaseEnd());
//			System.out.println(counterWeekRateData.getWeekStart() + "-----"+ counterWeekRateData.getWeekEnd());
//			System.out.println(counterWeekRateData.getReleaseDuration());
//			System.out.println(counterWeekRateData.getReleaseCompletion());
//			System.out.println(counterWeekRateData.getReleaseCategory());
//			System.out.println("==============================");
//		}
		
	}
	
	
	public Date addDate (int numofDays, Date startingDatetoAdd){
		Calendar cal = Calendar.getInstance();
        cal.setTime(startingDatetoAdd);
        cal.add(Calendar.DATE, numofDays); //minus number would decrement the days
        return cal.getTime(); 
    }
}// End of class
