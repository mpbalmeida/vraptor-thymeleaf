package br.com.faroltech.vraptor.vraptor_thymeleaf.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.StringUtils;

public class CustomMessageResolver extends StandardMessageResolver {

	private static final Logger logger = LoggerFactory.getLogger(CustomMessageResolver.class);
	
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
	
	@Override
	protected Map<String, String> resolveMessagesForTemplate(String template, ITemplateResource templateResource,
			Locale locale) {
		List<String> computeMessageResourceNamesFromBase = computeMessageResourceNamesFromBase("messages", locale);
		File file = createFileProperties(computeMessageResourceNamesFromBase);
		if (file != null) {
			try(FileInputStream fis = new FileInputStream(file)) {
				Properties properties = new Properties();
				properties.load(fis);
				setDefaultMessages(properties);
			} catch (Exception t) {
				logger.error("Failed to resolve messages for template", t);
			}
		}
		return super.resolveMessagesForTemplate(template, templateResource, locale);
	}

	private File createFileProperties(List<String> computeMessageResourceNamesFromBase) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file;
		for (String string : computeMessageResourceNamesFromBase) {
			URL resource = classLoader.getResource(string);
			if (resource != null) {
				file = new File(classLoader.getResource(string).getFile());
				if (file.exists()) {
					return file;
				}
			}
		}
		return null;
	}

	private static List<String> computeMessageResourceNamesFromBase(
            final String resourceBaseName, final Locale locale) {

        final List<String> resourceNames = new ArrayList<>(5);

        if (StringUtils.isEmptyOrWhitespace(locale.getLanguage())) {
            throw new TemplateProcessingException(
                    "Locale \"" + locale.toString() + "\" " +
                    "cannot be used as it does not specify a language.");
        }
        
        if (!StringUtils.isEmptyOrWhitespace(locale.getCountry())) {
            resourceNames.add(
                    resourceBaseName + "_" + locale.getLanguage() + "_" + locale.getCountry() + PROPERTIES_FILE_EXTENSION);
        }

        if (!StringUtils.isEmptyOrWhitespace(locale.getVariant())) {
            resourceNames.add(
                    resourceBaseName + "_" + locale.getLanguage() + "_" + locale.getCountry() + "-" + locale.getVariant() + PROPERTIES_FILE_EXTENSION);
        }
        
        resourceNames.add(resourceBaseName + "_" + locale.getLanguage() + PROPERTIES_FILE_EXTENSION);
        resourceNames.add(resourceBaseName + PROPERTIES_FILE_EXTENSION);

        return resourceNames;

    }
}
