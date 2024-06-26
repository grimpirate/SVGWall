package com.grimpirate;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.w3c.dom.Document;

public class BufferedImageTranscoder extends ImageTranscoder
{
	private BufferedImage image;
	
	public BufferedImageTranscoder(Document svg, float width, float height) throws TranscoderException
	{
		addTranscodingHint(KEY_WIDTH, width);
		addTranscodingHint(KEY_HEIGHT, height);
		TranscoderInput input = new TranscoderInput(svg);
		transcode(input, null);
	}
	
	@Override
	public BufferedImage createImage(int width, int height)
	{
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public void writeImage(BufferedImage image, TranscoderOutput output) throws TranscoderException
	{
		this.image = image;
	}

	public BufferedImage getBufferedImage() throws TranscoderException
	{
		return image;
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
}
