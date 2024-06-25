package com.grimpirate;

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
		String js = result.matchedOptionValue("--overlay", null);

		Painter painter = new Painter(svg);

		if(null != js)
			painter.drawOverlay(js, result.commandSpec().version()[0]);

		apply(painter.getImageData());
	}
}
