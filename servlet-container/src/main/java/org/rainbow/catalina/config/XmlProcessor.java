/**
 * 
 */
package org.rainbow.catalina.config;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author biya-bi
 *
 */
public class XmlProcessor<T> {
	private Class<T> clazz;

	public XmlProcessor(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public T unmarshall(String path) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new FileReader(path));
	}
}
