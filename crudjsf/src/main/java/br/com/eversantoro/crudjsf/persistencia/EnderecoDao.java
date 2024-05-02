package br.com.eversantoro.crudjsf.persistencia;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.eversantoro.crudjsf.bancodedados.Conexao;
import br.com.eversantoro.crudjsf.bancodedados.Dao;
import br.com.eversantoro.crudjsf.entidade.Endereco;
import br.com.eversantoro.crudjsf.entidade.Pessoa;
import br.com.eversantoro.crudjsf.exception.Excecoes;


public class EnderecoDao extends Dao<Endereco> {

	private static final long serialVersionUID = 6531539203097794341L;

	public EnderecoDao() {
		super(Endereco.class);
	}
	
	public List<Endereco> getListaEndereco(Pessoa p) throws Excecoes {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e FROM Endereco e")
		  .append(" WHERE e.pessoa.id = :idPessoa");
		
		EntityManager em = Conexao.getConexaoEM();
		try {
			return em.createQuery(sql.toString())
					.setParameter("idPessoa", p.getId())
					.getResultList();
		} catch(Exception e) {
			throw new Excecoes(e, "mensagem.erroListaEndereco");
		} finally {
			Conexao.fechaConexaoEM(em);
		}
	}
	
}