package br.com.eversantoro.crudjsf.bancodedados;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.proxy.HibernateProxy;

import br.com.eversantoro.crudjsf.exception.Excecoes;
import br.com.eversantoro.crudjsf.objeto.JoinClasseVO;


/**
 * Classe Genérica para a manipulação de objetos
 * @param <T> - Classe Utilizada para o DAO
 */
public abstract class Dao<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 6176187069298051614L;

	protected String nomeEntidade;
	protected Class<T> classe;

	/**
	 * Construtor
	 * @param classe - classe utilizada no construtor
	 */
	public Dao(Class<T> classe) {
		this.nomeEntidade = classe.getName();
		this.classe = classe;
	}
	
	public T getObjetoFiltro(T obj) {
		try {
			T retorno = classe.newInstance();
			setValorObjeto(retorno, "id", retornaValorObjeto(obj, "id"));
			return retorno;
		} catch (Exception e) {
			return null;
		}
	} 
	
	/**
	 * @param obj - objeto a ser alterado
	 * @return obj - objeto alterado
	 * @throws Excecoes
	 */
	public T alterar(T obj) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		
		try {
			beginTransaction(em);
			obj = em.merge(obj);
			em.getTransaction().commit();
			
			return obj;
		} catch (OptimisticLockException eO) {
			throw new Excecoes("mensagem.erroJversion");
		} catch (RollbackException ex) {
			Throwable tr = ex.getCause(); 
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintAlterar");
			}
			throw new Excecoes(ex, "mensagem.erroAlterarRegistro");
		} catch (Exception ex) {
			throw new Excecoes(ex, "mensagem.erroAlterarRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * @param obj - objeto a ser salvo
	 * @return obj - objeto salvo
	 * @throws Excecoes
	 */
//	@SuppressWarnings("unchecked")
	public T salvar(T obj) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
//		T objetoClonado = (T) SerializationUtils.clone(obj); 
		try {
			beginTransaction(em);
			
			em.flush();
//			em.persist(objetoClonado);
			em.persist(obj);
			em.getTransaction().commit();
			
			return obj;
//			return objetoClonado;
		} catch (RollbackException ex) {
			
			setValorObjeto(obj, "id", null);
			
			Throwable tr = ex.getCause(); 
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintSalvar");
			}
			
			throw new Excecoes(ex, "mensagem.erroSalvarRegistro");
		} catch (Exception ex) {
			setValorObjeto(obj, "id", null);
			throw new Excecoes(ex, "mensagem.erroSalvarRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * @param listaObj - lista de objetos a serem salvos
	 * @return listaObj - lisa de objetos salvos
	 * @throws Excecoes
	 */
	public List<T> salvarLista(List<T> listaObj) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			beginTransaction(em);
			for(T obj : listaObj) {
				em.persist(obj);
			}
			em.getTransaction().commit();
			
			return listaObj;
		} catch (RollbackException ex) {
			Throwable tr = ex.getCause(); 
			
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintSalvar");
			}

			while (tr != null) {
				if(tr instanceof SQLException) {
					int codigoErro = ((SQLException) tr).getErrorCode();
					String mensagem = "Erro";
					if(mensagem != null) {
						throw new Excecoes(ex, mensagem);
					} else {
						tr = null;
					}
				} else {
					tr = tr.getCause();
				}
			}
			
			throw new Excecoes(ex, "mensagem.erroSalvarRegistro");
		} catch (Exception ex) {
		
			throw new Excecoes(ex, "mensagem.erroSalvarRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}

	/**
	 * @param obj - objeto a ser apagado
	 * @throws Excecoes
	 */
	public void excluir(Long id) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			beginTransaction(em);
			
			em.remove(find(em, null, id));
			
			em.getTransaction().commit();
		} catch (RollbackException ex) {
			Throwable tr = ex.getCause(); 
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintExcluir");
			}
			
			throw new Excecoes(ex, "mensagem.erroExcluirRegistro");
		} catch (Excecoes ex) {
			throw new Excecoes(ex.getMessage());
		} catch (Exception ex) {
			throw new Excecoes(ex, "mensagem.erroExcluirRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * @param listaObj - lista de objetos a serem apagados
	 * @throws Excecoes
	 */
	public void excluirLista(List<T> listaObj) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			beginTransaction(em);
			Field idField;
			for(T obj : listaObj) {
				idField = obj.getClass().getDeclaredField("id");
				idField.setAccessible(true);
				
				em.remove(find(em, null, (Long)(idField.get(obj) == null ? 0L : idField.get(obj))));
				
			}
			
			em.getTransaction().commit();
		} catch (RollbackException ex) {
			Throwable tr = ex.getCause(); 
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintExcluir");
			}
			
			throw new Excecoes(ex, "mensagem.erroExcluirRegistro");
		} catch (Excecoes ex) {
			throw new Excecoes(ex.getMessage());
		} catch (Exception ex) {
			throw new Excecoes(ex, "mensagem.erroExcluirRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * @param listaSalvar - lista de objetos a serem salvos
	 * @param listaExcluir - lista de objetos a serem apagados
	 * @throws Excecoes
	 */
	public void salvarExcluirLista(List<T> listaSalvar, List<T> listaExcluir) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			beginTransaction(em);
			if(listaExcluir != null && !listaExcluir.isEmpty()) {
				StringBuilder sql = new StringBuilder();
				sql.append("DELETE FROM ").append(nomeEntidade).append(" entidade").append(" WHERE entidade IN (:listaExcluir)");
				
				em.createQuery(sql.toString())
				.setParameter("listaExcluir", listaExcluir)
				.executeUpdate();
			}

			for(T obj : listaSalvar) {
				em.persist(obj);
			}
			
			em.getTransaction().commit();
		} catch (RollbackException ex) {
			Throwable tr = ex.getCause(); 
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintSalvar");
			}
			
			throw new Excecoes(ex, "mensagem.erroSalvarRegistro");
		} catch (Exception ex) {
			throw new Excecoes(ex, "mensagem.erroSalvarRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * @param listaSalvar - lista de objetos a serem salvos
	 * @param listaExcluir - lista de objetos a serem apagados
	 * @throws Excecoes
	 */
	public List<T> alterarLista(List<T> listaAlterar) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			beginTransaction(em);

			for(T obj : listaAlterar) {
				obj = em.merge(obj);
				listaAlterar.set(listaAlterar.indexOf(obj), obj);
			}
			
			em.getTransaction().commit();
			
			return listaAlterar;
		} catch (RollbackException ex) {
			Throwable tr = ex.getCause(); 
			if(tr.getCause() instanceof ConstraintViolationException) {
				throw new Excecoes(ex, "mensagem.erroConstraintAlterar");
			}
			
			throw new Excecoes(ex, "mensagem.erroAlterarRegistro");
		} catch (Exception ex) {
			throw new Excecoes(ex, "mensagem.erroAlterarRegistro");
		} finally {
			if(em != null && em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			
			Conexao.fechaConexaoEM(em);
		}
	}

	/**
	 * @param id - ID a ser buscado na entidade
	 * @return objeto da entidade
	 * @throws Excecoes
	 */
	@SuppressWarnings("unchecked")
	public T getObjetoPorId(Long id) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			return (T) em.createQuery("select object(c) from " + nomeEntidade + " as c where c.id = :id" )
			.setParameter("id", id)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch(Exception e) {
			throw new Excecoes(e, "mensagem.erroPesquisarRegistro");
		} finally {
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * @param id - ID a ser buscado na entidade
	 * @param classeInterna - array contendo as classes que deverão ser utilizadas no Left Join
	 * @return objeto da entidade
	 * @throws Excecoes
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T getObjetoPorId(Long id, Class[] classeInterna) throws Excecoes {
		EntityManager em = Conexao.getConexaoEM();
		try {
			StringBuilder sql = new StringBuilder();
			String nomeEntidade = getNomeEntidade(classe);
			sql.append("SELECT ").append(nomeEntidade).append(" FROM ").append(classe.getCanonicalName()).append(" ").append(nomeEntidade).append(" ");
			
			if(classeInterna != null) {
				for(Class obj : classeInterna) {
					sql.append(concatenaObjetosRelacionados(classe, obj));
				}
			}
			
			sql.append(" WHERE ").append(nomeEntidade).append(".id = :id");
			
			return (T) em.createQuery(sql.toString())
						.setParameter("id", id)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch(Exception e) {
			throw new Excecoes(e, "mensagem.erroPesquisarRegistro");
		} finally {
			Conexao.fechaConexaoEM(em);
		}
	}
	
	public T find(Long id){
		EntityManager em = Conexao.getConexaoEM();
		try {
			return em.find(this.classe, id);
		} finally {
			Conexao.fechaConexaoEM(em);
		}
	}
	
	public T find(EntityManager em, T objeto, Long id) throws Excecoes{
		T obj = em.find(this.classe, id);
		
		if(obj == null) {
			throw new Excecoes("mensagem.erroJversion");
		} else {
			return obj;
		}
		
	}
	
	public void beginTransaction(EntityManager em) {
		em.getTransaction().begin();
	}

	/**
	 * @param ordem - atributos da entidade a serem ordenados
	 * @return lista contendo os objetos da entidade ordenados
	 * @throws Excecoes
	 */
	@SuppressWarnings("unchecked")
	public List<T> getTodos(String... ordem) throws Excecoes {
		StringBuilder strBuilder = new StringBuilder();
		
		int contador = 0;
		for(String str : ordem) {
			if(contador == 0){
				strBuilder.append(" order by "); 
			}
			strBuilder.append("c.").append(str);
			contador++;
			
			if(contador < ordem.length) {
				strBuilder.append(",");
			}
		}
		
		EntityManager em = Conexao.getConexaoEM();
		try {
			Query listQuery = em.createQuery("select object(c) from "+ nomeEntidade +" as c"+ strBuilder.toString() );
			return listQuery.getResultList();
		} catch(Exception e) {
			throw new Excecoes(e,"mensagem.erroPesquisarRegistro");
		} finally {
			Conexao.fechaConexaoEM(em);
		}
	}
	
	/**
	 * Método responsável por realizar a pesquisa através de um objeto populado sobrecarregado
	 * @param entidade - objeto que contenha os atributos para a pesquisa
	 * @param ordem - nome dos atributos para serem utilizados no order by da query
	 * @return lista do objeto com o retorno da pesquisa
	 * @throws Excecoes
	 */
	public List<T> getPorObjeto(T entidade, String... ordem) throws Excecoes {
		return getPorObjeto(entidade, null, ordem);
	}
	
	
	@SuppressWarnings("rawtypes")
	public List<T> getPorObjeto(T entidade, Class[] classeInterna, String... ordem) throws Excecoes {
		return getPorObjeto(entidade, classeInterna, null, ordem);
	}
	/**
	 * Método responsável por realizar a pesquisa através de um objeto populado
	 * @param entidade - objeto que contenha os atributos para a pesquisa
	 * @param classeInterna - array contendo as classes que deverão ser utilizadas no Left Join
	 * @param ordem - nome dos atributos para serem utilizados no order by da query
	 * @return lista do objeto com o retorno da pesquisa
	 * @throws Excecoes
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getPorObjeto(T entidade, Class[] classeInterna, List<Class> listaIsNull, String... ordem) throws Excecoes {
		if(entidade != null) {
			StringBuilder sql = new StringBuilder();
			String nomeEntidade = getNomeEntidade(entidade.getClass());
			sql.append("SELECT ").append(nomeEntidade).append(" FROM ").append(entidade.getClass().getCanonicalName()).append(" ").append(nomeEntidade).append(" ");
			
			if(classeInterna != null) {
				for(Class obj : classeInterna) {
					sql.append(concatenaObjetosRelacionados(entidade.getClass(), obj));
				}
			}
			
			return getPorObjeto(entidade, nomeEntidade, "", sql, 1, new HashMap<Integer, Object>(), listaIsNull, ordem);
		} else {
			return null;
		}
	}
	
	public List<T> getPorObjetoVariosNiveis(T entidade, List<JoinClasseVO> listaJoinClasseVO, String... ordem) throws Excecoes {
		return getPorObjetoVariosNiveis(entidade, listaJoinClasseVO, null, ordem);
	}
	
	/**
	 * Método responsável por realizar a pesquisa através de um objeto populado
	 * @param entidade - objeto que contenha os atributos para a pesquisa
	 * @param listaJoinClasseVO - lista contendo as classes que deverão ser utilizadas no Left Join
	 * @param ordem - nome dos atributos para serem utilizados no order by da query
	 * @return lista do objeto com o retorno da pesquisa
	 * @throws Excecoes
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getPorObjetoVariosNiveis(T entidade, List<JoinClasseVO> listaJoinClasseVO, List<Class> listaIsNull, String... ordem) throws Excecoes {
		if(entidade != null) {
			StringBuilder sql = new StringBuilder();
			String nomeEntidade = getNomeEntidade(entidade.getClass());
			sql.append("SELECT ").append(nomeEntidade).append(" FROM ").append(entidade.getClass().getCanonicalName()).append(" ").append(nomeEntidade).append(" ");
			
			if(listaJoinClasseVO != null) {
				for(JoinClasseVO vo: listaJoinClasseVO) {
					concatenaObjetosRelacionadosVariosNiveis(sql, vo, nomeEntidade, entidade.getClass());
				}
			}
			
			return getPorObjeto(entidade, nomeEntidade, "", sql, 1, new HashMap<Integer, Object>(), listaIsNull, ordem);
		} else {
			return null;
		}
	}
	
	/**
	 * Método responsável por controlar a recursividade dos atributos de entidades do objeto
	 * @param entidade - objeto que contenha os atributos para a pesquisa
	 * @param nomeEntidade - nome da entidade
	 * @param chaveEstrangeira - atributo de entidade do objeto
	 * @param sql - Query do sql
	 * @param contadorChamadaMetodo - controlador da recursividade
	 * @param mapaParametros - mapa contendo os atributos que foram preenchidos do objeto a ser pesquisado
	 * @param ordem - nome dos atributos para serem utilizados no order by da query
	 * @return lista do objeto com o retorno da pesquisa
	 * @throws Excecoes
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<T> getPorObjeto(T entidade, String nomeEntidade, String chaveEstrangeira, StringBuilder sql, Integer contadorChamadaMetodo, 
	Map<Integer, Object> mapaParametros, List<Class> listaIsNull,  String... ordem) throws Excecoes {
		
		try {
			Field atributos[] = entidade.getClass().getDeclaredFields();
			boolean existeValor = false;
			int quantidadeAtributos = atributos.length;
			for (int i = 0; i < quantidadeAtributos; i++) {
				Field atributo = atributos[i];
				atributo.setAccessible(true);
				
				if (atributo.get(entidade) instanceof HibernateProxy) {
					atributo.set(entidade, null);
				}
				
				if(atributo.get(entidade) != null && !(Modifier.isStatic(atributo.getModifiers()) && Modifier.isFinal(atributo.getModifiers())) 
						&& atributo.getAnnotation(javax.persistence.Transient.class) == null && atributo.getAnnotation(javax.persistence.Version.class) == null ) {
					
					if( atributo.getAnnotation(javax.persistence.ManyToOne.class) != null || atributo.getAnnotation(javax.persistence.OneToOne.class) != null ) {
						getPorObjeto((T) atributo.get(entidade), nomeEntidade.concat(".").concat(atributo.getName()), atributo.getName(), sql, contadorChamadaMetodo+1, mapaParametros, listaIsNull, ordem);
					}else {
						
						if((atributo.getType() == String.class && !((String)atributo.get(entidade)).isEmpty())
								|| (atributo.getType() == Long.class && ((Long)atributo.get(entidade)).intValue() > 0)
								|| (atributo.getType() == Integer.class && ((Integer)atributo.get(entidade)).intValue() > 0)
								|| (atributo.getAnnotation(javax.persistence.Enumerated.class) != null  && atributo.get(entidade) != null)) {
							
							sql.append(sql.indexOf("WHERE") >=0 ? "AND " : "WHERE ");
							mapaParametros.put(mapaParametros.size()+1, atributo.get(entidade));
							
							if(atributo.getType() == String.class) {
								sql.append("LOWER(").append(nomeEntidade).append(".").append(atributo.getName()).append(") LIKE LOWER('%' || ?")
								.append(mapaParametros.size()).append(" || '%') ");
							}else {
								sql.append(nomeEntidade).append(".").append(atributo.getName()).append(" = ?").append(mapaParametros.size()).append(" ");
							}
							
							existeValor = true;
						}
					}
				}
				
				if(i == quantidadeAtributos-1 && contadorChamadaMetodo > 1 && !existeValor) {
					
					if(listaIsNull != null){
						for(Class c : listaIsNull){
							if(entidade.getClass() == c){
								sql.append(sql.indexOf("WHERE") >=0 ? "AND " : "WHERE ");
								
								sql.append(nomeEntidade.concat(" IS NULL "));
								
								break;
							}
						}
					} else {
						sql.append(sql.indexOf("WHERE") >=0 ? "AND " : "WHERE ");
						
						sql.append(nomeEntidade.concat(" IS NULL "));
					}
					
					
				}
				
			}
			
			if(contadorChamadaMetodo == 1) {
				if(ordem.length > 0) {
					sql.append("ORDER BY ");
					for(int i = 0 ; i < ordem.length ; i++) {
						sql.append(nomeEntidade).append(".").append(ordem[i]);
						
						if(i < ordem.length - 1) {
							sql.append(", ");
						}
					}
				}
				
				EntityManager em = Conexao.getConexaoEM();
				Query query = em.createQuery(sql.toString());

				for(Entry<Integer, Object> entry : mapaParametros.entrySet()) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
				
				List<T> listaRetorno = query.getResultList();
					
				Conexao.fechaConexaoEM(em);
				
				return listaRetorno;
			}
			
			return null;
		} catch (Exception e) {
			throw new Excecoes(e, "mensagem.erroPesquisarRegistro");
		}
	}
	
	/**
	 * Método responsável por formatar o nome de entidade para ser utilizado na query de busca
	 * @param entidade - a classe da entidade
	 * @return String contendo o nome da entidade formatada para a pesquisa
	 */
	@SuppressWarnings("rawtypes")
	private String getNomeEntidade(Class entidade) {
		return String.valueOf(entidade.getSimpleName().charAt(0)).toLowerCase().concat(entidade.getSimpleName().substring(1));
	}
	
	/**
	 * @param classe - classe do objeto
	 * @param classeInterna - classe dos atributos de entidade do objeto
	 * @return String contendo a instrução de left join para a query
	 */
	@SuppressWarnings("rawtypes")
	public String concatenaObjetosRelacionados(Class classe, Class... classeInterna) {
		StringBuilder builder = new StringBuilder();
		String nomeEntidade = getNomeEntidade(classe);
		
		Field atributos[] = classe.getDeclaredFields();
		for(Class clazz: classeInterna) {
			for (int i = 0; i < atributos.length; i++) {
				Field atributo = atributos[i];
				if(atributo.getType() == clazz) {
					builder.append("LEFT JOIN FETCH ").append(nomeEntidade).append(".").append(atributo.getName()).append(" ");
				}
			}
		}
		
		return builder.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private StringBuilder concatenaObjetosRelacionadosVariosNiveis(StringBuilder builder, JoinClasseVO joinClasseVO, String atributoPai, Class classe){
		
		Field atributosEntidadePai[] = classe.getDeclaredFields();
		
		String atributoPaiAuxiliar = atributoPai+"";
		for (int i = 0; i < atributosEntidadePai.length; i++) {
			atributoPai = atributoPaiAuxiliar+"";
			Field atributoEntidadePai = atributosEntidadePai[i];
			if(atributoEntidadePai.getType() == joinClasseVO.getPai()) {
				
				builder.append(" LEFT JOIN FETCH ").append(atributoPai).append(".").append(atributoEntidadePai.getName()).append(" AS ").append(atributoPai+atributoEntidadePai.getName()).append(" ");
				
				atributoPai += atributoEntidadePai.getName();
				
				if(joinClasseVO.getListafilho() != null){
					for(JoinClasseVO vo: joinClasseVO.getListafilho()) {
						concatenaObjetosRelacionadosVariosNiveis(builder, vo, atributoPai, joinClasseVO.getPai());
					}
				}
			}
		}
		
		return builder;
	}

	protected void setValorObjeto(T objeto, String nomeDoCampo, Object valor) {
		try {
			Field atributos[] = objeto.getClass().getDeclaredFields();
			
			for (int i = 0; i < atributos.length; i++) {
				Field atributo = atributos[i];
				
				if(atributo.getName().equalsIgnoreCase(nomeDoCampo)) {
					if(!atributo.isAccessible()) {
						atributo.setAccessible(true);
					}
					
					if(valor != null)
						atributo.set(objeto, valor);
					else atributo.set(objeto, null);
					
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private Object retornaValorObjeto(T objeto, String nomeDoCampo) throws IllegalAccessException {
		Field atributos[] = objeto.getClass().getDeclaredFields();
		
		for (int i = 0; i < atributos.length; i++) {
			Field atributo = atributos[i];
			
			if(atributo.getName().equalsIgnoreCase(nomeDoCampo)) {
				if(!atributo.isAccessible()) {
					atributo.setAccessible(true);
				}
			
				return atributo.get(objeto);
			}
		}
		
		return null;
	}
}
