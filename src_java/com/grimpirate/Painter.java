package com.grimpirate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.batik.transcoder.TranscoderException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Painter
{
	private BufferedImage image;
	private Graphics2D g2d;
	public static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 18);
	public static final Color COLOR = Color.WHITE;
	public static final float MARGIN = 10f;

	public Painter(String svg)
	{
		boolean rainbow = false;
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		try
		{
			this.image = new BufferedImageTranscoder(svg, (float)dimension.getWidth(), (float)dimension.getHeight()).getBufferedImage();
		}
		catch (TranscoderException e)
		{
			this.image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
			rainbow = true;
		}

		g2d = image.createGraphics();
		g2d.setColor(COLOR);
		g2d.setFont(FONT);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if(rainbow)
			drawRainbow();
	}

	public void drawRainbow()
	{
		int offset = 48;
		for(int i = 0; i < image.getHeight(); i += offset)
		{
			int red = (i * 255) / (image.getHeight() - 1);
			for(int j = 0; j < image.getWidth(); j += offset)
			{
				int green = (j * 255) / (image.getWidth() - 1);
				int blue = 128;
				g2d.setColor(new Color((red << 16) | (green << 8) | blue));
				g2d.fillRect(j, i, offset, offset);
			}
		}
	}

	/**
	 * Returns a JNI char* compatible byte array for use with the XCreateImage function of the Xlib library.
	 * 
	 * This method retrieves raster data from a BufferedImage as a DataBufferInt. A ByteBuffer is created
	 * to transfer the raster data to the custom JNIX11 library. The byte order of the integer data
	 * must be reversed as BufferedImages are created with integers in ARGB format and the XCreateImage
	 * function uses byte data in the BGRA format.
	 * 
	 * @return a byte array of raster data
	 */
	public byte[] getImageData()
	{
		ByteBuffer buffer  = ByteBuffer.allocate(image.getWidth() * image.getHeight() * 4);
		Arrays.stream(((DataBufferInt) image.getData().getDataBuffer()).getData())
			.forEach(i -> buffer.putInt(Integer.reverseBytes(i)));
		return buffer.array();
	}
	
	public void drawOverlay(String js, String version) throws FileNotFoundException, IOException
	{
		Context context = Context.enter();
		Scriptable scope = createScriptable(context, version);
		CoordinateText[] texts = Arrays.stream(((NativeArray) context.evaluateReader(scope, new java.io.FileReader(js), "<cmd>", 1, null)).toArray()).toArray(CoordinateText[]::new);
		
		for(CoordinateText text : texts)
		{
			float width = (float)FONT.getStringBounds(text.getText(), g2d.getFontRenderContext()).getWidth();
			switch(text.getAlignment())
			{
			case LEFT:
				g2d.drawString(text.getText(), text.getX(), text.getY());
				break;
			case RIGHT:
				g2d.drawString(text.getText(), text.getX() - width, text.getY());
				break;
			case CENTER:
				g2d.drawString(text.getText(), text.getX() - width * 0.5f, text.getY());
				break;
			}
		}
	}
	
	private Scriptable createScriptable(Context context, String version)
	{
		Scriptable scope = new ImporterTopLevel(context);
		ScriptableObject host = (ScriptableObject) ScriptableObject.getObjectPrototype(scope);
		ScriptableObject.putConstProperty(host, "version", Context.javaToJS(version, scope));
		ScriptableObject.putConstProperty(host, "width", Context.javaToJS(image.getWidth(), scope));
		ScriptableObject.putConstProperty(host, "height", Context.javaToJS(image.getHeight(), scope));
		ScriptableObject.putConstProperty(scope, "SVGWall", Context.javaToJS(host, scope));
		
		return scope;
	}
}
