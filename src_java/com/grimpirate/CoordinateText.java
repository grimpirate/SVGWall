package com.grimpirate;

public class CoordinateText
{
	private float x;
	private float y;
	private String text;
	private Anchor alignment;

	public CoordinateText(float x, float y, String text)
	{
		this(x, y, text, Anchor.LEFT);
	}
	
	public CoordinateText(float x, float y, String text, Anchor alignment)
	{
		this.x = x;
		this.y = y;
		this.text = text;
		this.alignment = alignment;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public String getText()
	{
		return text;
	}
	
	public Anchor getAlignment()
	{
		return alignment;
	}
}
