package com.grimpirate;

import java.io.IOException;
import java.util.Map;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class SVG extends ScriptableObject
{
	public static Document document;

	@Override
	public String getClassName()
	{
		return "SVG";
	}
	
	@JSStaticFunction
	public static Element load(String uri) throws IOException
	{
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
		document = factory.createSVGDocument(uri);
		return document.getDocumentElement();
	}

	@JSStaticFunction
	public static Element blank(NativeObject attributes)
	{
		document = SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
		Element svg = document.getDocumentElement();
		//Map<String, String> attr = Arrays.stream(NativeObject.getPropertyIds(attributes)).collect(Collectors.toMap(e -> e.toString(), e -> NativeObject.getProperty(attributes, e.toString()).toString()));
		if(null != attributes)
			for(Map.Entry<Object, Object> entry : attributes.entrySet())
				svg.setAttributeNS(null, entry.getKey().toString(), entry.getValue().toString());
		return svg;
	}

	@JSStaticFunction
	public static Element element(String type, NativeObject attributes, String content)
	{
		Element element = document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, type);

		if(null != attributes)
			for(Map.Entry<Object, Object> entry : attributes.entrySet())
				element.setAttributeNS(null, entry.getKey().toString(), entry.getValue().toString());
		if(null == content)
			return element;
		if(content.equalsIgnoreCase("undefined"))
			return element;
		element.appendChild(document.createTextNode(content));
		return element;
	}
}
