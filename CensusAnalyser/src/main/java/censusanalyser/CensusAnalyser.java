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

    Map <String,IndiaCensusDAO> censusStateMap=null;
    public CensusAnalyser() {
        this.censusStateMap = new HashMap();
    }
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {

            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVbuilder();
            Iterator<IndiaCensusCSV> csvFileItrator= csvBuilder.getCSVFileItrator(reader,IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable=()->csvFileItrator;
            StreamSupport.stream(csvIterable.spliterator(),false).
                    forEach(censusCSV->censusStateMap.put(censusCSV.state,new IndiaCensusDAO(censusCSV)));
            return this.censusStateMap.size();

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
    public int loadStateCode(String stateCsvPath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvPath));) {
            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVbuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator= csvBuilder.getCSVFileItrator(reader,IndiaStateCodeCSV.class);
             while(stateCodeCSVIterator.hasNext()){
                 IndiaStateCodeCSV stateCSV=stateCodeCSVIterator.next();
                 IndiaCensusDAO censusDAO=censusStateMap.get(stateCSV.state);
                 if(censusDAO==null)
                     continue;
                 censusDAO.stateCode=stateCSV.StateCode;
             }
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

    }

    private<E>int getCount(Iterator<E> Iterator){
        Iterable<E> csvItrable = () -> Iterator;
        int numOfEntries = (int) StreamSupport.stream(csvItrable.spliterator(), false).count();
        return numOfEntries;
    }
    public String getStateWiseSortedData() throws CensusAnalyserException {
        if(censusStateMap==null || censusStateMap.size()==0)
        {
            throw new CensusAnalyserException("No Census data",CensusAnalyserException.ExceptionType.No_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusCSVComparator=Comparator.comparing(census->census.state);
        List<IndiaCensusDAO> censusDAOS=censusStateMap.values().stream().collect(Collectors.toList());
        this.sort(censusDAOS,censusCSVComparator);
        String sortedStateCensusJson=new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;

    }
    private void sort(List<IndiaCensusDAO> censusDAOS,Comparator<IndiaCensusDAO> censusCSVComparator) {
        for(int i=0;i<censusDAOS.size()-1;i++) {
            for(int j=0;j<censusDAOS.size()-i-1;j++){
               IndiaCensusDAO census1=censusDAOS.get(j);
               IndiaCensusDAO census2=censusDAOS.get(j+1);
               if(censusCSVComparator.compare(census1,census2)>0){
                   censusDAOS.set(j,census2);
                   censusDAOS.set(j+1,census1);
               }
            }
        }
    }

}
