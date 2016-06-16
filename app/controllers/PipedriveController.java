package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade;
import entities.AtividadeParse;
import play.Logger.ALogger;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Http.Status;
import play.mvc.Result;
import services.PipedriveServiceImpl;

public class PipedriveController extends Controller {

	ALogger LOGGER = play.Logger.of(PipedriveController.class);

	@Inject
	private PipedriveServiceImpl pipedriveService;
	@Inject
	private AtividadeParse atividadeParse;

	public Result adicionarNovaAtividade() {
		JsonNode formulario = request().body().asJson();
		Atividade atividade = atividadeParse.parse(formulario);
		WSResponse wsResponse;
		try {
			wsResponse = pipedriveService.adicionarNovaAtividade(atividade);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return badRequest(e.getMessage());
		}
		if (wsResponse.getStatus() == 201) {
			return created().withHeader("id", wsResponse.asJson().get("data").get("id").asText());
		} else {
			LOGGER.error(String.format("[ERRO DESCONHECIDO]: %s", wsResponse.asJson()));
			return badRequest(String.format("Erro: %s", wsResponse.asJson()));
		}
	}

	public Result obterDetalhesUmaAtividade(Long codigoAtividade) {
		WSResponse wsResponse = null;
		try {
			wsResponse = pipedriveService.obterDetalhesUmaAtividade(codigoAtividade);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return badRequest(e.getMessage());
		}
		if (wsResponse == null || wsResponse.getStatus() == Status.NOT_FOUND) {
			return notFound(String.format("Não foi possível encontrar atividade com o código %s", codigoAtividade));
		}
		return ok(wsResponse.asJson());
	}

	public Result editarAtividade(Long codigoAtividade) {
		JsonNode formulario = request().body().asJson();
		Atividade atividade = atividadeParse.parse(formulario);
		WSResponse wsResponse;
		try {
			wsResponse = pipedriveService.editarAtividade(atividade, codigoAtividade);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return internalServerError(e.getMessage());
		}
		if (wsResponse.getStatus() == 201) {
			return created().withHeader("id", wsResponse.asJson().get("data").get("id").asText());
		} else {
			return badRequest(wsResponse.asJson());
		}
	}

	public Result deletarAtividade(Long codigoAtividade) {
		WSResponse wsResponse = null;
		try {
			wsResponse = pipedriveService.deletarAtividade(codigoAtividade);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return badRequest(e.getMessage());
		}
		if (wsResponse == null) {
			return notFound(String.format("Não foi possível deletar atividade com código %s", codigoAtividade));
		}
		return ok(wsResponse.asJson());
	}

}
