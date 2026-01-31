package com.work.reader;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.batch.item.ItemReader;
import java.io.FileInputStream;

public class WordDocumentReader implements ItemReader<String> {
    private final String filePath;
    private boolean read = false;

    public WordDocumentReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String read() throws Exception {
        if (!read) {
            try (FileInputStream fis = new FileInputStream(filePath);
                 XWPFDocument document = new XWPFDocument(fis);
                 XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                read = true;
                return extractor.getText(); // Return text from Word doc
            }
        }
        return null; // Signals end of input
    }
}