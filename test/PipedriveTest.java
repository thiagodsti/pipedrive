import org.junit.Assert;
import org.junit.Test;

import controllers.PipedriveController;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.WithApplication;

public class PipedriveTest extends WithApplication {

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().configure("play.http.router", "/conf/routes").build();
	}

	@Test
	public void adicionarNovaAtividadeComSucessoTest() {
		Result result = new PipedriveController().adicionarNovaAtividade();
		Assert.assertEquals(Status.CREATED, result.status());
	}

}
