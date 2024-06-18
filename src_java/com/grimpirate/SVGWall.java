package com.grimpirate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
		
		Painter painter = new Painter(svg);
		
		AlignText[] texts = new AlignText[] {
				new AlignText(
						System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version")),
				new AlignText(
						System.getProperty("os.version")),
				new AlignText(
						System.getProperty("os.name") + " " + System.getProperty("os.arch")),
				new AlignText(
						"--grimpirate.com--",
						Alignment.CENTER),
				new AlignText(
						currentTimestamp(),
						Alignment.RIGHT),
				new AlignText(
						result.commandSpec().version()[0],
						Alignment.RIGHT),
		};

		float vOffset = 18f;
		AlignText prior = texts[0];
		for(AlignText text : texts)
		{
			vOffset = text.getAlignment() != prior.getAlignment() ? 0 : vOffset - 18f;
			painter.drawString(text.getText(), text.getAlignment(), vOffset);
			prior = text;
		}
		
		apply(painter.getImageData());
	}
	
	private static String currentTimestamp()
	{
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
