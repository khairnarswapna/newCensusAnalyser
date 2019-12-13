package censusanalyser;

public class CensusAnalyserException extends Exception {

    enum ExceptionType {
        CENSUS_FILE_PROBLEM,No_CENSUS_DATA, UNABLE_TO_PARSE, INVALID_COUNTRY, WRONG_NO_OF_FILE, NO_CENSUS_DATA, SOME_FILE_ISSUE
    }
    ExceptionType type;
    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type=ExceptionType.valueOf(name);
    }
    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
