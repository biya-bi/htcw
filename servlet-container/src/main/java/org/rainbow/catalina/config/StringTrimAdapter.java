/**
 * 
 */
package org.rainbow.catalina.config;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author biya-bi
 *
 */
public class StringTrimAdapter extends XmlAdapter<String, String> {
	@Override
	public String unmarshal(String value) throws Exception {
		if (value == null)
			return null;
		return value.trim();
	}

	@Override
	public String marshal(String value) throws Exception {
		if (value == null)
			return null;
		return value.trim();
	}
}