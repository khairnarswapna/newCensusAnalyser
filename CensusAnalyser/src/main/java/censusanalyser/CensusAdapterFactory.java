package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory {

    public static CensusAdapter getCreateCensusAdapter(CensusAnalyser.Country country) throws CensusAnalyserException {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusAdapter();
        else if(country.equals(CensusAnalyser.Country.US))
            return new USCensusAdapter();
        else
            throw new CensusAnalyserException("unknownCountry",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}
