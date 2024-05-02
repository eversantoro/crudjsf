package br.com.eversantoro.crudjsf.converter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

@FacesConverter(value = "PickListConverter")
public class PickListConverter<T extends Serializable> implements Converter {
	@SuppressWarnings("rawtypes")
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		Object ret = null;
		if (arg1 instanceof PickList) {
			Object dualList = ((PickList) arg1).getValue();
			DualListModel dl = (DualListModel) dualList;
			for (Iterator iterator = dl.getSource().iterator(); iterator
					.hasNext();) {
				Object o = iterator.next();
				String id = getId(o);
				if (arg2.equals(id)) {
					ret = o;
					break;
				}
			}
			if (ret == null) {
				for (Iterator iterator1 = dl.getTarget().iterator(); iterator1
						.hasNext();) {
					Object o = iterator1.next();
					String id = getId(o);
					if (arg2.equals(id)) {
						ret = o;
						break;
					}
				}
			}
		}
		return ret;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String str = "";
		if (arg2 instanceof Object)
			str = getId(arg2).toString();
		return str;
	}
	
	private String getId(Object objeto) {
		try {
			Field atributos[] = objeto.getClass().getDeclaredFields();

			for (int i = 0; i < atributos.length; i++) {
				Field atributo = atributos[i];

				if (atributo.getName().equalsIgnoreCase("id")) {
					if (!atributo.isAccessible()) {
						atributo.setAccessible(true);
					}

					return String.valueOf(atributo.get(objeto));
				}
			}
		} catch (IllegalArgumentException e) {
		} 
		catch (IllegalAccessException e) {
		}

		return null;
	}
}
