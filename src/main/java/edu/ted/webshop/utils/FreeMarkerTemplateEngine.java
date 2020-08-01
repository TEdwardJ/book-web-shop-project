package edu.ted.webshop.utils;

import freemarker.core.AliasTemplateNumberFormatFactory;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerTemplateEngine {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String baseTemplatePath;

    private Configuration configuration;

    public FreeMarkerTemplateEngine(String baseTemplatePath) {
        this.baseTemplatePath = baseTemplatePath;
    }

    public String getBaseTemplatePath() {
        return baseTemplatePath;
    }

    public void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setClassForTemplateLoading(this.getClass(), baseTemplatePath);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        Map<String, TemplateNumberFormatFactory> customNumberFormats = new HashMap<String, TemplateNumberFormatFactory>();
        customNumberFormats.put("price", new AliasTemplateNumberFormatFactory("#,##0.00 ¤;; currencyCode=UAH"));
        configuration.setCustomNumberFormats(customNumberFormats);
    }

    public void writePage(String page, Writer writer, Map fieldsMap) {
        try {
            Template template = configuration.getTemplate(page);
            template.process(fieldsMap, writer);
        } catch (Exception e) {
            logger.error("Template Engine Error occurred:", e);
            logger.error("Template page:", page);
        }
    }

    public void writeString(String templateName, String templateStr, Writer writer, Map<String, Object> fieldsMap) throws TemplateException, IOException {
        try {
            Template template = new Template(templateName, new StringReader(templateStr),
                    configuration);
            template.process(fieldsMap, writer);
        } catch (Exception e) {
            logger.error("Template Engine Error occurred when string {} was preparing:", templateStr, e);
            logger.error("Template string:", templateName);
            throw e;
        }
    }
}
