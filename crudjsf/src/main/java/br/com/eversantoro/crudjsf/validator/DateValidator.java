package br.com.eversantoro.crudjsf.validator;

import java.text.DateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.eversantoro.crudjsf.util.Mensagem;

@FacesValidator("dateValidator")
public class DateValidator implements Validator {

	 public void validate(FacesContext context, UIComponent component, Object value) {
		 try {
			 String valor = context.getExternalContext().getRequestParameterMap().get(component.getClientId());
			 
			 int style = DateFormat.SHORT;
			 
			 DateFormat formatter = DateFormat.getDateInstance(style);
			 formatter.setLenient(false);
			 formatter.parse(valor); 
		 } catch(Exception e) {
			 throw new ValidatorException(new FacesMessage(Mensagem.getMensagemProperties("dataInvalida")));
		 }   
	 }
}
