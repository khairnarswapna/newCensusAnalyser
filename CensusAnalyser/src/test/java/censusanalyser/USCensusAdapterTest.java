package censusanalyser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class USCensusAdapterTest {
    private static final String USCensus_CSVFILE="./src/test/resources/USCensusData.csv";
    @Test
    public void givenIndianCensusCSVFile_ReturnsCorrectRecords() {
        try {
            CensusAdapter usCensusAdapter=new USCensusAdapter();
            Map<String, CensusDAO> numOfRecords = usCensusAdapter.loadCensusData(USCensus_CSVFILE);
            Assert.assertEquals(51,numOfRecords.size());
        } catch (CensusAnalyserException e) {
            System.out.println("Error");
        }
    }
}
