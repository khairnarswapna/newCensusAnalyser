package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA__STATE_CSV_PATH="./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_CODE_CSV_FILE_PATH="./src/main/resources/IndiaStateCode.csv";
    private static final String  EMPTY_FILE_PATH="";
    private static final String DELIMETERMISSING_CENCUS_FILE="./src/test/resources/delimeterMissing.csv";
    private static final String HEADER_MISSING_CENCUS_FILE="./src/test/resources/HeaderMissing.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianStatCsv_shouldReturnexactCount() throws CensusAnalyserException {
        CensusAnalyser censusAnalyser=new CensusAnalyser();
        int numofStateCode=censusAnalyser.loadStateCode(INDIA__STATE_CSV_PATH);
        Assert.assertEquals(37,numofStateCode);
    }
    @Test
    public void givenIndianStateCodeCSV_WithWrongPath_ShouldThrowException()
    {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadStateCode(WRONG_CODE_CSV_FILE_PATH);
        }
        catch (CensusAnalyserException e) {
            e.printStackTrace();
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
    @Test
    public void givenIndiaCensusData_WithEmptyFilePath_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData("");
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_FILE_ISSUE, e.type);
        }
    }
    @Test
    public void givenIndiaStateCodeData_WithIncorrectDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData((DELIMETERMISSING_CENCUS_FILE));
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_FILE_ISSUE, e.type);
        }
    }
    @Test
    public void givenIndiaCensusData_WithIncorrectHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(HEADER_MISSING_CENCUS_FILE);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_FILE_ISSUE, e.type);
        }
    }

    @Test
    public void  givenIndianCensusData_whensortedOnState_shouldreturnSortedResult() {

        try {
            CensusAnalyser censusAnalyser=new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedData(INDIA_CENSUS_CSV_FILE_PATH );
            IndiaCensusCSV[] censusCSV=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }

    }
}
