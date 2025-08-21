package pages.login;

import org.openqa.selenium.WebDriver;

import components.Network;

import static utils.Methods.*;

public class NetworkLoginFlow {

    private Network network;

    public void loginFlow(WebDriver driver, String email, String password, String url) {
        driver.get(url);
        this.network = new Network(driver);
        network.login(email, password);
        if (!network.isLoginSuccessful()) { 
            throw new RuntimeException("Login failed!");
        }
        System.out.println("Logged in successfully");
        
    }
}
