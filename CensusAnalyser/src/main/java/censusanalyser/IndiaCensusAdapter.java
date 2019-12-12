package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class IndiaCensusAdapter extends CensusAdapter {

    Map <String, CensusDAO> censusStateMap=null;
    private int loadStateCode(Map<String, CensusDAO> censusStateMap, String stateCsvPath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvPath));) {
            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVbuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator= csvBuilder.getCSVFileItrator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable=()->stateCodeCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(censusCsv-> censusStateMap.get(censusCsv.state)!=null)
                    .forEach(censusCSV->censusStateMap.get(censusCSV.state).stateCode=censusCSV.state);
            return censusStateMap.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

    }
    @Override
    public Map<String, CensusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
            Map<String, CensusDAO> censusStateMap = super.loadCensusData(IndiaCensusCSV.class,csvFilePath[0]);
            this.loadStateCode(censusStateMap,csvFilePath[1]);
            return censusStateMap;
    }
}
