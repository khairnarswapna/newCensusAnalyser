package censusanalyser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class CommanCSV implements ICSVBuilder{

    @Override
    public Iterator getCSVFileItrator(Reader reader, Class csvClass) throws CSVBuilderException {
        return this.getCSVBean(reader, csvClass).iterator();
    }

    @Override
    public List getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException {
        try {
            return this.getCSVBean(reader, csvClass).getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private CSVParser getCSVBean(Reader reader, Class csvClass) {
        try {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());
            return csvParser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
