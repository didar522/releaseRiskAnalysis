package dataTemplates;

import java.util.Date;

public class ReleaseCalendarTemplate {

	private Date dateReleasEnd; 
	private String strCategoryofRelease; //High, Medium, Low (Satisfaction of a release)
	private double dblTotalDurationofRelease; 
	
	
	
	
	/**
	 * @return the dateWeekStartDate
	 */
	public Date getDateReleasEnd() {
		return dateReleasEnd;
	}
	/**
	 * @param dateWeekStartDate the dateWeekStartDate to set
	 */
	public void setDateReleasEnd(Date tmp_dateReleasEnd) {
		this.dateReleasEnd = tmp_dateReleasEnd;
	}
	
	/**
	 * @return the strCategoryofRelease
	 */
	public String getStrCategoryofRelease() {
		return strCategoryofRelease;
	}
	/**
	 * @param strCategoryofRelease the strCategoryofRelease to set
	 */
	public void setStrCategoryofRelease(String strCategoryofRelease) {
		this.strCategoryofRelease = strCategoryofRelease;
	}
	/**
	 * @return the dblTotalDurationofRelease
	 */
	public double getDblTotalDurationofRelease() {
		return dblTotalDurationofRelease;
	}
	/**
	 * @param dblTotalDurationofRelease the dblTotalDurationofRelease to set
	 */
	public void setDblTotalDurationofRelease(double dblTotalDurationofRelease) {
		this.dblTotalDurationofRelease = dblTotalDurationofRelease;
	}
	
	
	
	
	
	
	
	
	
}



