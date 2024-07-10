package ru.clevertec.check;

import java.io.*;
import java.util.List;

public class CustomCSVWriter implements Closeable {
    private BufferedWriter writer;
    private String delimiter;

    public CustomCSVWriter(Writer writer, String delimiter) {
        this.writer = new BufferedWriter(writer);
        this.delimiter = delimiter;
    }

    public void writeNext(String[] line) throws IOException {
        writer.write(String.join(delimiter, line));
        writer.newLine();
    }

    public void writeAll(List<String[]> lines) throws IOException {
        for (String[] line : lines) {
            writeNext(line);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
