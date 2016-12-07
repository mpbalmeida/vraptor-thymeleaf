package br.com.faroltech.vraptor.vraptor_thymeleaf.util;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import br.com.caelum.vraptor.core.MethodInfo;

@ApplicationScoped
public class ThymeleafComponent {

	@Inject
	private ThymeleafPathResolver resolver;

	private ServletContextTemplateResolver templateResolver;

	private TemplateEngine templateEngine;
	
	@Inject private ServletContext servletContext;

	@PostConstruct
	private void init() {
		templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(resolver.getTemplateMode());
		templateResolver.setPrefix(resolver.getPrefix());
		templateResolver.setSuffix("." + resolver.getExtension());
		templateResolver.setCharacterEncoding(resolver.getCharacterEncoding());
		templateResolver.setCacheTTLMs(resolver.getCache());
		templateResolver.setCacheable(false);

		templateEngine = new TemplateEngine();
		StandardMessageResolver standardMessageResolver = new CustomMessageResolver();
		templateEngine.setMessageResolver(standardMessageResolver);
		templateEngine.setTemplateResolver(templateResolver);
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public String getTemplateName(final MethodInfo methodInfo) {
		final String to = resolver.pathFor(methodInfo.getControllerMethod());
		return to.replace(templateResolver.getPrefix(), "").replace(templateResolver.getSuffix(), "");
	}

}
