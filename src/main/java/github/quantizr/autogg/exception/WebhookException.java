package github.quantizr.autogg.exception;

public class WebhookException extends WebhookExce{

	public WebhookException() {
		super("Your webhook has encountered an error. Is it invalid?");
	}

}
