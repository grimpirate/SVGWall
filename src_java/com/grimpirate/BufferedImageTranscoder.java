package com.grimpirate;

import java.awt.image.BufferedImage;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

public class BufferedImageTranscoder extends ImageTranscoder
{
	private BufferedImage image;
	
	public BufferedImageTranscoder(String svg, float width, float height) throws TranscoderException
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
}
