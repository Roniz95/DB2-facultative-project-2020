package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import exceptions.AnswerException;
import exceptions.CredentialsException;
import exceptions.MarketingQuestionException;
import model.Answer;
import model.AnswerPK;
import model.Marketingquestion;
import model.Offensiveword;
import model.Product;
import model.Questionnaire;

@Stateless
public class AnswerService {
	@PersistenceContext(unitName = "QuestionnaireEJB")
	private EntityManager em;

	public AnswerService() {

	}

	public void createAnswers(Integer UserID, Integer ProductID, List<Marketingquestion> questions, List<String> answers, Questionnaire questionnaire) throws AnswerException {
		try {
			int i = 0;
			for (Marketingquestion x : questions) {
				Answer answer = new Answer();
				AnswerPK answerPK = new AnswerPK();
				//answerPK.setProductID(ProductID);
				//answerPK.setUserID(UserID);
				//answerPK.setQuestionID(x.getQuestionID());
				//System.out.println("questa � la answerPK " + answerPK.getProductID() + " " + answerPK.getQuestionID() + " "	+ answerPK.getUserID());
				answer.setQuestionnaire(questionnaire);
				answer.setMarketingquestion(x);
				answer.setAnswer_text(answers.get(i));
				answer.setOffensive(false);
				answer.setId(answerPK);
				//System.out.println("L'user che sto inserendo ha Answer_text:" +answer.getAnswer_text()+" Offensive: " +answer.getOffensive() +"AnswerPK object:" +answer.getId() +"answer QuestionID: "+answerPK.getQuestionID()+"answer ProductID: "+answerPK.getProductID()+"answer UserID: "+answerPK.getUserID());
				em.persist(answer);
				i = i+1;
			}
			
		} catch (PersistenceException e) {
			throw new AnswerException("Could not create the answers");		
			}
	}
	
	public boolean checkAnswers(List <String> answers) throws AnswerException{
		List <Offensiveword> offensive = new ArrayList<Offensiveword>();
		List <String> off = new ArrayList<String>();
		List <String> list = new ArrayList<String>();
		String [] splitted_answ = null;		
		try {
			offensive = em.createQuery("SELECT u FROM Offensiveword u",Offensiveword.class).getResultList();
			for (Offensiveword x: offensive) {
				off.add(x.getWord());
			}			
			for (String x: answers) {
				splitted_answ = x.split(" ");
				for(String i: splitted_answ) {
					list.add(i);
				}				
			}
			for(String x: off) {
				if(list.contains(x))
					return true;
			}	
		} catch (PersistenceException e) {
			throw new AnswerException("Cannot retrieve Offensiveword ");
		}
		return false;
	}

}
