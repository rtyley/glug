package glug.parser;

import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
import glug.model.IntervalTypeDescriptor;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;


public class GlugConfigTest {

	private final static JAXBContext context = context();
	
	private final static JAXBContext context() {
		try {
			return JAXBContext.newInstance(GlugConfig.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void shouldBeLADeDa() throws JAXBException {
		GlugConfig config = new GlugConfig();
		config.getIntervalTypes().add(new IntervalTypeDescriptor(RED,"Thunder"));
		config.getIntervalTypes().add(new IntervalTypeDescriptor(BLACK,"Strong"));
		StringWriter stringWriter = new StringWriter();
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
		marshaller.marshal(config, stringWriter);
		System.out.println(stringWriter.toString());
	}
	
	@Test
	public void shouldBeBeauty() throws JAXBException {
		String xml = "<glugConfig>\n" + 
				"	<intervalTypes>\n" + 
				"		<intervalType id=\"pageRequest\" description=\"Page Request\" defaultColour=\"RED\" />\n" + 
				"		<intervalType id=\"dbQuery\" description=\"Database Query\" defaultColour=\"BLACK\" />\n" + 
				"	</intervalTypes>\n" + 
//				"	<messageParsers>\n" + 
//				"\n" + 
//				"		<message intervalType=\"pageRequest\"\n" + 
//				"			logger=\"com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter\"\n" + 
//				"			dataFieldIndex=\"1\" durationInMillisFieldIndex=\"2\" \n" + 
//				"			>Request for ([^ ]+?) completed in (\\\\d+?) ms</message>\n" + 
//				"\n" + 
//				"		<message intervalType=\"dbQuery\"\n" + 
//				"			logger=\"com.gu.r2.common.diagnostic.database.PreparedStatementProxy\"\n" + 
//				"			dataFieldIndex=\"1\" durationInMillisFieldIndex=\"3\" \n" + 
//				"			>Query \"(.+?)\" \\(component: (.+)\\) completed in (\\d+) ms</message>\n" + 
//				"\n" + 
//				"	</messageParsers>\n" + 
				"</glugConfig>";
		GlugConfig glugConfig = (GlugConfig) context.createUnmarshaller().unmarshal(new StringReader(xml));
		//assertThat(glugConfig.getIntervalTypes().size(), equalTo(2));
	}
	
}
