import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class X11WallpaperManager {

	// Define X11 Constants
	private static final int ZPixmap = 2;
	private static final int False = 0;
	private static final int TrueColor = 4;
	private static final int PropModeReplace = 0;
	private static final long XA_PIXMAP = 20; 
	private static final int RetainPermanent = 1; 

	// Define static MethodHandles for native symbols
	private static final MethodHandle XOpenDisplay;
	private static final MethodHandle XDefaultScreen;
	private static final MethodHandle XRootWindow;
	private static final MethodHandle XDisplayWidth;
	private static final MethodHandle XDisplayHeight;
	private static final MethodHandle XDefaultDepth;
	private static final MethodHandle XDefaultVisual;
	private static final MethodHandle XMatchVisualInfo;
	private static final MethodHandle XCreateImage;
	private static final MethodHandle XCreatePixmap;
	private static final MethodHandle XCreateGC;
	private static final MethodHandle XInternAtom;
	private static final MethodHandle XChangeProperty;
	private static final MethodHandle XSetWindowBackgroundPixmap;
	private static final MethodHandle XClearWindow;
	private static final MethodHandle XPutImage;
	private static final MethodHandle XFlush;
	private static final MethodHandle XFreeGC;
	private static final MethodHandle XCloseDisplay;
	private static final MethodHandle XSetCloseDownMode;

	// Resolve native hooks once at class-load time
	static {
		Linker linker = Linker.nativeLinker();
		SymbolLookup x11 = SymbolLookup.libraryLookup("libX11.so.6", Arena.global());

		XOpenDisplay = lookup(linker, x11, "XOpenDisplay", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
		XDefaultScreen = lookup(linker, x11, "XDefaultScreen", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
		XRootWindow = lookup(linker, x11, "XRootWindow", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		XDisplayWidth = lookup(linker, x11, "XDisplayWidth", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		XDisplayHeight = lookup(linker, x11, "XDisplayHeight", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		XDefaultDepth = lookup(linker, x11, "XDefaultDepth", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		XDefaultVisual = lookup(linker, x11, "XDefaultVisual", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		
		XMatchVisualInfo = lookup(linker, x11, "XMatchVisualInfo", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
		XCreateImage = lookup(linker, x11, "XCreateImage", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
		XCreatePixmap = lookup(linker, x11, "XCreatePixmap", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
		XCreateGC = lookup(linker, x11, "XCreateGC", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
		XInternAtom = lookup(linker, x11, "XInternAtom", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		
		XChangeProperty = lookup(linker, x11, "XChangeProperty", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
		XSetWindowBackgroundPixmap = lookup(linker, x11, "XSetWindowBackgroundPixmap", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
		XClearWindow = lookup(linker, x11, "XClearWindow", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
		XPutImage = lookup(linker, x11, "XPutImage", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
		XFlush = lookup(linker, x11, "XFlush", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
		XFreeGC = lookup(linker, x11, "XFreeGC", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
		XCloseDisplay = lookup(linker, x11, "XCloseDisplay", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
		XSetCloseDownMode = lookup(linker, x11, "XSetCloseDownMode", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
	}

	/**
	 * Updates the X11 desktop background with the provided raw BGRA byte payload.
	 * 
	 * @param rawInput Byte array containing raw sequential BGRA image pixels.
	 * @throws IllegalStateException If the connection to the active X server cannot be initiated.
	 */
	public void setWallpaper(byte[] rawInput) {
		Arena globalArena = Arena.global();

		try {
			// Open Display
			MemorySegment display = (MemorySegment) XOpenDisplay.invokeExact(MemorySegment.NULL);
			if (display.equals(MemorySegment.NULL)) {
				throw new IllegalStateException("Failed to open connection to active X11 display server.");
			}

			int screen = (int) XDefaultScreen.invokeExact(display);
			MemorySegment root = (MemorySegment) XRootWindow.invokeExact(display, screen);
			if (root.equals(MemorySegment.NULL)) {
				XCloseDisplay.invokeExact(display);
				throw new IllegalStateException("Failed to target X11 Root Window context.");
			}

			int width = (int) XDisplayWidth.invokeExact(display, screen);
			int height = (int) XDisplayHeight.invokeExact(display, screen);
			int depth = (int) XDefaultDepth.invokeExact(display, screen);
			
			MemorySegment vInfo = globalArena.allocate(64);
			MemorySegment visual;

			if ((int) XMatchVisualInfo.invokeExact(display, screen, depth, TrueColor, vInfo) != 0) {
				visual = vInfo.get(ValueLayout.ADDRESS, 0);
			} else {
				visual = (MemorySegment) XDefaultVisual.invokeExact(display, screen);
			}

			int bytesPerPixel = 4;
			int quantum = 32;
			int expectedBufferSize = width * height * bytesPerPixel;

			// Optional safety boundary validation check
			if (rawInput.length < expectedBufferSize) {
				XCloseDisplay.invokeExact(display);
				throw new IllegalArgumentException("Input byte buffer size (" + rawInput.length + 
						") does not meet requirements for display dimensions: " + width + "x" + height + " BGRA (" + expectedBufferSize + " bytes).");
			}

			// Transfer payload to native allocation space
			MemorySegment dataBuffer = globalArena.allocate(expectedBufferSize);
			MemorySegment.copy(rawInput, 0, dataBuffer, ValueLayout.JAVA_BYTE, 0, expectedBufferSize);

			// Provision image pointers natively
			MemorySegment image = (MemorySegment) XCreateImage.invokeExact(
				display, visual, depth, ZPixmap, 0, dataBuffer, width, height, quantum, width * bytesPerPixel
			);

			if (image.equals(MemorySegment.NULL)) {
				XCloseDisplay.invokeExact(display);
				throw new IllegalStateException("Native XCreateImage structural setup failed.");
			}

			// Allocate Pixmap and Graphical Drawing Context hooks
			MemorySegment pixmap = (MemorySegment) XCreatePixmap.invokeExact(display, root, width, height, depth);
			MemorySegment gc = (MemorySegment) XCreateGC.invokeExact(display, pixmap, 0L, MemorySegment.NULL);

			// Blit pixels directly over server pixmap buffer region
			XPutImage.invokeExact(display, pixmap, gc, image, 0, 0, 0, 0, width, height);

			// Configure target property mappings to preserve layout configurations across compositors
			MemorySegment rootPmapStr = globalArena.allocateFrom("_XROOTPMAP_ID");
			MemorySegment esetrootStr = globalArena.allocateFrom("ESETROOT_PMAP_ID");

			MemorySegment propRootPmap = (MemorySegment) XInternAtom.invokeExact(display, rootPmapStr, False);
			MemorySegment propEsetroot = (MemorySegment) XInternAtom.invokeExact(display, esetrootStr, False);

			MemorySegment pixmapRef = globalArena.allocate(ValueLayout.ADDRESS);
			pixmapRef.set(ValueLayout.ADDRESS, 0, pixmap);

			XChangeProperty.invokeExact(display, root, propRootPmap, MemorySegment.ofAddress(XA_PIXMAP), 32, PropModeReplace, pixmapRef, 1);
			XChangeProperty.invokeExact(display, root, propEsetroot, MemorySegment.ofAddress(XA_PIXMAP), 32, PropModeReplace, pixmapRef, 1);

			// Execute changes natively
			XSetWindowBackgroundPixmap.invokeExact(display, root, pixmap);
			XClearWindow.invokeExact(display, root);
			
			// Retain the resource on the server context before cutting the thread link pipeline
			XSetCloseDownMode.invokeExact(display, RetainPermanent);
			XFlush.invokeExact(display);

			// Graceful pipeline teardown
			XFreeGC.invokeExact(display, gc);
			XCloseDisplay.invokeExact(display);

		} catch (Throwable t) {
			throw new RuntimeException("Unexpected exception encountered during native X11 interaction execution.", t);
		}
	}

	private static MethodHandle lookup(Linker linker, SymbolLookup lookup, String name, FunctionDescriptor desc) {
		return lookup.find(name)
		.map(symbol -> linker.downcallHandle(symbol, desc))
		.orElseThrow(() -> new NoSuchMethodError("Native symbol missing: " + name));
	}
}