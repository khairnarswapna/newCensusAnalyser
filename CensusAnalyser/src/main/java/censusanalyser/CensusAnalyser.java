package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
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
}
