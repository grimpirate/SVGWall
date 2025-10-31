#include <stdio.h>
#include <stdlib.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>

// head -c width * height * bytes_per_pixel | a.out

int main()
{
	Display *display = XOpenDisplay(NULL);
	if(!display) return EXIT_FAILURE;
	int screen = XDefaultScreen(display);
	Window root = XRootWindow(display, screen);
	if(!root)
	{
		XCloseDisplay(display);
		return EXIT_FAILURE;
	}
	int width = XDisplayWidth(display, screen);
	int height = XDisplayHeight(display, screen);
	int depth = XDefaultDepth(display, screen);
	Pixmap pixmap = XCreatePixmap(display, root, width, height, depth);
	GC graphics = XCreateGC(display, pixmap, 0, NULL);

	int bytes_per_pixel = 4;
	int quantum = bytes_per_pixel * 8;	// 32
	int buffer = width * height * bytes_per_pixel;
	char* data = (char*)malloc(buffer);
	if(!data)
	{
		XFreeGC(display, graphics);
		XFreePixmap(display, pixmap);
		XDestroyWindow(display, root);
		XCloseDisplay(display);
		return EXIT_FAILURE;
	}
//	printf("%d", buffer);
	fread(data, 1, buffer, stdin);
	/*for (int y = height / 2; y < height; y++) {
		for (int x = width / 2; x < width; x++) {
			unsigned char color = ((x / 10) % 2 == (y / 10) % 2) ? 0XFF : 0X00;
			data[(y * width + x) * bytes_per_pixel + 0] = 255;   // Blue
			data[(y * width + x) * bytes_per_pixel + 1] = 255;   // Green
			data[(y * width + x) * bytes_per_pixel + 2] = 255; // Red
			data[(y * width + x) * bytes_per_pixel + 3] = 255; // Alpha
			for(int i = 0; i < bytes_per_pixel; i++)
			{
				data[(y * width + x) * bytes_per_pixel + i] = color;
			}
		}
	}*/
	// https://tronche.com/gui/x/xlib/utilities/XCreateImage.html
	XImage *image = XCreateImage(
		display,
		XDefaultVisual(display, screen),
		depth,
		ZPixmap,	// ZPixmap from X11/X.h defined as the value 2
		0,
		data,
		width,
		height,
		quantum,
		0
	);
	if(!image)
	{
		free(data);
		XFreeGC(display, graphics);
		XFreePixmap(display, pixmap);
		XDestroyWindow(display, root);
		XCloseDisplay(display);
		return EXIT_FAILURE;
	}
	XPutImage(display, pixmap, graphics, image, 0, 0, 0, 0, width, height);
	XSetWindowBackgroundPixmap(display, root, pixmap);
	XClearWindow(display, root);
	XFlush(display);

	XDestroyImage(image);
	XFreeGC(display, graphics);
	XFreePixmap(display, pixmap);
	XDestroyWindow(display, root);
	XCloseDisplay(display);

	return EXIT_SUCCESS;
}
