package de.linzn.cbn.api.ch7466ce;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.logging.Level;

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

    private static void signal(String text){
        System.out.println("CBN " + text);
    }

    // Private functions

    public boolean restart_cbn_webinterface(String cbnIP) {
        try {
            signal("Create first reboot request to cnb-interface.");
            this.client.waitForBackgroundJavaScript(4000);
            this.client.getPage("http://" + cbnIP + "/common_page/reboot.html");
            this.client.waitForBackgroundJavaScript(4000);
            signal("Create second reboot request as backup to cnb-interface.");
            this.client.getPage("http://" + cbnIP + "/common_page/reboot.html");
            this.client.waitForBackgroundJavaScript(6000);
            signal("Request complete. Exit");
        } catch (IOException e) {
            e.printStackTrace();
            signal("No CBN Page found!");
            return false;
        }
        return true;
    }

    private boolean login_cbn_webinterface(String cbnIP, String cbnUsername, String cbnPassword) {
        try {
            signal("Create login token for cbn-access.");
            HtmlPage loginPage = this.client.getPage("http://" + cbnIP + "/common_page/login.html");
            this.client.waitForBackgroundJavaScript(1000);
            HtmlForm form = loginPage.getHtmlElementById("form-login");
            HtmlTextInput userField = form.getInputByName("loginUsername");
            userField.setValueAttribute(cbnUsername);
            HtmlPasswordInput passField = form.getInputByName("loginPassword");
            passField.setValueAttribute(cbnPassword);
            loginPage = form.getInputByValue("Login").click();
            signal("Run cbn-login.");
            this.client.waitForBackgroundJavaScript(5000);
            signal("Ready for api-access.");
        } catch (IOException e) {
            e.printStackTrace();
            signal("No CBN Page found!");
            return false;
        }
        return true;
    }

    private WebClient init_web_client() {
        signal("Setup javascript based web-access.");
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
