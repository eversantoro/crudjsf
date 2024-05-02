package br.com.eversantoro.crudjsf.bancodedados;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConexaoListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		Conexao.closeEntityManagerFactory();
	}

	public void contextInitialized(ServletContextEvent arg0) {
	}
	
}
