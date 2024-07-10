package ru.clevertec.check;

import java.io.*;
import java.util.*;

public class CustomCSVReader implements Closeable {
    private BufferedReader reader;
    private String delimiter;

    public CustomCSVReader(Reader reader, String delimiter) {
        this.reader = new BufferedReader(reader);
        this.delimiter = delimiter;
    }

    public List<String[]> readAll() throws IOException {
        List<String[]> records = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            records.add(line.split(delimiter));
        }
        return records;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
