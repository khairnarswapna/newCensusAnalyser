package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface ICSVBuilder <E>{
    public Iterator<E>getCSVFileItrator(Reader reader, Class  CSVClass) throws CSVBuilderException;
}
