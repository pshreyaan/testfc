package pages.upload;

import static utils.Methods.*;
import components.Network;

public class UploadNetworkXML{
	
	private Network network;
	
    public void uploadFlow(String Name) {
        network.navigateDashboard();
        network.clickSubmitMessage();
        network.clickLiveProcessing();
        network.inboundConfiguration();
        network.pasteMessage(Name);
        network.submitMessage();
        waitForPageLoad();
    }
}