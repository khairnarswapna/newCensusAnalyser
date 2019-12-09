package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder<E> implements ICSVBuilder {

    @Override
    public Iterator<E> getCSVFileItrator(Reader reader, Class csvClass) throws CSVBuilderException {
            return this.getCSVToBean(reader, csvClass).iterator();

    }
    @Override
    public List getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException {
        return  this.getCSVToBean(reader, csvClass).parse();
    }

    private CsvToBean<E> getCSVToBean(Reader reader, Class csvClass) throws CSVBuilderException {

        try {
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(csvClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            return csvToBeanBuilder.build();

        } catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(),
                    CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }
    }


}