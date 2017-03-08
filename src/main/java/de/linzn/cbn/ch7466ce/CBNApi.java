package de.linzn.cbn.ch7466ce;

import java.io.IOException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class CBNApi {
	

	// Public API
	
	
	public void restartCBN(String cbnIP, String cbnUsername, String cbnPassword){
		Boolean status;
		status = this.login_cbn_webinterface(cbnIP, cbnUsername, cbnPassword);
		System.out.println("Login: " + status);
		
	}
	
	

	
	// Private functions
	
	
	private boolean login_cbn_webinterface(String cbnIP, String cbnUsername, String cbnPassword) {
		try {
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
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
			loginPage = (HtmlPage)webClient.getPage("http://" + cbnIP + "/common_page/reboot.html");
		    webClient.waitForBackgroundJavaScript(2000);
			System.out.println(loginPage.asText());
		    webClient.waitForBackgroundJavaScript(2000);
		    webClient.getOptions().setTimeout(1000);
			webClient.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Es wurde kein Content registriert. Fehler!");
			return false;
		}
		return true;
	}

}
