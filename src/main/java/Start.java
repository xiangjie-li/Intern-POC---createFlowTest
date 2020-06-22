
import javax.inject.Inject;

import org.mule.api.MuleException;
import org.mule.construct.Flow;
import org.mule.runtime.api.artifact.Registry;


public class Start {

	@Inject
	Registry muleRegistry;
	
	public Start() {
		
	}
	
	public void createFlow() throws MuleException {
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
