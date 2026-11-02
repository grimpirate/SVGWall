#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
JNIX11="$DIR/src_c"
SRC="$DIR/src_java"
ASSETS="$DIR/assets"
BATIK="$ASSETS/batik-1.20/lib"
APPDIR="$DIR/svgwall.AppDir"
USR="$APPDIR/usr"
LIBS="
rhino-1.9.2
picocli-4.7.7
xmlgraphics-commons-2.12
batik-anim-1.20
batik-bridge-1.20
batik-css-1.20
batik-dom-1.20
batik-gvt-1.20
batik-transcoder-1.20
batik-util-1.20
batik-i18n-1.20
batik-svg-dom-1.20
batik-constants-1.20
batik-ext-1.20
batik-xml-1.20
batik-parser-1.20
batik-script-1.20
batik-awt-util-1.20
xml-apis-ext-1.3.04
"

CLASSPATH=""

# AppDir structure
mkdir -p "$USR/lib"
mkdir -p "$USR/bin"
mkdir -p "$USR/share/metainfo"
mkdir -p "$USR/share/icons/hicolor/256x256/apps"
mkdir -p "$USR/share/icons/hicolor/scalable/apps"

# Needed lib(s)
wget https://archive.apache.org/dist/xmlgraphics/batik/binaries/batik-bin-1.20.tar.gz -P "$ASSETS"
tar -xzvf "$ASSETS/batik-bin-1.20.tar.gz" -C "$ASSETS"

for JAR in $LIBS; do
	CLASSPATH="$CLASSPATH:$USR/lib/$JAR.jar"
	mv "$BATIK/$JAR.jar" "$USR/lib"
done

wget https://github.com/remkop/picocli/releases/download/v4.7.7/picocli-4.7.7.jar -P "$USR/lib"
wget https://repo1.maven.org/maven2/org/mozilla/rhino/1.9.2/rhino-1.9.2.jar -P "$USR/lib"

wget https://download.java.net/java/GA/jdk27.0.1.0.3/b14170c9b2de463d9b89442c6db3e384/1/GPL/openjdk-27.0.1.0.3_linux-x64_bin.tar.gz -P "$ASSETS"
tar -xzvf "$ASSETS/openjdk-27.0.1.0.3_linux-x64_bin.tar.gz" -C "$ASSETS"

# Compile Java
"$ASSETS/jdk-27.0.1.0.3/bin/javac" \
	-cp "$SRC$CLASSPATH" \
	-d "$USR/bin" \
	"$SRC/com/grimpirate/SVGWall.java"

# Superseded by Java FFM X11WallpaperManager
# Compile Position Independent Executable
#cc \
#	-fPIE \
#	-o "$USR/bin/x11root" \
#	"$JNIX11/x11root.c" \
#	-lX11
#chmod 0755 "$USR/bin/x11root"

# Embedded JRE
"$ASSETS/jdk-27.0.1.0.3/bin/jlink" \
	--no-header-files \
	--no-man-pages \
	--strip-debug \
	--add-modules java.desktop,jdk.xml.dom,jdk.dynalink \
	--output "$USR/bin/jre"
rm -rf "$USR/bin/jre/legal" "$USR/bin/jre/release" "$USR/bin/jre/bin/keytool"
mv "$USR/bin/jre/bin/java" "$USR/bin/jre/bin/svgwall"

# Finalize AppDir
mv "$ASSETS/AppRun" "$USR/bin"
chmod 0755 "$USR/bin/AppRun"
mv "$ASSETS/svgwall.desktop" "$APPDIR"
chmod 0755 "$APPDIR/svgwall.desktop"
mv "$ASSETS/com.grimpirate.svgwall.appdata.xml" "$USR/share/metainfo"
mv "$ASSETS/svgwall.png" "$USR/share/icons/hicolor/256x256/apps"
mv "$ASSETS/svgwall.svg" "$USR/share/icons/hicolor/scalable/apps"
ln -sT "usr/bin/AppRun" "$APPDIR/AppRun"
ln -sT "usr/share/icons/hicolor/256x256/apps/svgwall.png" "$APPDIR/.DirIcon"
ln -sT "usr/share/icons/hicolor/scalable/apps/svgwall.svg" "$APPDIR/svgwall.svg"
wget https://github.com/AppImageCommunity/AppImageUpdate/releases/latest/download/appimageupdatetool-x86_64.AppImage -P "$USR/bin"
chmod 0755 "$USR/bin/appimageupdatetool-x86_64.AppImage"
wget  https://github.com/AppImage/appimagetool/releases/latest/download/appimagetool-x86_64.AppImage -P "$ASSETS"
chmod 0755 "$ASSETS/appimagetool-x86_64.AppImage"

# Package AppImage
"$ASSETS/appimagetool-x86_64.AppImage" --appimage-extract-and-run "$APPDIR" -u "gh-releases-zsync|grimpirate|SVGWall|latest|SVGWall-*x86_64.AppImage.zsync"

# Cleanup
rm -rf "$APPDIR" "$ASSETS" "$JNIX11" "$SRC" screenshot.png README.md LICENSE build.sh

# ldd jre/bin/java
