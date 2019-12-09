package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        CsvToBean<IndiaCensusCSV> csvToBean;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {

            Iterator<IndiaCensusCSV> censusCSVIterator = new OpenCSVBuilder().
                    getCSVFileItrator(reader,IndiaCensusCSV.class);
             return this.getCount(censusCSVIterator);

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }


    }

    public int loadStateCode(String stateCsvPath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvPath));) {

            Iterator<IndiaStateCodeCSV> StateCSVIterator = new OpenCSVBuilder().
                    getCSVFileItrator(reader,IndiaStateCodeCSV.class);
            return this.getCount(StateCSVIterator);

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
    private<E>Iterator<E> getCSVFileIterator(Reader reader, Class CSVClass) throws CensusAnalyserException {
        try{
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(CSVClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<E> csvToBean = csvToBeanBuilder.build();
            return csvToBean.iterator();
        }catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
    private<E>int getCount(Iterator<E> Iterator){
        Iterable<E> csvItrable = () -> Iterator;
        int numOfEntries = (int) StreamSupport.stream(csvItrable.spliterator(), false).count();
        return numOfEntries;
    }
}
