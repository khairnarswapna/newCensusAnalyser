package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        CsvToBean<IndiaCensusCSV> csvToBean;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {

            ICSVBuilder csvbuilder=CSVBuilderFactory.createCSVbuilder();
            List<IndiaCensusCSV> censusCSVList =csvbuilder.getCSVFileList(reader,IndiaCensusCSV.class);
             return censusCSVList.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }


    }

    public int loadStateCode(String stateCsvPath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvPath));) {

            ICSVBuilder csvbuilder=CSVBuilderFactory.createCSVbuilder();
            List<IndiaCensusCSV> StateCSVList =csvbuilder.getCSVFileList(reader,IndiaStateCodeCSV.class);
            return StateCSVList.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }
    private<E>int getCount(Iterator<E> Iterator){
        Iterable<E> csvItrable = () -> Iterator;
        int numOfEntries = (int) StreamSupport.stream(csvItrable.spliterator(), false).count();
        return numOfEntries;
    }

    public String getStateWiseSorteddata(String csvFilePath) throws CensusAnalyserException {

        CsvToBean<IndiaCensusCSV> csvToBean;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {

            ICSVBuilder csvbuilder=CSVBuilderFactory.createCSVbuilder();
            List<IndiaCensusCSV> censusCSVList =csvbuilder.getCSVFileList(reader,IndiaCensusCSV.class);
            Comparator<IndiaCensusCSV> censusCSVComparator=Comparator.comparing(census->census.state);
            this.sort(censusCSVList,censusCSVComparator);
            String sortedStateCensusJson=new Gson().toJson(censusCSVList);
            return sortedStateCensusJson;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }


    }

    private void sort(List<IndiaCensusCSV> censusCSVList, Comparator<IndiaCensusCSV> censusCSVComparator) {
        for(int i=0;i<censusCSVList.size()-1;i++) {
            for(int j=0;j<censusCSVList.size()-i-1;j++){
               IndiaCensusCSV census1=censusCSVList.get(j);
               IndiaCensusCSV census2=censusCSVList.get(j+1);
               if(censusCSVComparator.compare(census1,census2)>0){
                   censusCSVList.set(j,census2);
                   censusCSVList.set(j+1,census1);
               }
            }
        }
    }

}
