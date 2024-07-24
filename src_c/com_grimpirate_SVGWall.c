#include <X11/Xlib.h>
#include <X11/Xutil.h>

#include <jni.h>
#include "com_grimpirate_SVGWall.h"

JNIEXPORT void JNICALL Java_com_grimpirate_SVGWall_apply(JNIEnv *env, jclass thisClass, jbyteArray inJNIArray)
{
	Display *display = XOpenDisplay(NULL);
	if(!display) return;
	int screen = XDefaultScreen(display);
	Window root = XRootWindow(display, screen);
	int width = XDisplayWidth(display, screen);
	int height = XDisplayHeight(display, screen);
	int depth = XDefaultDepth(display, screen);
	Pixmap pixmap = XCreatePixmap(display, root, width, height, depth);
	GC graphics = XCreateGC(display, pixmap, 0, NULL);

	// https://tronche.com/gui/x/xlib/utilities/XCreateImage.html
	XImage *image = XCreateImage(display,
		XDefaultVisual(display, screen),
		depth, ZPixmap, 0,	// ZPixmap from X11/X.h defined as the value 2
		(char*)(*env)->GetByteArrayElements(env, inJNIArray, NULL),
		width, height, 32, 0);
	XPutImage(display, pixmap, graphics, image, 0, 0, 0, 0, width, height);
	XSetWindowBackgroundPixmap(display, root, pixmap);
	XClearWindow(display, root);

	// Unnecessary because XDestroyImage frees image structure and data pointed to by image structure
	//(*env)->ReleaseByteArrayElements(env, inJNIArray, data, 0);
	XFreeGC(display, graphics);
	XDestroyImage(image);
	XFreePixmap(display, pixmap);
	XCloseDisplay(display);
}
