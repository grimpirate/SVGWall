package com.grimpirate;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaScript
{
	private CoordinateText[] texts;
	
	public JavaScript(String js, String version, Dimension dimension) throws FileNotFoundException, IOException
	{
		Context context = Context.enter();
		Scriptable scope = new ImporterTopLevel(context);
		ScriptableObject host = (ScriptableObject) ScriptableObject.getObjectPrototype(scope);
		ScriptableObject.putConstProperty(host, "version", Context.javaToJS(version, scope));
		ScriptableObject.putConstProperty(host, "width", Context.javaToJS(dimension.getWidth(), scope));
		ScriptableObject.putConstProperty(host, "height", Context.javaToJS(dimension.getHeight(), scope));
		ScriptableObject.putConstProperty(host, "jvm", Context.javaToJS(System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version"), scope));
		ScriptableObject.putConstProperty(host, "os_ver", Context.javaToJS(System.getProperty("os.version"), scope));
		ScriptableObject.putConstProperty(host, "os_arch", Context.javaToJS(System.getProperty("os.name") + " " + System.getProperty("os.arch"), scope));
		ScriptableObject.putConstProperty(host, "time", Context.javaToJS(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), scope));
		ScriptableObject.putConstProperty(scope, "SVGWall", Context.javaToJS(host, scope));
		CoordinateText[] texts = Arrays.stream(((NativeArray) context.evaluateReader(scope, new java.io.FileReader(js), "<cmd>", 1, null)).toArray()).toArray(CoordinateText[]::new);
		Context.exit();
		this.texts = texts;
	}
	
	public CoordinateText[] getText()
	{
		return texts;
	}
}
