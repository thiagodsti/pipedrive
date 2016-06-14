package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletionStage;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade;
import entities.Atividade.AtividadeTipoEnum;
import play.Logger.ALogger;
import play.libs.Json;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Result;

public class PipedriveController extends WebServiceController {

	ALogger LOGGER = play.Logger.of(PipedriveController.class);

	public Result adicionarNovaAtividade() {
		JsonNode formulario = request().body().asJson();
		Atividade atividade = obterAtividadePeloFormulario(formulario);
		WSResponse wsResponse;
		try {
			wsResponse = this.adicionarNovaAtividade(atividade);
			System.out.println(wsResponse);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(String.format("Houve erro ao adicionar atividade: %s", e.getCause()));
			return internalServerError(e.getMessage());
		}
		if (wsResponse.getStatus() == 201) {
			return created(wsResponse.getBodyAsStream());
		} else {
			return badRequest(wsResponse.getBodyAsStream());
		}
	}

	public WSResponse adicionarNovaAtividade(Atividade atividade) throws Exception {
		if (atividade == null) {
			throw new Exception("Atividade está nula");
		}

		this.validarAtividade(atividade);
		JsonNode jsonNode = Json.toJson(atividade);

		String url = "https://api.pipedrive.com/v1/activities";
		WSRequest request = ws.url(url).setQueryParameter("api_token", "539e9bff4e3cc6b4a0ecf984f8d9f80039dd5667");
		request.setContentType("application/json");
		CompletionStage<WSResponse> responsePromise = request.post(jsonNode);
		WSResponse wsResponse = responsePromise.toCompletableFuture().get();
		return wsResponse;
	}

	private void validarAtividade(Atividade atividade) throws Exception {
		validarTipoAtividade(atividade.getTipoAtividade());
		validarNegocio(atividade.getIdNegocio());
		validarAssunto(atividade.getAssunto());
	}

	private void validarAssunto(String assunto) throws Exception {
		if (StringUtils.isEmpty(assunto)) {
			throw new Exception("Assunto deve ser preenchido");
		}
	}

	private void validarNegocio(Long idNegocio) throws Exception {
		if (idNegocio == null) {
			throw new Exception("Atividade deve estar atrelada a algum Negócio.");
		}
	}

	private void validarTipoAtividade(AtividadeTipoEnum tipoAtividade) throws Exception {
		if (tipoAtividade == null) {
			throw new Exception("Atividade deve possuir um tipo.");
		}
	}

	private Atividade obterAtividadePeloFormulario(JsonNode formulario) {
		if (formulario == null) {
			return null;
		}

		Atividade atividade = new Atividade();
		atividade.setAssunto(parseJsonNodeToString(formulario.get("assunto")));
		atividade.setTipoAtividade(obterTipoAtividade(parseJsonNodeToString(formulario.get("tipoAtividade"))));
		atividade.setData(obterData(parseJsonNodeToString(formulario.get("data"))));
		atividade.setDataHora(obterData(parseJsonNodeToString(formulario.get("dataHora")), "HH:mm"));
		atividade.setDuracao(obterData(parseJsonNodeToString(formulario.get("duracao"))));
		atividade.setFeito(formulario.get("feito").asBoolean());
		atividade.setIdNegocio(this.parseStringToLong(parseJsonNodeToString(formulario.get("idNegocio"))));
		atividade.setIdOrganizacao(this.parseStringToLong(parseJsonNodeToString(formulario.get("idOrganizacao"))));
		atividade.setIdPessoa(this.parseStringToLong(parseJsonNodeToString(formulario.get("idPessoa"))));
		atividade.setIdUsuario(this.parseStringToLong(parseJsonNodeToString(formulario.get("idUsuario"))));
		atividade.setObservacao(parseJsonNodeToString(formulario.get("observacao")));
		return atividade;
	}

	private String parseJsonNodeToString(JsonNode node) {
		if (node == null) {
			return null;
		}
		return node.asText();
	}

	private Long parseStringToLong(String codigo) {
		if (StringUtils.isEmpty(codigo)) {
			return null;
		}

		return Long.parseLong(codigo);
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
			LOGGER.error(String.format("Ocorreu um erro ao fazer o parse da data %s. Error: %s", data, e));
		}
		return date;
	}

	private Date obterData(String data) {
		return this.obterData(data, "dd/MM/yyyy");
	}

}
