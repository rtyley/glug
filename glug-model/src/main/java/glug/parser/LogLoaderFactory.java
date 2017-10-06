package glug.parser;

import glug.FooLogLineParser;
import glug.model.ThreadedSystem;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class LogLoaderFactory {
    public LogLoader createLoaderFor(File logFile, ThreadedSystem threadedSystem) {
        // System.out.println("Processing "+logFile);
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new InputStreamReader(streamForFile(logFile)), 8192 * 2);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        LogLoader logLoader = new LogLoader(new LogParsingReader(reader,
                new FooLogLineParser(threadedSystem)
        ));
        System.out.println("woo");
        return logLoader;
    }

    private InputStream streamForFile(File logFile) throws IOException, FileNotFoundException {
        FileInputStream uncompressedFileStream = new FileInputStream(logFile);
        if (logFile.getName().endsWith(".gz")) {
            return new GZIPInputStream(uncompressedFileStream);
        }
        return uncompressedFileStream;
    }
}
