package engine.rendering;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import engine.core.space.AABB;

public class ArrayBitmap implements IBitmap {
	private final int width;
	private final int height;
	private final int colors[];

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getPixel(int i, int j) {
		return colors[getIndex(i, j)];
	}

	public ArrayBitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.colors = new int[width * height];
	}

	public ArrayBitmap(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(fileName));

		this.width = image.getWidth();
		this.height = image.getHeight();

		int imgPixels[] = new int[getNumPixels()];
		image.getRGB(0, 0, width, height, imgPixels, 0, width);
		this.colors = imgPixels;
	}

	@Override
	public IBitmap clear(int color) {
		Arrays.fill(colors, color);
		return this;
	}

	@Override
	public IBitmap setPixel(int x, int y, int color) {
		colors[getIndex(x, y)] = color;
		return this;
	}

	@Override
	public int[] copyToIntArray(int[] dest) {
		if (dest.length < getNumPixels()) {
			throw new IllegalArgumentException(
					"Result array is not large enough to store all pixels");
		}

		for (int i = 0; i < getNumPixels(); i++) {
			dest[i] = colors[i];
		}
		return dest;
	}

	@Override
	public void save(String filetype, File file) throws IOException {
		BufferedImage output = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int[] displayComponents = ((DataBufferInt) output.getRaster()
				.getDataBuffer()).getData();
		copyToIntArray(displayComponents);

		ImageIO.write(output, filetype, file);
	}

	private int getNumPixels() {
		return width * height;
	}

	private int getIndex(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			throw new IllegalArgumentException("Location (" + x + ", " + y
					+ ") is not within the range of (0,0) to (" + width + ", "
					+ height + ")");
		}

		return y * width + x;
	}
}
