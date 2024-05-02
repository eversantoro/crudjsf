package br.com.eversantoro.crudjsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.BeanValidator;

public class BeanValidatorSis extends BeanValidator {
    public void validate(FacesContext context, UIComponent component, Object value) {
        if (doValidation(context)) {
            super.validate(context, component, value);
        }
    }

    private boolean doValidation(FacesContext context) {
   		String value = context.getExternalContext().getRequestParameterMap().get("validateBean");
   		if ( value != null) {
       		return Boolean.valueOf(value);
       	}
        	
       	return false;            
    }
}