package br.com.eversantoro.crudjsf.entidade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Ever Santoro
 * Classe responsavel pelo mapeamento objeto relacional da pessoa
 */
@Entity
@Table(name="pessoa")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 3631328751651244663L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "id")
	    private Long id;

	    @Column(name = "nome")
		@NotNull
		@Size(min=1, max=150)	    
	    private String nome;

	    @Column(name = "idade")
		@NotNull	    
	    @Temporal(TemporalType.DATE)
	    private Date idade;

	    @Column(name = "sexo")
		@NotNull
		@Size(min=1, max=2)
	    private String sexo;

	    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy="pessoa")
	    private List<Endereco> listaEndereco;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public Date getIdade() {
			return idade;
		}

		public void setIdade(Date idade) {
			this.idade = idade;
		}

		public String getSexo() {
			return sexo;
		}

		public void setSexo(String sexo) {
			this.sexo = sexo;
		}

		public List<Endereco> getListaEndereco() {
			return listaEndereco;
		}

		public void setListaEndereco(List<Endereco> listaEndereco) {
			this.listaEndereco = listaEndereco;
		}

		@Override
		public String toString() {
			return "Pessoa [id=" + id + ", nome=" + nome + ", idade=" + idade + ", sexo=" + sexo + ", listaEndereco="
					+ listaEndereco + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pessoa other = (Pessoa) obj;
			return id == other.id;
		}
	    
	    
	
}
