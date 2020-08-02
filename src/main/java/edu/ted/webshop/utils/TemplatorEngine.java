package edu.ted.webshop.utils;

import edu.ted.templator.TemplateProcessor;
import edu.ted.webshop.utils.interfaces.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.Writer;
import java.util.Map;

public class TemplatorEngine implements TemplateEngine {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private TemplateProcessor processor;

    public TemplatorEngine(TemplateProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void writePage(String page, Writer writer, Map fieldsMap) {
        try {
            processor.process(page, fieldsMap, writer);
        } catch (FileNotFoundException e) {
            logger.error("Template Engine Error occurred:", e);
            logger.error("Template page:", page);
        }
    }

    @Override
    public void writeString(String templateName, String templateStr, Writer writer, Map<String, Object> fieldsMap) throws Exception {

    }
}
