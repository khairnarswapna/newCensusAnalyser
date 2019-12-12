package censusanalyser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class IndiaCensusAdapterTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String INDIA__STATE_CSV_PATH="./src/test/resources/IndiaStateCode.csv";
    @Test
    public void givenIndianCensusCSVFile_ReturnsCorrectRecords() {
        try {
            CensusAdapter indiaCensusAdapter=new IndiaCensusAdapter();
            Map<String, CensusDAO> Records = indiaCensusAdapter.loadCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA__STATE_CSV_PATH);
            Assert.assertEquals(29,Records.size());
        } catch (CensusAnalyserException e) { }
    }
    @Test
    public void givenIndianCensusCSVFile_PassedSingleCSVFile_throwsException() {
        try {
            CensusAdapter indiaCensusAdapter=new IndiaCensusAdapter();
            Map<String, CensusDAO> numOfRecords = indiaCensusAdapter.loadCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords.size());
        } catch (CensusAnalyserException e) { }
    }
}
