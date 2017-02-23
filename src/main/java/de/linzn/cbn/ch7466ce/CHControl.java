package de.linzn.cbn.ch7466ce;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class CHControl {
	private static  CHControl instance;
	
	private String cbnIP = "";
	private String cbnUsername = "";
	private String cbnPassword = "";
	

	public static void main(String[] args) {
		instance = new CHControl(args);

	}
	
	public CHControl(String[] args){
		
	}
	
	
	
	private void loginCBN() throws IOException{
		final WebClient webClient = new WebClient();
		final HtmlPage loginPage = webClient.getPage(cbnIP + "/common_page/login.html");
		final HtmlForm form = loginPage.getFormByName("form-login");

		final HtmlSubmitInput loginButton = form.getInputByValue("Login");
		final HtmlTextInput userField = form.getInputByName("loginUsername");
		userField.setValueAttribute(cbnUsername);
		final HtmlTextInput passField = form.getInputByName("loginPassword");
		passField.setValueAttribute(cbnPassword);
		final HtmlPage page2 = loginButton.click();
		System.out.println(page2.asText());
	}

	public CHControl getInstance(){
		return instance;
	}
	
}
