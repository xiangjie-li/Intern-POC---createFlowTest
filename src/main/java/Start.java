
import javax.inject.Inject;

import java.util.Collections;

import javax.inject.Inject;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.core.api.construct.Flow;
import org.mule.runtime.core.api.context.notification.MuleContextNotification;
import org.mule.runtime.core.api.context.notification.MuleContextNotificationListener;
import org.mule.runtime.core.api.event.CoreEvent;
import org.mule.runtime.core.api.event.EventContextFactory;
import org.mule.runtime.dsl.api.component.config.DefaultComponentLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Start {

	@Inject
	Registry muleRegistry;
	
	public Start(){
	}
	
	
	public void startFlow() throws MuleException {
		// add flow by creating new muleContext
//	    MuleContextFactory muleContextFactory = new DefaultMuleContextFactory();				
//		System.out.println("create new mule context factory");
//		
//		
//		List<ConfigurationBuilder> builders = new ArrayList<ConfigurationBuilder>();
//		ConfigurationBuilder builder = (ConfigurationBuilder) new SpringXmlConfigurationBuilder("testFlow.xml");
//		builders.add(builder);
//		System.out.println("create new configBuilder and builders");
//		
//		MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
//		System.out.println("create new contextBuilder");
//		
//		MuleContext muleContext = (MuleContext) muleContextFactory.createMuleContext(builder, contextBuilder);
//		muleContext.start();
		System.out.println("ready to start the flow");
		
		Flow flow = (Flow) muleRegistry.lookupByName("connect").get();
		System.out.println("get the flow");
		flow.start();
		System.out.println("start the connect flow");
	}

}
