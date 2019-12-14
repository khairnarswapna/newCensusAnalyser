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
    public String getStateWithSortByField(SortByField.Field... field) throws CensusAnalyserException {
        Comparator<CensusDAO> censusCSVComparator=null;
        if (censusStateMap == null || censusStateMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data",
                    CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        if (field.length==2) {
            censusCSVComparator = SortByField.getParameter(field[0]).thenComparing(SortByField.getParameter(field[1]));
        } else
            {
            censusCSVComparator = SortByField.getParameter(field[0]);
        }
        ArrayList censusDTO = censusStateMap.values().stream().
                sorted(censusCSVComparator).
                map(censusDAO -> censusDAO.getCensusDTO(country)).
                collect(Collectors.toCollection(ArrayList::new));
        String sortedStateCensusJson = new Gson().toJson(censusDTO);
        return sortedStateCensusJson;
    }

}
