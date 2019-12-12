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

public class CensusLoader {

    public <E> Map loadCensusData(Class<E> censusCSVClass,String... csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDAO> censusStateMap=new HashMap();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVbuilder();
            Iterator<E> csvFileItrator= csvBuilder.getCSVFileItrator(reader,censusCSVClass);
            Iterable<E> csvIterable=()->csvFileItrator;

            if(censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusStateMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            else if(censusCSVClass.getName().equals("censusanalyser.USCensusCSV")){
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusStateMap.put(censusCSV.state, new CensusDAO(censusCSV)));

            }
            if(csvFilePath.length==1)
                this.loadStateCode(censusStateMap,csvFilePath[1]);
            return censusStateMap;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }catch (RuntimeException e) {
            throw new CensusAnalyserException("SOME_FILE_ISSUE",CensusAnalyserException.ExceptionType.SOME_FILE_ISSUE);
        }
    }
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
}
