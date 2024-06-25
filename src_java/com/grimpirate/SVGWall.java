package com.grimpirate;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import picocli.CommandLine.ParseResult;
import picocli.CommandLine.PicocliException;

public class SVGWall {

	public static final String APP_NAME = "SVGWall-x86_64.AppImage";

	static
	{
		System.loadLibrary("jnix11");
	}

	private static native void apply(byte[] data);

	public static void main(String... args) throws PicocliException, SAXException, IOException, ParserConfigurationException, InterruptedException
	{
		ParseResult result = (new Shell(args)).getParseResult();

		String svg = result.matchedOptionValue("--svg", "");
		String js = result.matchedOptionValue("--overlay", "");
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		JavaScript script = new JavaScript(js, result.commandSpec().version()[0], dimension);

		Painter painter = new Painter(svg, dimension, script.getText());

		apply(painter.getImageData());
	}
}
