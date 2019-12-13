package censusanalyser;

public class CensusAdapterFactory {

    public static CensusAdapter getCreateCensusAdapter(CensusAnalyser.Country country) throws CensusAnalyserException {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusAdapter();
            return new USCensusAdapter();
    }
}
