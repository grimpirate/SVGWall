package com.grimpirate;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.w3c.dom.Document;

public class BufferedImageTranscoder extends ImageTranscoder
{
	private BufferedImage image;
	private DataBufferByte buffer;
	
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
		return new BufferedImage(new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] {8,8,8,8},
				true,
				false,
				Transparency.OPAQUE,
				DataBuffer.TYPE_BYTE),
			Raster.createInterleavedRaster(
				new DataBufferByte(with * 4 * height),
				width,
				height,
				width * 4,
				4,
				new int[] {2, 1, 0, 3}, // BGRA offsets
				null),
		false,
		null);
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
		return buffer.getData();
	}
}
