package edu.ted.webshop.utils;

import edu.ted.webshop.server.WebShopServer;
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

public class TemplateEngine {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String baseTemplatePath;

    private Configuration webConfiguration;

    public TemplateEngine(String baseTemplatePath) {
        this.baseTemplatePath = baseTemplatePath;
    }

    public String getBaseTemplatePath() {
        return baseTemplatePath;
    }

    public void init() {
        webConfiguration = new Configuration(Configuration.VERSION_2_3_30);
        webConfiguration.setClassForTemplateLoading(WebShopServer.class, baseTemplatePath);
        webConfiguration.setDefaultEncoding("UTF-8");
        webConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        webConfiguration.setLogTemplateExceptions(false);
        webConfiguration.setWrapUncheckedExceptions(true);
        webConfiguration.setFallbackOnNullLoopVariable(false);
        Map<String, TemplateNumberFormatFactory> customNumberFormats = new HashMap<String, TemplateNumberFormatFactory>();
        customNumberFormats.put("price", new AliasTemplateNumberFormatFactory("#,##0.00 ¤;; currencyCode=UAH"));
        webConfiguration.setCustomNumberFormats(customNumberFormats);
    }

    public void writePage(String page, Writer writer, Map fieldsMap) {
        try {
            Template template = webConfiguration.getTemplate(page);
            template.process(fieldsMap, writer);
        } catch (TemplateException e) {
            logger.error("Template Engine Error occurred: {}", e);
            logger.error("Template page: {}", page);
        } catch (IOException e) {
            logger.error("Template Engine Error occurred: {}", e);
            logger.error("Template page: {}", page);
        }
    }

    public void writeString(String templateName, String templateStr, Writer writer, Map<String, Object> fieldsMap) throws TemplateException, IOException {
        try {
            Template template = new Template(templateName, new StringReader(templateStr),
                    webConfiguration);
            template.process(fieldsMap, writer);
        } catch (TemplateException e) {
            logger.error("Template Engine Error occurred when SQL query was preparing: {}", e);
            logger.error("Template string: {}", templateName);
            throw e;
        } catch (IOException e) {
            logger.error("Template Engine Error occurred when SQL query was preparing: {}", e);
            logger.error("Template string: {}", templateName);
            throw e;
        }
    }
}
