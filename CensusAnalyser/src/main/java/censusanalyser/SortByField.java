package censusanalyser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class
SortByField {

    static Map<Field, Comparator> sortParameterComparator = new HashMap<>();
    enum Field{
        STATE, POPULATION, AREA, DENSITY;
    }
    SortByField() {

    }
    public static Comparator getParameter(SortByField.Field field) {

        Comparator<CensusDAO> stateComparator = Comparator.comparing(census -> census.state);
        Comparator<CensusDAO> areaComparator = Comparator.comparing(census -> census.totalArea,Comparator.reverseOrder());
        Comparator<CensusDAO> populationComparator = Comparator.comparing(census -> census.population,Comparator.reverseOrder());
        Comparator<CensusDAO> densityComparator = Comparator.comparing(census -> census.populationDensity,Comparator.reverseOrder());
        sortParameterComparator.put(Field .STATE, stateComparator);
        sortParameterComparator.put(Field .POPULATION,  populationComparator);
        sortParameterComparator.put(Field .AREA, areaComparator);
        sortParameterComparator.put(Field .DENSITY, densityComparator);

        Comparator<CensusDAO> comparator = sortParameterComparator.get(field);
        return comparator;
    }


}
