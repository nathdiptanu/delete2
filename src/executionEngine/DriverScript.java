package executionEngine;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import config.ActionKeywords;
import config.Constants;
import utility.ExcelUtils;
import utility.Log;
 
public class DriverScript {
	
	public static Properties OR;
	public static ActionKeywords actionKeywords;
	public static String sAction;
	public static String URL;
	public static String itemMatch;
	public static String expectedValue;
	public static String entireResponse;
	public static Method method[];
		
	public static int iTestStep;
	public static int iTestLastStep;
	public static String sTestCaseID;
	public static String sRunMode;
	public static String sData;
	public static boolean bResult;
	
	
	public DriverScript() throws NoSuchMethodException, SecurityException{
		actionKeywords = new ActionKeywords();
		method = actionKeywords.getClass().getMethods();	
	}
	
    public static void main(String[] args) throws Exception {
    	ExcelUtils.setExcelFile(Constants.Path_TestData);
    	DOMConfigurator.configure("log4j.xml");
    	String Path_OR = Constants.Path_OR;
		FileInputStream fs = new FileInputStream(Path_OR);
		OR= new Properties(System.getProperties());
		OR.load(fs);
		
		DriverScript startEngine = new DriverScript();
		startEngine.execute_TestCase();
		
    }
		
    private void execute_TestCase() throws Exception {
	    	int iTotalTestCases = ExcelUtils.getRowCount(Constants.Sheet_TestCases);
			for(int iTestcase=1;iTestcase<iTotalTestCases;iTestcase++){
				bResult = true;
				sTestCaseID = ExcelUtils.getCellData(iTestcase, Constants.Col_TestCaseID, Constants.Sheet_TestCases); 
				sRunMode = ExcelUtils.getCellData(iTestcase, Constants.Col_RunMode,Constants.Sheet_TestCases);
				if (sRunMode.equals("Yes")){
					Log.startTestCase(sTestCaseID);
					iTestStep = ExcelUtils.getRowContains(sTestCaseID, Constants.Col_TestCaseID, Constants.Sheet_TestSteps);
					iTestLastStep = ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, sTestCaseID, iTestStep);
					bResult=true;
					for (;iTestStep<iTestLastStep;iTestStep++){
			    		sAction= ExcelUtils.getCellData(iTestStep, Constants.Col_Action,Constants.Sheet_TestSteps);
			    		URL = ExcelUtils.getCellData(iTestStep, Constants.Col_URL, Constants.Sheet_TestSteps);
			    		itemMatch = ExcelUtils.getCellData(iTestStep, Constants.Col_ItemMatch, Constants.Sheet_TestSteps);
			    		expectedValue=ExcelUtils.getCellData(iTestStep, Constants.Col_ExpectedValue, Constants.Sheet_TestSteps);
			    		System.out.println(sAction);
			    		System.out.println(URL);
			    		System.out.println(itemMatch);
			    		//entireResponse=ExcelUtils.getCellData(iTestStep, Constants.Col_Response, Constants.Sheet_TestSteps);
			    		execute_Actions();
						if(bResult==false){
							ExcelUtils.setCellData(Constants.KEYWORD_FAIL,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);
							Log.endTestCase(sTestCaseID);
							break;
							}						
						}
					if(bResult==true){
					ExcelUtils.setCellData(Constants.KEYWORD_PASS,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);
					Log.endTestCase(sTestCaseID);	
						}					
					}
				}
    		}	
     
     private static void execute_Actions() throws Exception {
	
		for(int i=0;i<method.length;i++){
			
			if(method[i].getName().equals(sAction)){
				method[i].invoke(actionKeywords,URL,itemMatch,expectedValue,iTestStep);
				if(bResult==true){
					ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestStep, Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
					
					break;
				}else{
					ExcelUtils.setCellData(Constants.KEYWORD_FAIL, iTestStep, Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
					ActionKeywords.closeBrowser("","");
					break;
					}
				}
			}
     }
     
}