/**
 * 
 */
package org.rainbow.catalina.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author biya-bi
 *
 */
public class DeploymentDescriptorProcessorTest {
	private XmlProcessor<DeploymentDescriptor> processor = new XmlProcessor<>(DeploymentDescriptor.class);

	private final String webInfPathName = System.getProperty("user.dir") + File.separator + "target" + File.separator
			+ "WEB-INF";
	private final String deploymentDescriptorPathName = webInfPathName + File.separator + "web.xml";

	private File webInf;

	@Before
	public void setUp() {
		webInf = new File(webInfPathName);
		webInf.mkdir();
	}

	@After
	public void cleanUp() {
		if (webInf.exists()) {
			deleteDirectory(webInf);
		}
	}

	private boolean deleteDirectory(File dir) {
		File[] allContents = dir.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return dir.delete();
	}

	@Test
	public void unmarshall_deploymentDescriptorExists_DeploymentDescriptorUnmarshalled()
			throws FileNotFoundException, JAXBException, UnsupportedEncodingException {
		try (PrintWriter writer = new PrintWriter(deploymentDescriptorPathName, "UTF-8")) {
			writer.println("<web-app>");
			writer.println("</web-app>");
		}

		DeploymentDescriptor deploymentDescriptor = processor.unmarshall(deploymentDescriptorPathName);

		Assert.assertNotNull(deploymentDescriptor);
	}

	@Test
	public void unmarshall_ServletMappingsExist_ServletMappingsReturned()
			throws FileNotFoundException, JAXBException, UnsupportedEncodingException {
		ServletMapping[] expecteds = { new ServletMapping("servlet1", "pattern1"),
				new ServletMapping("servlet2", "pattern2") };
		try (PrintWriter writer = new PrintWriter(deploymentDescriptorPathName, "UTF-8")) {
			writer.println("<web-app>");

			for (ServletMapping mapping : expecteds) {
				writer.println("<servlet-mapping>");

				writer.println("<servlet-name>");
				writer.println(mapping.getServletName());
				writer.println("</servlet-name>");

				writer.println("<url-pattern>");
				writer.println(mapping.getUrlPattern());
				writer.println("</url-pattern>");

				writer.println("</servlet-mapping>");
			}

			writer.println("</web-app>");
		}

		DeploymentDescriptor deploymentDescriptor = processor.unmarshall(deploymentDescriptorPathName);

		ServletMapping[] actuals = deploymentDescriptor.getServletMappings().toArray(new ServletMapping[2]);

		Assert.assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void unmarshall_ServletsExist_ServletsReturned()
			throws FileNotFoundException, JAXBException, UnsupportedEncodingException {
		Servlet servlet1 = new Servlet("servlet1", "class1");
		servlet1.setInitParams(Arrays.asList(new InitParam("servlet1-param1", "servlet1-value1")));

		Servlet servlet2 = new Servlet("servlet2", "class2");
		servlet2.setInitParams(Arrays.asList(new InitParam("servlet2-param1", "servlet2-value1")));

		Servlet[] expecteds = { servlet1, servlet2 };
		try (PrintWriter writer = new PrintWriter(deploymentDescriptorPathName, "UTF-8")) {
			writer.println("<web-app>");

			for (Servlet servlet : expecteds) {
				writer.println("<servlet>");

				writer.println("<servlet-name>");
				writer.println(servlet.getServletName());
				writer.println("</servlet-name>");

				writer.println("<servlet-class>");
				writer.println(servlet.getServletClass());
				writer.println("</servlet-class>");

				for (InitParam param : servlet.getInitParams()) {
					writer.println("<init-param>");

					writer.println("<param-name>");
					writer.println(param.getParamName());
					writer.println("</param-name>");

					writer.println("<param-value>");
					writer.println(param.getParamValue());
					writer.println("</param-value>");

					writer.println("</init-param>");
				}

				writer.println("</servlet>");
			}

			writer.println("</web-app>");
		}

		DeploymentDescriptor deploymentDescriptor = processor.unmarshall(deploymentDescriptorPathName);

		Servlet[] actuals = deploymentDescriptor.getServlets().toArray(new Servlet[2]);

		Assert.assertArrayEquals(expecteds, actuals);

		for (int i = 0; i < expecteds.length; i++) {
			final List<InitParam> expectedInitParams = expecteds[i].getInitParams();
			final List<InitParam> actualInitParams = actuals[i].getInitParams();
			Assert.assertArrayEquals(expectedInitParams.toArray(new InitParam[expectedInitParams.size()]),
					actualInitParams.toArray(new InitParam[actualInitParams.size()]));
		}
	}
}
