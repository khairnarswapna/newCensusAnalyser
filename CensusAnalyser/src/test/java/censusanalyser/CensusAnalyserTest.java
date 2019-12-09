package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIAN__STATE_CSV_PATH="/home/admin142/Downloads/CensusAnalyser/CensusAnalyser/src/test/resources/IndiaStateCode.csv";

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
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianStatCsv_shouldReturnexactCount() throws CensusAnalyserException {
        CensusAnalyser censusAnalyser=new CensusAnalyser();
        int numofStateCode=censusAnalyser.loadStateCode(INDIAN__STATE_CSV_PATH);
        Assert.assertEquals(37,numofStateCode);
    }

    @Test
    public void  givenIndianCensusData_whensortedOnState_shouldreturnSortedResult() throws CensusAnalyserException {
        CensusAnalyser censusAnalyser=new CensusAnalyser();
        String sortedCensusdata=censusAnalyser.getStateWiseSorteddata(INDIA_CENSUS_CSV_FILE_PATH );
        IndiaCensusCSV[] censusCSV=new Gson().fromJson(sortedCensusdata,IndiaCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
    }
}
