package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

    public enum Country{INDIA,US}

    Map <String, CensusDAO> censusStateMap=null;
    public CensusAnalyser() {
    }
    public int loadCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {

        CensusAdapter censusAdapter = CensusAdapterFactory.getCreateCensusAdapter(country);
        censusStateMap= censusAdapter.loadCensusData(csvFilePath);
        return censusStateMap.size();

    }
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
