package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade;
import entities.Atividade.AtividadeTipoEnum;
import play.Logger.ALogger;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import services.PipedriveServiceImpl;

public class PipedriveController extends Controller {

	ALogger LOGGER = play.Logger.of(PipedriveController.class);

	@Inject
	private PipedriveServiceImpl pipedriveService;

	public Result adicionarNovaAtividade() {
		JsonNode formulario = request().body().asJson();
		Atividade atividade = obterAtividadePeloFormulario(formulario);
		WSResponse wsResponse;
		try {
			wsResponse = pipedriveService.adicionarNovaAtividade(atividade);
		} catch (Exception e) {
			LOGGER.error(String.format("Houve erro ao adicionar atividade: %s", e.getCause()));
			return internalServerError(e.getMessage());
		}
		if (wsResponse.getStatus() == 201) {
			return created(wsResponse.getBodyAsStream());
		} else {
			return badRequest(wsResponse.getBodyAsStream());
		}
	}

	public Result obterDetalhesUmaAtividade(Long codigoAtividade) {
		WSResponse wsResponse = null;
		try {
			wsResponse = pipedriveService.obterDetalhesUmaAtividade(codigoAtividade);
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError(e.getMessage());
		}
		if (wsResponse == null) {
			return notFound("Não foi possível encontrar atividade");
		}
		return ok(wsResponse.getBodyAsStream());
	}

	public Result editarAtividade(Long codigoAtividade) {
		JsonNode formulario = request().body().asJson();
		Atividade atividade = obterAtividadePeloFormulario(formulario);
		WSResponse wsResponse;
		try {
			wsResponse = pipedriveService.editarAtividade(atividade, codigoAtividade);
		} catch (Exception e) {
			LOGGER.error(String.format("Houve erro ao editar atividade %s: %s", codigoAtividade, e.getCause()));
			return internalServerError(e.getMessage());
		}
		if (wsResponse.getStatus() == 201) {
			return created(wsResponse.getBodyAsStream());
		} else {
			return badRequest(wsResponse.getBodyAsStream());
		}
	}

	public Result deletarAtividade(Long codigoAtividade) {
		WSResponse wsResponse = null;
		try {
			wsResponse = pipedriveService.deletarAtividade(codigoAtividade);
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError(e.getMessage());
		}
		if (wsResponse == null) {
			return notFound("Não foi possível deletar atividade");
		}
		return ok(wsResponse.getBodyAsStream());
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
		atividade.setIdNegocio(parseJsonNodeToLong(formulario.get("idNegocio")));
		atividade.setIdOrganizacao(parseJsonNodeToLong(formulario.get("idOrganizacao")));
		atividade.setIdPessoa(parseJsonNodeToLong(formulario.get("idPessoa")));
		atividade.setIdUsuario(parseJsonNodeToLong(formulario.get("idUsuario")));
		atividade.setObservacao(parseJsonNodeToString(formulario.get("observacao")));
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
			LOGGER.error(String.format("Ocorreu um erro ao fazer o parse da data %s. Error: %s", data, e));
		}
		return date;
	}

	private Date obterData(String data) {
		return this.obterData(data, "dd/MM/yyyy");
	}

}
