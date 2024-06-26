package com.grimpirate;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
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

	public static void main(String... args) throws PicocliException, SAXException, IOException, ParserConfigurationException, InterruptedException, TranscoderException, IllegalAccessException, InstantiationException, InvocationTargetException
	{
		ParseResult result = (new Shell(args)).getParseResult();

		String js = result.matchedOptionValue("--javascript", "");
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		JavaScript script = new JavaScript(js, result.commandSpec().version()[0], dimension);

		BufferedImageTranscoder transcoder = new BufferedImageTranscoder(script.getDocument(), dimension.width, dimension.height);

		apply(transcoder.getImageData());
	}
}
