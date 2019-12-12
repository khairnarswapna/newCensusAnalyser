package censusanalyser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class loaderTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAdapter indiaCensusAdapter=new IndiaCensusAdapter();
            Map<String, CensusDAO> numOfRecords = indiaCensusAdapter.loadCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords.size());
        } catch (CensusAnalyserException e) { }
    }
}
