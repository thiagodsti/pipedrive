package infra;

import com.fasterxml.jackson.databind.JsonNode;

import entities.Atividade;

public interface ParseJson<T> {

	Atividade parse(JsonNode json);

}
