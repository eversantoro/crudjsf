package br.com.eversantoro.crudjsf.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Classe Controladora de Mensagens
 */
public class Mensagem {
	
	public static String getMensagemPropertiesSemFaces(String textoChave, Object[] parametros){
		String retorno = null;
		ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
		retorno = resourceBundle.getString(textoChave);
		
		if(parametros != null){
			MessageFormat mf = new MessageFormat(retorno);
			retorno = mf.format(parametros, new StringBuffer(), null).toString();
		}

		return retorno;
	}
	
	public static String getMensagemPropertiesSemFaces(String textoChave){
		return getMensagemPropertiesSemFaces(textoChave, null);
	}
	
	/**
	 * Método utilizado para buscar a mensagem através da chave no Applicatrion Resources
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 * @param parametros - parâmetros a serem adicionados na mensagem
	 * @return Mensagem encontrada no ApplicationResources
	 */
	public static String getMensagemProperties(String textoChave, Object[] parametros){
		String retorno = null;
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context.getViewRoot().getLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources", locale);
		retorno = resourceBundle.getString(textoChave);
		
		if(parametros != null){
			MessageFormat mf = new MessageFormat(retorno, locale);
			retorno = mf.format(parametros, new StringBuffer(), null).toString();
		}

		return retorno;
	}
	
	/**
	 * Método utilizado para chamar o getMensagemProperties sobreposto
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 * @return Mensagem encontrada no ApplicationResources
	 */
	public static String getMensagemProperties(String textoChave){
		return getMensagemProperties(textoChave, null);
	}

	/**
	 * Método que adiciona a mensagem no facesContext
	 * @param mensagem - Mensagem a ser adicionada no FacesContext
	 * @param tipoMensagem - nível crítico da mensagem
	 */
	public static void mostraMensagem(String mensagem, Severity tipoMensagem) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(tipoMensagem, mensagem, null);
		fc.addMessage(null, message);
	}
	
	/**
	 * Método que recebe uma lista de erros e adiciona todos no facesContext
	 * @param erros - list de array de strings com resumo e detalhe da mensagem
	 * @param target - componente alvo
	 * @param tipoMensagem - nivel critico da mensagem
	 */
	public static void mostraErros(List<String[]> erros, String target, Severity tipoMensagem){
		mostraErros(erros, target, tipoMensagem, ":");
	}
	
	public static void mostraErros(List<String[]> erros, String target, Severity tipoMensagem, String separador){
		for(String[] erro : erros){
			int i = 0;
			mostraMensagem(erro[i++]+separador, erro[i++], target, tipoMensagem);
		}
	}
	
	/**
	 * Método que adiciona mensagem no facesContext
	 * @param resumo - resulmo da mensagem
	 * @param detalhe - detalhe da mensagem
	 * @param target - componente alvo
	 * @param tipoMensagem - nivel critico da mensagem
	 */
	public static void mostraMensagem(String resumo, String detalhe, String target, Severity tipoMensagem){
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(tipoMensagem, resumo, detalhe);
		fc.addMessage(target, message);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como fatal
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 * @param parametros - parâmetros a serem adicionados na mensagem
	 */
	public static void erro(String textoChave, String[] parametros) {
		String mensagem = getMensagemProperties(textoChave, parametros);
		mostraMensagem(mensagem, FacesMessage.SEVERITY_ERROR);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como fatal
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 */
	public static void erro(String textoChave) {
		String mensagem = getMensagemProperties(textoChave);
		mostraMensagem(mensagem, FacesMessage.SEVERITY_ERROR);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como fatal
	 * @param mensagem - texto a ser exibido na mensagem
	 */
	public static void erroSemProperties(String mensagem) {
		mostraMensagem(mensagem, FacesMessage.SEVERITY_ERROR);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como aviso
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 * @param parametros - parâmetros a serem adicionados na mensagem
	 */
	public static void aviso(String textoChave, String[] parametros) {
		String mensagem = getMensagemProperties(textoChave, parametros);
		mostraMensagem(mensagem, FacesMessage.SEVERITY_WARN);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como aviso
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 */
	public static void aviso(String textoChave) {
		String mensagem = getMensagemProperties(textoChave);
		mostraMensagem(mensagem, FacesMessage.SEVERITY_WARN);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como informação
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 * @param parametros - parâmetros a serem adicionados na mensagem
	 */
	public static void informacao(String textoChave, String[] parametros) {
		String mensagem = getMensagemProperties(textoChave, parametros);
		mostraMensagem(mensagem, FacesMessage.SEVERITY_INFO);
	}
	
	/**
	 * Método responsável por definir o nível crítico da mensagem como informação
	 * @param textoChave - chave a ser buscada no ApplicationResources
	 */
	public static void informacao(String textoChave) {
		String mensagem = getMensagemProperties(textoChave);
		mostraMensagem(mensagem, FacesMessage.SEVERITY_INFO);
	}
	
}