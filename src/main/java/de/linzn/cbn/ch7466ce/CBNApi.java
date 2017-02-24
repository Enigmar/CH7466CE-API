package de.linzn.cbn.ch7466ce;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
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
			final WebClient webClient = new WebClient();
			final HtmlPage loginPage = webClient.getPage(cbnIP + "/common_page/login.html");
			final HtmlForm form = loginPage.getFormByName("form-login");
	
			final HtmlSubmitInput loginButton = form.getInputByValue("Login");
			final HtmlTextInput userField = form.getInputByName("loginUsername");
			userField.setValueAttribute(cbnUsername);
			final HtmlTextInput passField = form.getInputByName("loginPassword");
			passField.setValueAttribute(cbnPassword);
			final HtmlPage returnPage = loginButton.click();
			System.out.println(returnPage.asText());
		} catch (IOException e) {
			System.out.println("Es wurde kein Content registriert. Fehler!");
			return false;
		}
		return true;
	}

}
