package censusanalyser;

public class IndiaCensusDAO {

    public  String state;
    public String stateCode;
    public int population;
    public int areaInSqKm;
    public int densityPerSqKm;

    public IndiaCensusDAO() {

    }

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqKm =indiaCensusCSV.densityPerSqKm;

    }
}
