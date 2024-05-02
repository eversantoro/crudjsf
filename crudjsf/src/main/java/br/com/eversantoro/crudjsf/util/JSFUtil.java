package br.com.eversantoro.crudjsf.util;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.eversantoro.crudjsf.exception.Excecoes;


/**
 * Classe responsável por gerenciar os métodos utilitários do JSF
 * @param <T> - Classe a ser utilizada
 */
public class JSFUtil<T> implements Serializable{

	private static final long serialVersionUID = -6573568232336185730L;
	
	/**
	 * Método utilizado para obter a instância o HttpServletRequest
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}
	
	/**
	 * Método utilizado para obter a instância o HttpServletResponse
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}	
	
	/**
	 * Método utilizado para adicionar um atributo na sessão
	 * @param chave - chave para ser adicionada no mapa de atributos
	 * @param valor - valor para ser adicionada no mapa de atributos
	 */
	public static void setSessionAttribute(String chave, Object valor) {
		Map<String, Object> map = getExternalContext().getSessionMap();
		map.put(chave, valor);
	}

	/**
	 * Método utilizado para obter um atributo da sessão
	 * @param chave - chave utilizada para buscar o valor do mapa de atributos da sessão
	 * @return Object
	 */
	public static Object getSessionAttribute(String chave) {
		Map<String, Object> map = getExternalContext().getSessionMap();
		return map.get(chave);
	}

	/**
	 * Método responsável por navegar para outra página
	 * @param pagina - página para qual deseja ser direcionada + extensão
	 * @throws Excecoes 
	 */
	public static void navegaPagina(String pagina) {
		NavigationHandler nh = getFacesContext().getApplication().getNavigationHandler();
		nh.handleNavigation(getFacesContext(), null, pagina);
	}

	/**
	 * Método utilizado para obter a instância o ExternalContext
	 * @return ExternalContext
	 */
	public static ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	/**
	 * Método utilizado para obter o caminho da aplicação
	 * @return String - caminho da aplicação
	 */
	public static String getCaminhoAplicacao() {
		ServletContext ctx = (ServletContext) getExternalContext().getContext();
		String realPath = ctx.getRealPath("/");
		return realPath;
	}
	
	public static String getURL() {
		return getRequest().getRequestURL().toString();
	}
	

	public static void manterMensagemAposRedirecionar() {
		getExternalContext().getFlash().setKeepMessages(true);
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static void setCookie(String name, String value, int expiry) {
		Cookie cookie = new Cookie(name, value);
		getResponse().addCookie(cookie);
	}

	public static Cookie getCookie(String name) {
		Cookie cookie = null;

		Cookie[] userCookies = getRequest().getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					return cookie;
				}
			}
		}
		return null;
	}
	
}
