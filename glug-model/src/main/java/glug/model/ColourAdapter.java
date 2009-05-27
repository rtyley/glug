package glug.model;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColourAdapter extends XmlAdapter<String, Color> {

	@Override
	public String marshal(Color v) throws Exception {
		return Integer.toHexString(v.getRGB()).substring(2);
	}

	@Override
	public Color unmarshal(String v) throws Exception {
		return new Color(Integer.parseInt(v, 16));
	}

}
