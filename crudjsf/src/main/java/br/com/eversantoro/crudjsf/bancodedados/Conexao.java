package br.com.eversantoro.crudjsf.bancodedados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

/**
 * 
 * Classe responsavel pela captura e disponibilizacao da conexão
 */
public class Conexao {
	private static final EntityManagerFactory emf; 

    static {
        emf = Persistence.createEntityManagerFactory("conexao");  
    }

    public static EntityManager getConexaoEM() {
    	EntityManager em = emf.createEntityManager();
    	em.setFlushMode(FlushModeType.COMMIT);
        
    	return em;
    }
    
    public static void fechaConexaoEM(EntityManager em) {
        if (em != null) {
            em.close();
        }
    }

    public static void closeEntityManagerFactory() {
    	if(emf != null && emf.isOpen()) {
   			emf.close();
    	}
    }
}
