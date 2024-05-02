package br.com.eversantoro.crudjsf.persistencia;

import br.com.eversantoro.crudjsf.bancodedados.Dao;
import br.com.eversantoro.crudjsf.entidade.Pessoa;


public class PessoaDao extends Dao<Pessoa> {

	private static final long serialVersionUID = 6531539203097794341L;

	public PessoaDao() {
		super(Pessoa.class);
	}
}