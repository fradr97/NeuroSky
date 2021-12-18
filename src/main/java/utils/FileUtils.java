package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.List;

public class FileUtils {

    public List<String[]> parseCSVFile(String filepath) {
        List<String[]> r;
        try (CSVReader reader = new CSVReader(new FileReader(filepath))) {
            r = reader.readAll();
            return r;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeFile(String filepath, List<String[]> list, boolean append) {
        try {
            File myObj = new File(filepath);

            if(myObj.createNewFile()) {
                System.out.println("File created.");
            }

            FileWriter fileWriter = new FileWriter(filepath, append);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            csvWriter.writeAll(list);
            csvWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
