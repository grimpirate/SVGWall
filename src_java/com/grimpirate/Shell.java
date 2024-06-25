package com.grimpirate;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.PicocliException;
import picocli.CommandLine.UnmatchedArgumentException;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

public class Shell
{
	private ParseResult result = null;
	
	public Shell(String... args) throws PicocliException, SAXException, IOException, ParserConfigurationException, InterruptedException
	{
		CommandLine command = new CommandLine(getCommandSpec());
		
		try
		{
			result = command.parseArgs(args);
		}
		catch(ParameterException e)
		{
			command.getErr().println(e.getMessage());
			if(!UnmatchedArgumentException.printSuggestions(e, command.getErr()))
				e.getCommandLine().usage(command.getErr());
			System.exit(command.getCommandSpec().exitCodeOnInvalidInput());
		}
		
		if(command.isUsageHelpRequested())
		{
			command.usage(command.getOut());
			System.exit(command.getCommandSpec().exitCodeOnUsageHelp());
		}
		
		if(result.hasMatchedOption("--update"))
		{
			ProcessBuilder builder = new ProcessBuilder();
			builder.command("sh", "-c", "$APPDIR/usr/bin/appimageupdatetool-x86_64.AppImage -r $APPIMAGE");
			Process process = builder.inheritIO().start();
			System.exit(process.waitFor());
		}
		
		if(result.hasMatchedOption("--check"))
		{
			ProcessBuilder builder = new ProcessBuilder();
			builder.command("sh", "-c", "$APPDIR/usr/bin/appimageupdatetool-x86_64.AppImage -j $APPIMAGE");
			Process process = builder.inheritIO().start();
			int exitCode = process.waitFor();
			switch(exitCode)
			{
			case 0:
				command.getOut().println("No update(s) available.");
				System.exit(exitCode);
			case 1:
				command.getOut().println("Update(s) available.");
				System.exit(exitCode);
			default:
				command.getErr().println("Unable to check for update(s).");
				System.exit(exitCode);
			}
		}
		
		command.getCommandSpec().version(parseXMLInfo(result.matchedOption("--meta").getValue()));
		
		if(command.isVersionHelpRequested())
		{
			command.printVersionHelp(command.getOut());
			System.exit(command.getCommandSpec().exitCodeOnVersionHelp());
		}
	}
	
	public ParseResult getParseResult()
	{
		return result;
	}
	
	private String parseXMLInfo(File xml) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(xml);
		NodeList nodeList = document.getElementsByTagName("name");
		StringBuilder version = new StringBuilder(nodeList.item(0).getTextContent());
		nodeList = document.getElementsByTagName("release");
		version.append(" v");
		version.append(nodeList.item(0).getAttributes().getNamedItem("version").getTextContent());
		return version.toString();
	}
	
	private CommandSpec getCommandSpec()
	{
		CommandSpec spec = CommandSpec.create();
		spec.name(SVGWall.APP_NAME);
		spec.mixinStandardHelpOptions(true);
		spec.addOption(OptionSpec.builder("-m", "--meta")
				.required(true)
				.hidden(true)
				.paramLabel("FILE")
				.type(File.class)
				.description("AppStream metadata XML file")
				.build());
		spec.addOption(OptionSpec.builder("-s", "--svg")
				.required(false)
				.paramLabel("FILE")
				.type(String.class)
				.description("SVG file")
				.build());
		spec.addOption(OptionSpec.builder("-o", "--overlay")
				.required(false)
				.paramLabel("FILE")
				.type(String.class)
				.description("Text overlay JavaScript file")
				.build());
		spec.addOption(OptionSpec.builder("-u", "--update")
				.required(false)
				.type(Boolean.class)
				.description("Update SVGWall-x86_64.AppImage and exit.")
				.build());
		spec.addOption(OptionSpec.builder("-c", "--check")
				.required(false)
				.type(Boolean.class)
				.description("Check for SVGWall-x86_64.AppImage update(s) and exit.")
				.build());
		
		return spec;
	}
}
