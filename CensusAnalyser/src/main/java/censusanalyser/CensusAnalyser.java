package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<IndiaCensusDAO> censusCSVList;
    public CensusAnalyser() {
        this.censusCSVList = new ArrayList<IndiaCensusDAO>();
    }
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {

            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVbuilder();

            Iterator<IndiaCensusCSV> csvFileItrator= csvBuilder.getCSVFileItrator(reader,IndiaCensusCSV.class);
            while(csvFileItrator.hasNext()) {
               censusCSVList.add(new IndiaCensusDAO(csvFileItrator.next()));
            }
            return this.censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }catch (RuntimeException e) {
            throw new CensusAnalyserException("SOME_FILE_ISSUE",CensusAnalyserException.ExceptionType.SOME_FILE_ISSUE);
        }

    }
    public int loadStateCode(String stateCsvPath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvPath));) {

            ICSVBuilder csvBuilder=CSVBuilderFactory.createCSVbuilder();
            List<IndiaStateCodeCSV> StateCSVList =csvBuilder.getCSVFileList(reader,IndiaStateCodeCSV.class);
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
    public String getStateWiseSortedData(String field) throws CensusAnalyserException {
        if(censusCSVList==null || censusCSVList.size()==0)
        {
            throw new CensusAnalyserException("No Census data",CensusAnalyserException.ExceptionType.No_CENSUS_DATA);
        }
            Comparator<IndiaCensusDAO> censusCSVComparator=Comparator.comparing(census->census.state);
            this.sort(this.getComparator(field));
            String sortedStateCensusJson=new Gson().toJson(censusCSVList);
            return sortedStateCensusJson;

    }
    public Comparator<IndiaCensusDAO> getComparator(String field) {
        Comparator<IndiaCensusDAO> censusComparator;
        switch (field.toLowerCase()) {
            case "state":
                censusComparator = Comparator.comparing(census -> census.state);
                break;
            case "population":
                censusComparator = Comparator.comparing(census -> census.population);
                break;
            case "area":
                censusComparator = Comparator.comparing(census -> census.areaInSqKm);
                break;
            case "density":
                censusComparator = Comparator.comparing(census -> census.densityPerSqKm);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + field.toLowerCase());
        }
        return censusComparator;
    }
    private void sort(Comparator<IndiaCensusDAO> censusCSVComparator) {
        for(int i=0;i<censusCSVList.size()-1;i++) {
            for(int j=0;j<censusCSVList.size()-i-1;j++){
               IndiaCensusDAO census1=censusCSVList.get(j);
               IndiaCensusDAO census2=censusCSVList.get(j+1);
               if(censusCSVComparator.compare(census1,census2)>0){
                   censusCSVList.set(j,census2);
                   censusCSVList.set(j+1,census1);
               }
            }
        }
    }

}
