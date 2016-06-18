package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Atividade;
import entities.AtividadeParse;
import play.Logger.ALogger;
import play.libs.Json;
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
			return badRequest(this.obterErro(e.getMessage()));
		}
		if (wsResponse.getStatus() == Status.CREATED) {
			return created().withHeader("id", wsResponse.asJson().get("data").get("id").asText());
		} else {
			LOGGER.error(String.format("[ERRO DESCONHECIDO]: %s", wsResponse.asJson()));
			return badRequest(this.obterErro(wsResponse.asJson().toString()));
		}
	}

	public Result obterDetalhesUmaAtividade(Long codigoAtividade) {
		WSResponse wsResponse = null;
		try {
			wsResponse = pipedriveService.obterDetalhesUmaAtividade(codigoAtividade);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return badRequest(this.obterErro(e.getMessage()));
		}
		if (wsResponse == null || wsResponse.getStatus() == Status.NOT_FOUND) {
			return notFound(this
					.obterErro(String.format("Não foi possível encontrar atividade com o código %s", codigoAtividade)));
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
			return internalServerError(this.obterErro(e.getMessage()));
		}
		if (wsResponse.getStatus() == Status.OK || wsResponse.getStatus() == Status.CREATED) {
			return created().withHeader("id", wsResponse.asJson().get("data").get("id").asText());
		} else {
			return badRequest(this.obterErro(wsResponse.asJson().toString()));
		}
	}

	public Result deletarAtividade(Long codigoAtividade) {
		WSResponse wsResponse = null;
		try {
			wsResponse = pipedriveService.deletarAtividade(codigoAtividade);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return badRequest(this.obterErro(e.getMessage()));
		}
		if (wsResponse.getStatus() == Status.OK) {
			return ok(wsResponse.asJson());
		}

		if (wsResponse.getStatus() == Status.GONE) {
			return notFound(
					obterErro(String.format("Não foi possível encontrar a atividade de código %s", codigoAtividade)));
		}
		return badRequest(obterErro(wsResponse.asJson().toString()));
	}

	private ObjectNode obterErro(String data) {
		return Json.newObject().put("success", false).put("error", data);
	}

}
