#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
JNIX11="$DIR/src_c"
SRC="$DIR/src_java"
ASSETS="$DIR/assets"
BATIK="$ASSETS/batik-1.17/lib"
APPDIR="$DIR/svgwall.AppDir"
USR="$APPDIR/usr"
LIBS="
rhino-1.7.15
picocli-4.7.6
xmlgraphics-commons-2.9
batik-anim-1.17
batik-bridge-1.17
batik-css-1.17
batik-dom-1.17
batik-gvt-1.17
batik-transcoder-1.17
batik-util-1.17
batik-i18n-1.17
batik-svg-dom-1.17
batik-constants-1.17
batik-ext-1.17
batik-xml-1.17
batik-parser-1.17
batik-script-1.17
batik-awt-util-1.17
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
wget https://archive.apache.org/dist/xmlgraphics/batik/binaries/batik-bin-1.17.tar.gz -P "$DIR/assets"
tar -xzf "$DIR/assets/batik-bin-1.17.tar.gz" -C "$DIR/assets"

for JAR in $LIBS; do
	CLASSPATH="$CLASSPATH:$USR/lib/$JAR.jar"
	mv "$BATIK/$JAR.jar" "$USR/lib"
done

wget https://github.com/remkop/picocli/releases/download/v4.7.6/picocli-4.7.6.jar -P "$USR/lib"
wget https://github.com/mozilla/rhino/releases/download/Rhino1_7_15_Release/rhino-1.7.15.jar -P "$USR/lib"

# Compile Java
javac \
	-cp "$SRC:$CLASSPATH" \
	-h "$JNIX11" \
	-d "$USR/bin" \
	"$SRC/com/grimpirate/SVGWall.java"

# Compile native library
cc \
	-fPIC \
	-I/usr/lib/jvm/default/include/ \
	-I/usr/lib/jvm/default/include/linux/ \
	-lX11 \
	-shared \
	-o "$USR/lib/libjnix11.so" \
	"$JNIX11/com_grimpirate_SVGWall.c"
chmod 0644 "$USR/lib/libjnix11.so"

# Embedded JRE
jlink \
	--no-header-files \
	--no-man-pages \
	--strip-debug \
	--add-modules java.desktop,jdk.xml.dom \
	--output "$USR/bin/jre"
rm -rf "$USR/bin/jre/legal" "$USR/bin/jre/release" "$USR/bin/jre/bin/keytool"
mv "$USR/bin/jre/bin/java" "$USR/bin/jre/bin/svgwall"

# Finalize AppDir
mv "$ASSETS/AppRun" "$USR/bin"
chmod 0755 "$USR/bin/AppRun"
mv "$ASSETS/svgwall.desktop" "$APPDIR"
chmod 0755 "$APPDIR/svgwall.desktop"
mv "$ASSETS/svgwall.appdata.xml" "$USR/share/metainfo"
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
"$ASSETS/appimagetool-x86_64.AppImage" "$APPDIR" -u "gh-releases-zsync|grimpirate|SVGWall|latest|SVGWall-*x86_64.AppImage.zsync"

# Cleanup
rm -rf "$APPDIR" "$ASSETS" "$JNIX11" "$SRC" screenshot.png README.md LICENSE build.sh

# ldd jre/bin/java
