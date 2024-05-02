package br.com.eversantoro.crudjsf.converter;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.hibernate.proxy.HibernateProxy;

import br.com.eversantoro.crudjsf.util.Mensagem;

/**
 * Método genérico responsável por fazer a conversão dos objetos do selectItem
 *
 */
@FacesConverter("ConverterDefault")
public class ConverterDefault implements Converter{

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
		 if(value != null && !value.equals("")) {
			 try {
				 Long id = Long.valueOf(value);
			
				 for(UIComponent componentChild :component.getChildren()) {
					 if(componentChild.getClass().equals(UISelectItems.class)) {
						 Collection<?> collection =  (Collection<?>) componentChild.getAttributes().get("value");
						 return findById(collection, id);  
					 }
				 }
			 } catch(Exception ex){
				 String[] parametros = new String[2];
				 parametros[0] = value;
				 parametros[1] = component.getId();
				 
				 throw new RuntimeException(Mensagem.getMensagemProperties("ConversorErroValor", parametros), ex);
			 }  
		 }
		 
		 return null;
	}
	
	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
		if(object == null) {
			return "";
		}
		return getIdByReflection(object).toString();
	}
	
	/**
	 * @param collection - lista de Collection
	 * @param idToFind
	 * @return
	 */
	private Object findById(Collection<?> collection, Long idToFind) {  
		for (Object obj : collection) {
			Long id = getIdByReflection(obj);
			
			if (id.equals(idToFind)) {  
				return obj;
			}
		}
		
		return null;  
    }  

	/**
	 * @param object
	 * @return
	 */
	private Long getIdByReflection(Object object) {  
		try {
			if (object instanceof HibernateProxy) {
				object = ((HibernateProxy)object).getHibernateLazyInitializer().getImplementation();
			}
			Field idField = object.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			return (Long)(idField.get(object) == null ? 0L : idField.get(object));
		} catch(Exception ex){
			throw new RuntimeException(Mensagem.getMensagemProperties("ConversorErroId"), ex);
		}  
	}  
}
