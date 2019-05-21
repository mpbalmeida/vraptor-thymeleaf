package br.com.faroltech.vraptor.vraptor_thymeleaf.util;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.thymeleaf.templatemode.TemplateMode;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Specializes
public class ThymeleafPathResolver extends DefaultPathResolver {

	private static final String ENCODING = "UTF-8";
	private static final Long CACHE_TIME = 3600000L;

	/**
	 * @deprecated CDI eyes only
	 */
	@Deprecated
	protected ThymeleafPathResolver() {
		this(null);
	}

	@Inject
	public ThymeleafPathResolver(FormatResolver resolver) {
		super(resolver);
	}

	@Override
	protected String getPrefix() {
		return "/WEB-INF/templates/";
	}

	@Override
	protected String getExtension() {
		return "html";
	}

	protected TemplateMode getTemplateMode() {
		return TemplateMode.HTML;
	}

	protected Long getCache() {
		return CACHE_TIME;
	}

	protected String getCharacterEncoding() {
		return ENCODING;
	}

}
