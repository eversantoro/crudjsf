package br.com.eversantoro.crudjsf.objeto;

import java.util.List;

public class JoinClasseVO {

	@SuppressWarnings("rawtypes")
	private Class pai;
	private List<JoinClasseVO> listafilho;
	
	public JoinClasseVO(){}
	
	@SuppressWarnings("rawtypes")
	public JoinClasseVO(Class pai, List<JoinClasseVO> listafilho) {
		this.pai = pai;
		this.listafilho = listafilho;
	}
	
	@SuppressWarnings("rawtypes")
	public Class getPai() {
		return pai;
	}
	
	@SuppressWarnings("rawtypes")
	public void setPai(Class pai) {
		this.pai = pai;
	}
	
	public List<JoinClasseVO> getListafilho() {
		return listafilho;
	}

	public void setListafilho(List<JoinClasseVO> listafilho) {
		this.listafilho = listafilho;
	}
	
}
