package controllers;

import javax.inject.Inject;

import play.libs.ws.WSClient;
import play.mvc.Controller;

public class WebServiceController extends Controller {

	@Inject
	protected WSClient ws;

}
