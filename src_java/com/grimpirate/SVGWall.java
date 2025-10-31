package com.grimpirate;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.xml.sax.SAXException;

import picocli.CommandLine.ParseResult;
import picocli.CommandLine.PicocliException;

public class SVGWall {

	public static final String APP_NAME = "SVGWall-x86_64.AppImage";

	private static native void apply(byte[] data);

	public static void main(String... args) throws PicocliException, SAXException, IOException, ParserConfigurationException, InterruptedException, TranscoderException, IllegalAccessException, InstantiationException, InvocationTargetException
	{
		ParseResult result = (new Shell(args)).getParseResult();

		File js = result.matchedOptionValue("--javascript", "");
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		JavaScript script = new JavaScript(js, result.commandSpec().version()[0], dimension);

		BufferedImageTranscoder transcoder = new BufferedImageTranscoder(script.getDocument(), dimension.width, dimension.height);

		File pipe = result.matchedOptionValue("--pipe", "");
		ProcessBuilder builder = new ProcessBuilder(URLDecoder.decode(pipe.getAbsolutePath(), StandardCharsets.UTF_8.name()));
		Process process = builder.start();
		OutputStream stream = process.getOutputStream();
		stream.write(transcoder.getImageData());
		stream.flush();
		stream.close();
	}
}
