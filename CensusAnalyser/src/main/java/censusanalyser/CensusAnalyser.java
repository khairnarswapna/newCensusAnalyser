package censusanalyser;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    Map <String, CensusDAO> censusStateMap=null;
    public CensusAnalyser() {
    }
    public int loadIndiaCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusStateMap=new CensusLoader().loadCensusData(IndiaCensusCSV.class,csvFilePath);
        return censusStateMap.size();

    }
    public int loadUSCensusData(String... csvFilePath) throws CensusAnalyserException {

        censusStateMap=new CensusLoader().loadCensusData(USCensusCSV.class,csvFilePath);
        return censusStateMap.size();

    }
    /*public int loadStateCode(String stateCsvPath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvPath));) {
            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVbuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator= csvBuilder.getCSVFileItrator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable=()->stateCodeCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(censusCsv-> censusStateMap.get(censusCsv.state)!=null)
                    .forEach(censusCSV->censusStateMap.get(censusCSV.state).stateCode=censusCSV.state);
            return this.censusStateMap.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

    }*/
    public String getStateWithSortByField(SortByField.Field field) throws CensusAnalyserException {
        if(censusStateMap==null || censusStateMap.size()==0)
        {
            throw new CensusAnalyserException("No Census data",CensusAnalyserException.ExceptionType.No_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator=Comparator.comparing(census->census.state);
        List<CensusDAO> censusDAOS=censusStateMap.values().stream().collect(Collectors.toList());
        this.sort(censusDAOS,censusCSVComparator);
        String sortedStateCensusJson=new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;

    }
    private void sort(List<CensusDAO> censusDAOS, Comparator<CensusDAO> censusCSVComparator) {
        for(int i=0;i<censusDAOS.size()-1;i++) {
            for(int j=0;j<censusDAOS.size()-i-1;j++){
               CensusDAO census1=censusDAOS.get(j);
               CensusDAO census2=censusDAOS.get(j+1);
               if(censusCSVComparator.compare(census1,census2)>0){
                   censusDAOS.set(j,census2);
                   censusDAOS.set(j+1,census1);
               }
            }
        }
    }


}
