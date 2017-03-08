package de.linzn.cbn.ch7466ce;

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

	public CBNApi(String cbnIP, String cbnUsername, String cbnPassword) {
		this.cbnIP = cbnIP;
		this.cbnUsername = cbnUsername;
		this.cbnPassword = cbnPassword;

	}

	// Public API

	public void restartCBN() {
		WebClient client = this.login_cbn_webinterface(this.cbnIP, this.cbnUsername, this.cbnPassword);
		boolean status = this.restart_cbn_webinterface(client, this.cbnIP);
		client.close();
	}

	// Private functions

	public boolean restart_cbn_webinterface(WebClient webClient, String cbnIP) {
		try {
			System.out.println("Send restart signal to CBN-Modem...Take some while!");
			webClient.waitForBackgroundJavaScript(4000);
			HtmlPage restartPage = (HtmlPage) webClient.getPage("http://" + cbnIP + "/common_page/reboot.html");
			webClient.waitForBackgroundJavaScript(4000);
			
			//System.out.println(restartPage.asText());
			webClient.waitForBackgroundJavaScript(4000);
			webClient.getOptions().setTimeout(4000);
			System.out.println("Signal send finish!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No CBN Page found!");
			return false;
		}
		return true;
	}

	private WebClient login_cbn_webinterface(String cbnIP, String cbnUsername, String cbnPassword) {
		WebClient webClient = null;
		try {
			// disable error logging
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
			java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
			java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);
			
			System.out.println("Create connection to CBN-Modem...Take some while!");
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getCookieManager().setCookiesEnabled(true);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setTimeout(1000);
			System.out.println("Open login to CBN-Modem...Take some while!");
			HtmlPage loginPage = (HtmlPage) webClient.getPage("http://" + cbnIP + "/common_page/login.html");
			webClient.waitForBackgroundJavaScript(500);
			HtmlForm form = loginPage.getHtmlElementById("form-login");
			HtmlTextInput userField = form.getInputByName("loginUsername");
			userField.setValueAttribute(cbnUsername);
			HtmlPasswordInput passField = form.getInputByName("loginPassword");
			passField.setValueAttribute(cbnPassword);
			loginPage = form.getInputByValue("Login").click();
			System.out.println("Logging in to CBN-Modem...Take some while!");
			webClient.waitForBackgroundJavaScript(2000);
			//System.out.println(loginPage.asText());
			webClient.getOptions().setTimeout(1000);
			System.out.println("Login finish!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No CBN Page found!");
		}
		return webClient;
	}

}
