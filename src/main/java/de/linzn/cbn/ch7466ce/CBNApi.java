package de.linzn.cbn.ch7466ce;

import java.io.IOException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class CBNApi {
	

	// Public API
	
	
	public void restartCBN(String cbnIP, String cbnUsername, String cbnPassword){
		WebClient client = this.login_cbn_webinterface(cbnIP, cbnUsername, cbnPassword);
		boolean status = this.restart_cbn_webinterface(client, cbnIP);
		client.close();
		if (status){
			System.out.println("Restart Succesfuly!");
		}
	}
	
	

	
	// Private functions
	
	
	public boolean restart_cbn_webinterface(WebClient webClient, String cbnIP){
		try {
		webClient.waitForBackgroundJavaScript(4000);
		HtmlPage restartPage = (HtmlPage)webClient.getPage("http://" + cbnIP + "/common_page/reboot.html");
	    webClient.waitForBackgroundJavaScript(4000);
		System.out.println(restartPage.asText());
	    webClient.waitForBackgroundJavaScript(4000);
	    webClient.getOptions().setTimeout(4000);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Es wurde kein Content registriert. Fehler 2!");
			return false;
		}
		return true;
	}
	
	private WebClient login_cbn_webinterface(String cbnIP, String cbnUsername, String cbnPassword) {
		WebClient webClient = null;
		try {
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getCookieManager().setCookiesEnabled(true);
		    webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		    webClient.getOptions().setTimeout(1000);
			HtmlPage loginPage = (HtmlPage)webClient.getPage("http://" + cbnIP + "/common_page/login.html");
		    webClient.waitForBackgroundJavaScript(500);
			HtmlForm form = loginPage.getHtmlElementById("form-login");
			HtmlTextInput userField = form.getInputByName("loginUsername");
			userField.setValueAttribute(cbnUsername);
		    HtmlPasswordInput passField = form.getInputByName("loginPassword");
			passField.setValueAttribute(cbnPassword);
			loginPage = form.getInputByValue("Login").click();
		    webClient.waitForBackgroundJavaScript(2000);
			System.out.println(loginPage.asText());
		    webClient.getOptions().setTimeout(1000);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Es wurde kein Content registriert. Fehler!");
		}
		return webClient;
	}

}
