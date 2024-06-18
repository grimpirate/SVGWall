package com.grimpirate;

public class AlignText
{
	private String text;
	private Alignment alignment;

	public AlignText(String text)
	{
		this(text, Alignment.LEFT);
	}
	
	public AlignText(String text, Alignment alignment)
	{
		this.text = text;
		this.alignment = alignment;
	}
	
	public String getText()
	{
		return text;
	}
	
	public Alignment getAlignment()
	{
		return alignment;
	}
}
