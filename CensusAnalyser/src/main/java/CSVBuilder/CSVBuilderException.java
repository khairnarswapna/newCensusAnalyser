package CSVBuilder;

public class CSVBuilderException extends Exception {
    public  ExceptionType type;

    enum ExceptionType {
        CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE
    }

    public CSVBuilderException() {
    }

    public CSVBuilderException(String message,ExceptionType type) {
        super(message);
        this.type=type;

    }

    public CSVBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
