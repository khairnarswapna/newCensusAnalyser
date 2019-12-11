package CSVBuilder;

public class CSVBuilderFactory {
    public static ICSVBuilder createCSVbuilder(){
        return new OpenCSVBuilder();

    }
}
