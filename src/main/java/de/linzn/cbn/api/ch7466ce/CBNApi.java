package de.linzn.cbn.api.ch7466ce;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class CBNApi {

	private String cbnIP;
	private String cbnUsername;
	private String cbnPassword;
	private WebClient client;

	public CBNApi(String cbnIP, String cbnUsername, String cbnPassword) {
		this.cbnIP = cbnIP;
		this.cbnUsername = cbnUsername;
		this.cbnPassword = cbnPassword;

	}

	// Public API

	public void restartCBN() {
		this.client = init_web_client();
		this.login_cbn_webinterface(this.cbnIP, this.cbnUsername, this.cbnPassword);
		this.restart_cbn_webinterface(this.cbnIP);
		this.client.close();
	}

	// Private functions

	public boolean restart_cbn_webinterface(String cbnIP) {
		try {
			System.out.println("Send restart signal to CBN-Modem...Take some while!");
			this.client.waitForBackgroundJavaScript(4000);
			this.client.getPage("http://" + cbnIP + "/common_page/reboot.html");
			this.client.waitForBackgroundJavaScript(4000);
			System.out.println("Signal send finish!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No CBN Page found!");
			return false;
		}
		return true;
	}

	private boolean login_cbn_webinterface(String cbnIP, String cbnUsername, String cbnPassword) {
		try {
			System.out.println("Open login to CBN-Modem...Take some while!");
			HtmlPage loginPage = this.client.getPage("http://" + cbnIP + "/common_page/login.html");
			this.client.waitForBackgroundJavaScript(500);
			HtmlForm form = loginPage.getHtmlElementById("form-login");
			HtmlTextInput userField = form.getInputByName("loginUsername");
			userField.setValueAttribute(cbnUsername);
			HtmlPasswordInput passField = form.getInputByName("loginPassword");
			passField.setValueAttribute(cbnPassword);
			loginPage = form.getInputByValue("Login").click();
			System.out.println("Logging in to CBN-Modem...Take some while!");
			this.client.waitForBackgroundJavaScript(2000);
			System.out.println("Login finish!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No CBN Page found!");
			return false;
		}
		return true;
	}

	private WebClient init_web_client() {
		System.out.println("Create connection to CBN-Modem...Take some while!");
		// Create web client
		WebClient wClient = new WebClient(BrowserVersion.CHROME);
		wClient.getOptions().setCssEnabled(false);
		wClient.getOptions().setUseInsecureSSL(true);
		wClient.getOptions().setThrowExceptionOnScriptError(false);
		wClient.getCookieManager().setCookiesEnabled(true);
		wClient.setAjaxController(new NicelyResynchronizingAjaxController());
		wClient.getOptions().setTimeout(1000);

		// Disable error logging
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies")
				.setLevel(Level.OFF);
		return wClient;
	}

}
