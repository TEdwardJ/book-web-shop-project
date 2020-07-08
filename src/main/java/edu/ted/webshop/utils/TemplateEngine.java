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

public final class TemplateEngine {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String BASE_TEMPLATE_PATH = "/product/";

    private static final TemplateEngine ENGINE_INSTANCE = new TemplateEngine();
    private final Configuration webConfiguration;
    private final Configuration dbConfiguration;

    private TemplateEngine() {
        webConfiguration = new Configuration(Configuration.VERSION_2_3_30);
        webConfiguration.setClassForTemplateLoading(WebShopServer.class, BASE_TEMPLATE_PATH);
        // Recommended settings for new projects:
        webConfiguration.setDefaultEncoding("UTF-8");
        webConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        webConfiguration.setLogTemplateExceptions(false);
        webConfiguration.setWrapUncheckedExceptions(true);
        webConfiguration.setFallbackOnNullLoopVariable(false);
        Map<String, TemplateNumberFormatFactory> customNumberFormats
                = new HashMap<String, TemplateNumberFormatFactory>();
        customNumberFormats.put("price", new AliasTemplateNumberFormatFactory("0.00"));
        webConfiguration.setCustomNumberFormats(customNumberFormats);

        //
        dbConfiguration = new Configuration(Configuration.VERSION_2_3_30);
        dbConfiguration.setClassForTemplateLoading(WebShopServer.class, BASE_TEMPLATE_PATH);
        // Recommended settings for new projects:
        dbConfiguration.setDefaultEncoding("UTF-8");
        dbConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        dbConfiguration.setLogTemplateExceptions(false);
        dbConfiguration.setWrapUncheckedExceptions(true);
        dbConfiguration.setFallbackOnNullLoopVariable(false);
        Map<String, TemplateNumberFormatFactory> dbCustomNumberFormats
                = new HashMap<String, TemplateNumberFormatFactory>();
        dbCustomNumberFormats.put("price", new AliasTemplateNumberFormatFactory("0.##"));
        dbConfiguration.setCustomNumberFormats(dbCustomNumberFormats);
    }

    public static TemplateEngine getInstance() {
        return ENGINE_INSTANCE;
    }

    public void writePage(String page, Writer writer, Map fieldsMap) {
        try {
            Template template = webConfiguration.getTemplate(page);
            template.process(fieldsMap, writer);
        } catch (TemplateException e) {
            logger.error("Template Engine Error occured: {}", e);
            logger.error("Template page: {}", page);
            //throw e;
        } catch (IOException e) {
            logger.error("Template Engine Error occured: {}", e);
            logger.error("Template page: {}", page);
            //throw e;
        }

    }

    public void writeString(String templateName, String templateStr, Writer writer, Map fieldsMap) throws TemplateException, IOException {
        try {
            Template template = new Template(templateName, new StringReader(templateStr),
                    dbConfiguration);
            template.process(fieldsMap, writer);
        } catch (TemplateException e) {
            logger.error("Template Engine Error occured when SQL query was preparing: {}", e);
            logger.error("Template string: {}", templateName);
            throw e;
        } catch (IOException e) {
            logger.error("Template Engine Error occured when SQL query was preparing: {}", e);
            logger.error("Template string: {}", templateName);
            throw e;
        }

    }
}
