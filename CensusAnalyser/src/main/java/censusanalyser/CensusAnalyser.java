package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

    public enum Country{INDIA,US}
    private Country country;

    Map <String, CensusDAO> censusStateMap=null;
    public CensusAnalyser() {
    }

    public CensusAnalyser(Country country) {
        this.country = country;
    }
    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        CensusAdapter censusAdapter = CensusAdapterFactory.getCreateCensusAdapter(country);
        censusStateMap= censusAdapter.loadCensusData(csvFilePath);
        return censusStateMap.size();
    }
    public String getStateWithSortByField(SortByField.Field field) throws CensusAnalyserException {
        if(censusStateMap==null || censusStateMap.size()==0)
        {
            throw new CensusAnalyserException("No Census data",CensusAnalyserException.ExceptionType.No_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator=SortByField.getParameter(field);
        ArrayList censusDAOS=censusStateMap.values().stream().
                sorted(censusCSVComparator).
                map(censusDAO -> censusDAO.getCensusDTO(this.country)).
                collect(Collectors.toCollection(ArrayList::new));
        String sortedStateCensusJson=new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;
    }



}
