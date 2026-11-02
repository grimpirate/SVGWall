#include <stdio.h>
#include <stdlib.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <X11/Xatom.h>

// Compilation: cc -fPIE -o x11root docker_x11root.c -lX11
// Usage: openssl rand $((width * height * 4)) | ./a.out

int main()
{
	Display *display = XOpenDisplay(NULL);
	if (!display) return EXIT_FAILURE;

	int screen = XDefaultScreen(display);
	Window root = XRootWindow(display, screen);
	if (!root)
	{
		XCloseDisplay(display);
		return EXIT_FAILURE;
	}

	int width = XDisplayWidth(display, screen);
	int height = XDisplayHeight(display, screen);
	int depth = XDefaultDepth(display, screen);

	// 1. Match visual configuration that handles TrueColor processing dynamically
	XVisualInfo vinfo;
	Visual *visual;
	if (XMatchVisualInfo(display, screen, depth, TrueColor, &vinfo)) {
		visual = vinfo.visual;
	} else {
		visual = XDefaultVisual(display, screen);
	}

	int bytes_per_pixel = 4;
	int quantum = 32;
	int buffer = width * height * bytes_per_pixel;
	
	char* data = (char*)malloc(buffer);
	if (!data)
	{
		XCloseDisplay(display);
		return EXIT_FAILURE;
	}

	// Read exact BGRA payload from stdin
	fread(data, 1, buffer, stdin);

	// Let native XCreateImage calculate inner paddings and function tables securely
	XImage *image = XCreateImage(
		display,
		visual,
		depth,
		ZPixmap,
		0,
		data,
		width,
		height,
		quantum,
		width * bytes_per_pixel
	);

	if (!image)
	{
		free(data);
		XCloseDisplay(display);
		return EXIT_FAILURE;
	}

	// Create a persistent Pixmap on the server matching target depth dimensions
	Pixmap pixmap = XCreatePixmap(display, root, width, height, depth);
	GC gc = XCreateGC(display, pixmap, 0, NULL);

	// Draw your BGRA image onto the offscreen pixmap safely
	XPutImage(display, pixmap, gc, image, 0, 0, 0, 0, width, height);

	// Get global property handles recognized across window managers and Selkies
	Atom prop_rootpmap = XInternAtom(display, "_XROOTPMAP_ID", False);
	Atom prop_esetroot = XInternAtom(display, "ESETROOT_PMAP_ID", False);

	// Update root window global attributes using our persistent Pixmap ID references
	XChangeProperty(display, root, prop_rootpmap, XA_PIXMAP, 32, PropModeReplace, (unsigned char *)&pixmap, 1);
	XChangeProperty(display, root, prop_esetroot, XA_PIXMAP, 32, PropModeReplace, (unsigned char *)&pixmap, 1);

	// Assign the background buffer and force an exposure draw layer refresh loop
	XSetWindowBackgroundPixmap(display, root, pixmap);
	XClearWindow(display, root);
	
	// 2. CRITICAL CHANGE: Set Close Down Mode to preserve the pixmap allocation on the server
	// This makes it safe to exit the one-shot utility without destroying the wallpaper reference.
	XSetCloseDownMode(display, RetainPermanent);
	
	XFlush(display);

	// Clean up local client-side structures
	XFreeGC(display, gc);
	
	// XDestroyImage automatically handles freeing the nested raw 'data' pointer block
	XDestroyImage(image); 
	XCloseDisplay(display);

	return EXIT_SUCCESS;
}