package censusanalyser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SortByField {

    static Map<Field, Comparator> sortParameterComparator = new HashMap<>();

    enum Field{
        STATE, POPULATION, AREA, DENSITY;
    }

    SortByField() {

    }
    public static Comparator getParameter(SortByField.Field field) {

        Comparator<IndiaCensusDAO> stateComparator = Comparator.comparing(census -> census.state);
        Comparator<IndiaCensusDAO> areaComparator = Comparator.comparing(census -> census.areaInSqKm);
        Comparator<IndiaCensusDAO> populationComparator = Comparator.comparing(census -> census.population);
        Comparator<IndiaCensusDAO> densityComparator = Comparator.comparing(census -> census.densityPerSqKm);

        sortParameterComparator.put(Field .STATE, stateComparator);
        sortParameterComparator.put(Field .POPULATION,  populationComparator);
        sortParameterComparator.put(Field .AREA, areaComparator);
        sortParameterComparator.put(Field .DENSITY, densityComparator);

        Comparator<IndiaCensusDAO> comparator = sortParameterComparator.get(field);

        return comparator;
    }


}
