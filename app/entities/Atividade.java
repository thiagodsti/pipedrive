package entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Atividade {

	@JsonProperty("subject")
	public String assunto;
	@JsonProperty("done")
	public boolean feito;
	@JsonProperty("type")
	public AtividadeTipoEnum tipoAtividade;
	@JsonProperty("due_date")
	public Date data;
	@JsonProperty("due_time")
	public Date dataHora;
	@JsonProperty("duration")
	public Date duracao;
	@JsonProperty("user_id")
	public Long idUsuario;
	@JsonProperty("deal_id")
	public Long idNegocio;
	@JsonProperty("person_id")
	public Long idPessoa;
	@JsonProperty("org_id")
	public Long idOrganizacao;
	@JsonProperty("note")
	public String observacao;

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public boolean isFeito() {
		return feito;
	}

	public void setFeito(boolean feito) {
		this.feito = feito;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Date getDuracao() {
		return duracao;
	}

	public void setDuracao(Date duracao) {
		this.duracao = duracao;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdNegocio() {
		return idNegocio;
	}

	public void setIdNegocio(Long idNegocio) {
		this.idNegocio = idNegocio;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Long getIdOrganizacao() {
		return idOrganizacao;
	}

	public void setIdOrganizacao(Long idOrganizacao) {
		this.idOrganizacao = idOrganizacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public AtividadeTipoEnum getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(AtividadeTipoEnum tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public enum AtividadeTipoEnum {
		LIGAR("call"), REUNIAO("meeting"), TAREFA("task"), PRAZO("deadline"), EMAIL("email"), ALMOCO("lunch");

		private String chaveAtividadeTipo;

		@JsonValue
		public String getChaveAtividadeTipo() {
			return chaveAtividadeTipo;
		}

		private AtividadeTipoEnum(String chaveAtividadeTipo) {
			this.chaveAtividadeTipo = chaveAtividadeTipo;
		}
	}

}
