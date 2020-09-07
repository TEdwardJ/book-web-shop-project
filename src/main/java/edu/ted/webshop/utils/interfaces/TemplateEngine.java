package edu.ted.webshop.utils.interfaces;

import java.io.Writer;
import java.util.Map;

public interface TemplateEngine {

    public void writePage(String page, Writer writer, Map<String, Object> fieldsMap);

    public void writeString(String templateName, String templateStr, Writer writer, Map<String, Object> fieldsMap) throws Exception;


    }
