package it.polimi.db2.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
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

import model.Product;
import services.MarketingQuestionService;
import services.ProductService;

/**
 * Servlet implementation class CreateQuestionnaire
 */
@WebServlet("/AdministrativeTools/CreateQuestionnaire")
public class CreateQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
    @EJB(name="services/ProductService")   
    ProductService productService;
    
    @EJB(name="services/MarketingQuestionService")   
    MarketingQuestionService marketingQuestionService;
    
    public CreateQuestionnaire() {
        super();
    }
    
    public void init() {
    	ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productName= request.getParameter("productName");
		String imagePath=request.getParameter("imagePath");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date startDate;
		try {
			startDate = (Date) sdf.parse(request.getParameter("date"));
			
			if(productService.alreadyScheduled(startDate)) {
				String path = "/html/noProduct.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("error", "a questionnaire for the specified date is already present !");
				templateEngine.process(path, ctx, response.getWriter());
				return;
			}
		} catch (ParseException e1) {
			
			e1.printStackTrace();
		}
		
		
		
		try {
			startDate = (Date) sdf.parse(request.getParameter("date"));
			productService.createProduct(productName, imagePath,startDate);
			String[] questions = request.getParameterValues("question");
			for(String question : questions) {
				
				Product product=productService.findProductByDate(startDate);
				marketingQuestionService.createQuestion(question, product);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String path = "/QuestionnaireWeb/AdministrativeTools";
		response.sendRedirect(path);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path;
		path = "/html/createQuestionnaire.html";
		
		templateEngine.process(path, ctx, response.getWriter());
	}

}
