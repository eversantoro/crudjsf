package br.com.eversantoro.crudjsf.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.SerializationUtils;

import br.com.eversantoro.crudjsf.entidade.Endereco;
import br.com.eversantoro.crudjsf.entidade.Pessoa;
import br.com.eversantoro.crudjsf.exception.Excecoes;
import br.com.eversantoro.crudjsf.persistencia.EnderecoDao;
import br.com.eversantoro.crudjsf.persistencia.PessoaDao;
import br.com.eversantoro.crudjsf.util.ComboUtil;
import br.com.eversantoro.crudjsf.util.Mensagem;



@ManagedBean(name = "pessoaMB")
@ViewScoped
public class PessoaMB implements Serializable {
	private static final long serialVersionUID = 3771278294366057068L;

	private PessoaDao pessoaDao;
	private EnderecoDao enderecoDao;

	private Pessoa pessoa;
	private Pessoa pessoaUltimaPesquisa;
	private List<Pessoa> listaPessoa;
	private List<SelectItem> comboSexo;
	private List<SelectItem> comboEstado;
	private Endereco endereco;

	@PostConstruct
	public void metodoInicial() {
		pessoa = new Pessoa();
		getPessoa().setListaEndereco(new ArrayList<Endereco>());
		endereco = new Endereco();
		setComboSexo(ComboUtil.comboSexo());
		setComboEstado(ComboUtil.comboEstados());
		try {
			setListaPessoa(getPessoaDao().getTodos("nome"));
		} catch (Excecoes e) {
			e.printStackTrace();
		}
	}

	public void salvar() {
		try {	getPessoaDao().salvar(getPessoa());
				cancelar();
				Mensagem.informacao("mensagem.sucessoSalvarRegistro");
				pesquisar(getPessoaUltimaPesquisa());
		} catch (Excecoes e) {
			Mensagem.erro(e.getMessage());
		}
	}

	public void excluir() {
		try {
			getPessoaDao().excluir(getPessoa().getId());
			cancelar();
			Mensagem.informacao("mensagem.sucessoExcluirRegistro");
			pesquisar(getPessoaUltimaPesquisa());
		} catch (Excecoes e) {
			Mensagem.erro(e.getMessage());
		}
	} 

	public void alterar() {
			try {
				getEndereco().setPessoa(pessoa);
				getPessoaDao().alterar(getPessoa());
				getPessoa().setListaEndereco(getEnderecoDao().getListaEndereco(pessoa));
				cancelar();
				Mensagem.informacao("mensagem.sucessoAlterarRegistro");
				pesquisar(getPessoaUltimaPesquisa());
			} catch (Excecoes e) {
				Mensagem.erro(e.getMessage());
			}
	}

	public void salvarEndereco() {
		try {	
				getEndereco().setPessoa(pessoa);
				getEnderecoDao().salvar(getEndereco());
				getPessoa().setListaEndereco(getEnderecoDao().getListaEndereco(pessoa));
				Mensagem.informacao("mensagem.sucessoSalvarRegistro");
				setEndereco(new Endereco());
		} catch (Excecoes e) {
			Mensagem.erro(e.getMessage());
		}
	}

	public void excluirEndereco() {
		try {
			getEnderecoDao().excluir(getEndereco().getId());
			getPessoa().setListaEndereco(getEnderecoDao().getListaEndereco(pessoa));
			Mensagem.informacao("mensagem.sucessoExcluirRegistro");
			setEndereco(new Endereco());
		} catch (Excecoes e) {
			Mensagem.erro(e.getMessage());
		}
	} 

	public void alterarEndereco() {
			try {
				getEndereco().setPessoa(pessoa);
				getEnderecoDao().alterar(getEndereco());
				getPessoa().setListaEndereco(getEnderecoDao().getListaEndereco(pessoa));
				Mensagem.informacao("mensagem.sucessoAlterarRegistro");
				setEndereco(new Endereco());
			} catch (Excecoes e) {
				Mensagem.erro(e.getMessage());
			}
	}
	
	public void abrirDialogEndereco() {
		setEndereco(new Endereco());
	}
	
	
	public void pesquisar() {
		setPessoaUltimaPesquisa((Pessoa) SerializationUtils.clone(getPessoa()));
		try {
			pesquisar(getPessoaUltimaPesquisa());
		} catch (Excecoes e) {
			Mensagem.erro(e.getMessage());
		}
	}

	private void pesquisar(Pessoa pessoa) throws Excecoes {
		setListaPessoa(getPessoaDao().getPorObjeto(pessoa, "nome"));
	}

	public void cancelar() {
		metodoInicial();
	}



	// getters e setters
	public Pessoa getPessoa() {
		if (pessoa == null) {
			this.pessoa = new Pessoa();
		}
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaPessoa() {
		if (listaPessoa == null) {
			listaPessoa = new ArrayList<Pessoa>();
		}
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public Pessoa getPessoaUltimaPesquisa() {
		if (pessoaUltimaPesquisa == null) {
			pessoaUltimaPesquisa = new Pessoa();
		}
		return pessoaUltimaPesquisa;
	}

	public void setPessoaUltimaPesquisa(Pessoa pessoaUltimaPesquisa) {
		this.pessoaUltimaPesquisa = pessoaUltimaPesquisa;
	}

	private PessoaDao getPessoaDao() {
		if (pessoaDao == null) {
			pessoaDao = new PessoaDao();
		}

		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}
	
	private EnderecoDao getEnderecoDao() {
		if (enderecoDao == null) {
			enderecoDao = new EnderecoDao();
		}

		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public void clonarObjetoSelecionado() {
		setPessoa((Pessoa) SerializationUtils.clone(getPessoa()));
	}

	public void clonarObjetoSelecionadoEndereco() {
		setEndereco((Endereco) SerializationUtils.clone(getEndereco()));
	}

	
	public List<SelectItem> getComboSexo() {
		return comboSexo;
	}

	public void setComboSexo(List<SelectItem> comboSexo) {
		this.comboSexo = comboSexo;
	}

	public Endereco getEndereco() {
		if (endereco == null)
			endereco = new Endereco();
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<SelectItem> getComboEstado() {
		return comboEstado;
	}

	public void setComboEstado(List<SelectItem> comboEstado) {
		this.comboEstado = comboEstado;
	}

	
	
	
}
