package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder <E>{

    public Iterator<E>getCSVFileItrator(Reader reader, Class  csvClass) throws CSVBuilderException;
    public List<E> getCSVFileList(Reader reader, Class  csvClass) throws CSVBuilderException, IOException;
}
