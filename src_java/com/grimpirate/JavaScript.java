package com.grimpirate;

import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JavaScript
{
	private Document document;

	public JavaScript(File js, String version, Dimension dimension) throws IllegalAccessException, InstantiationException, InvocationTargetException
	{
		Context context = Context.enter();
		context.setLanguageVersion(Context.VERSION_ES6);
		Scriptable scope = new ImporterTopLevel(context);
		ScriptableObject.defineClass(scope, SVG.class);
		ScriptableObject host = context.initSafeStandardObjects();
		ScriptableObject.putConstProperty(host, "version", Context.javaToJS(version, scope));
		ScriptableObject.putConstProperty(host, "width", Context.javaToJS(dimension.getWidth(), scope));
		ScriptableObject.putConstProperty(host, "height", Context.javaToJS(dimension.getHeight(), scope));
		ScriptableObject.putConstProperty(host, "jvm", Context.javaToJS(System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version"), scope));
		ScriptableObject.putConstProperty(host, "os_ver", Context.javaToJS(System.getProperty("os.version"), scope));
		ScriptableObject.putConstProperty(host, "os_arch", Context.javaToJS(System.getProperty("os.name") + " " + System.getProperty("os.arch"), scope));
		ScriptableObject.putConstProperty(host, "chrono", Context.javaToJS(new Chrono(), scope));
		ScriptableObject.putConstProperty(host, "network", Context.javaToJS(new LocalNetwork(), scope));
		ScriptableObject.putConstProperty(scope, "Platform", Context.javaToJS(host, scope));
		try
		{
			context.evaluateReader(scope, new java.io.FileReader(js), js.getName(), 1, null);
			Element element = (Element) Context.jsToJava(scope.get("svg", scope), Element.class);
			document = element.getOwnerDocument();
			//texts = Arrays.stream(((NativeArray) context.evaluateReader(scope, new java.io.FileReader(js), "<cmd>", 1, null)).toArray()).toArray(CoordinateText[]::new);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Context.exit();
			document = SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
		}
	}

	public Document getDocument()
	{
		return document;
	}
}
