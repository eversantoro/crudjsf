package br.com.eversantoro.crudjsf.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * Classe respons�vel por gerenciar os m�todos utilit�rios do JSF
 * @param <T> - Classe a ser utilizada
 */
public class ComboUtil<T> implements Serializable{

	private static final long serialVersionUID = -6573568232336185730L;
	
	public static List<SelectItem> comboSexo() {
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem("M", "Masculino"));
		retorno.add(new SelectItem("F", "Feminino"));
		return retorno;
	}
	
	public static List<SelectItem> comboEstados() {
		List<SelectItem> retorno = new ArrayList<SelectItem>();
		retorno.add(new SelectItem("AC", "AC"));
        retorno.add(new SelectItem("AL", "AL"));
        retorno.add(new SelectItem("AP", "AP"));
        retorno.add(new SelectItem("AM", "AM"));
        retorno.add(new SelectItem("BA", "BA"));
        retorno.add(new SelectItem("CE", "CE"));
        retorno.add(new SelectItem("DF", "DF"));
        retorno.add(new SelectItem("ES", "ES"));
        retorno.add(new SelectItem("GO", "GO"));
        retorno.add(new SelectItem("MA", "MA"));
        retorno.add(new SelectItem("MT", "MT"));
        retorno.add(new SelectItem("MS", "MS"));
        retorno.add(new SelectItem("MG", "MG"));
        retorno.add(new SelectItem("PA", "PA"));
        retorno.add(new SelectItem("PB", "PB"));
        retorno.add(new SelectItem("PR", "PR"));
        retorno.add(new SelectItem("PE", "PE"));
        retorno.add(new SelectItem("PI", "PI"));
        retorno.add(new SelectItem("RJ", "RJ"));
        retorno.add(new SelectItem("RN", "RN"));
        retorno.add(new SelectItem("RS", "RS"));
        retorno.add(new SelectItem("RO", "RO"));
        retorno.add(new SelectItem("RR", "RR"));
        retorno.add(new SelectItem("SC", "SC"));
        retorno.add(new SelectItem("SP", "SP"));
        retorno.add(new SelectItem("SE", "SE"));
        retorno.add(new SelectItem("TO", "TO"));
		return retorno;
	}
	
}
