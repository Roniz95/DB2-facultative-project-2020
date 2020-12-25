package it.polimi.db2.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import it.polimi.db2.controllers.PropagationI;

/**
 * questo servlet viene richiamato tramite il submit button di fixedQuestions.html e submitter� i valori non solo delle fixedquestions
 * ma anche delle variable questions reindirizzato l'utente ad una pagina di ringraziamenti.
 */
@WebServlet("/PropagationII")
public class PropagationII extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private PropagationI prop = new PropagationI();

    public PropagationII() {
        super();
    }
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Sono entrato in doGet poich� ho cliccato il submit button");
		Object[] answers_prop = (Object[]) request.getAttribute("answers");
		for ( Object x : answers_prop) {
			System.out.println(x);
			System.out.println("bella");
		}
		String path = "/html/greetings.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
	}



}
