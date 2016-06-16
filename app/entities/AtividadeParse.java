package entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade.AtividadeTipoEnum;
import infra.ParseJson;

public class AtividadeParse implements ParseJson<Atividade> {

	@Override
	public Atividade parse(JsonNode json) {
		if (json == null) {
			return null;
		}

		Atividade atividade = new Atividade();
		atividade.setAssunto(parseJsonNodeToString(json.get("assunto")));
		atividade.setTipoAtividade(obterTipoAtividade(parseJsonNodeToString(json.get("tipoAtividade"))));
		atividade.setData(obterData(parseJsonNodeToString(json.get("data"))));
		atividade.setDataHora(obterData(parseJsonNodeToString(json.get("dataHora")), "HH:mm"));
		atividade.setDuracao(obterData(parseJsonNodeToString(json.get("duracao"))));
		atividade.setFeito(json.get("feito").asBoolean());
		atividade.setIdNegocio(parseJsonNodeToLong(json.get("idNegocio")));
		atividade.setIdOrganizacao(parseJsonNodeToLong(json.get("idOrganizacao")));
		atividade.setIdPessoa(parseJsonNodeToLong(json.get("idPessoa")));
		atividade.setIdUsuario(parseJsonNodeToLong(json.get("idUsuario")));
		atividade.setObservacao(parseJsonNodeToString(json.get("observacao")));
		return atividade;
	}

	private Long parseJsonNodeToLong(JsonNode node) {
		if (node == null) {
			return null;
		}

		return node.asLong();
	}

	private String parseJsonNodeToString(JsonNode node) {
		if (node == null) {
			return null;
		}
		return node.asText();
	}

	private AtividadeTipoEnum obterTipoAtividade(String tipoAtividade) {
		AtividadeTipoEnum atividadeTipo = null;
		if (StringUtils.isNotEmpty(tipoAtividade)) {
			tipoAtividade = tipoAtividade.toUpperCase().trim();
			atividadeTipo = AtividadeTipoEnum.valueOf(tipoAtividade);
		}
		return atividadeTipo;
	}

	private Date obterData(String data, String format) {
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			date = simpleDateFormat.parse(data);
		} catch (ParseException e) {
			// fazer nada por enquanto;
		}
		return date;
	}

	private Date obterData(String data) {
		return this.obterData(data, "dd/MM/yyyy");
	}

}
