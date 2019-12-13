package censusanalyser;

public class CensusDAO {

    public  String state;
    public String stateCode;
    public int population;
    public int areaInSqKm;
    public double populationDensity;
    public double totalArea;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        populationDensity=indiaCensusCSV.densityPerSqKm;
    }
    public CensusDAO(USCensusCSV usCensusCSV){

        state = usCensusCSV.state;
        stateCode = usCensusCSV.stateId;
        population=usCensusCSV.population;
        populationDensity=usCensusCSV.populationDensity;
        totalArea=usCensusCSV.totalArea;

    }
    public Object getCensusDTO(CensusAnalyser.Country country) {
        if (country.equals(CensusAnalyser.Country.US))
            return new USCensusCSV(state,stateCode,population,(int)populationDensity,(int)totalArea);
        return new IndiaCensusCSV(state,population, (int)populationDensity,(int)totalArea);
    }
}
