#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
JNIX11="$DIR/src_c"
SRC="$DIR/src_java"
APPIMAGE="$DIR/appimage"
BATIK="$APPIMAGE/batik-1.17/lib"
APPDIR="$DIR/svgwall.AppDir"
USR="$APPDIR/usr"

# AppDir structure
mkdir -p "$USR/lib"
mkdir -p "$USR/bin"
mkdir -p "$USR/share/metainfo"
mkdir -p "$USR/share/icons/hicolor/256x256/apps"
mkdir -p "$USR/share/icons/hicolor/scalable/apps"

# Needed lib(s)
wget https://dlcdn.apache.org/xmlgraphics/batik/binaries/batik-bin-1.17.tar.gz -P "$DIR/appimage"
tar -xzf "$DIR/appimage/batik-bin-1.17.tar.gz" -C "$DIR/appimage"
mv "$BATIK/batik-anim-1.17.jar" "$USR/lib"
mv "$BATIK/batik-awt-util-1.17.jar" "$USR/lib"
mv "$BATIK/batik-bridge-1.17.jar" "$USR/lib"
mv "$BATIK/batik-constants-1.17.jar" "$USR/lib"
mv "$BATIK/batik-css-1.17.jar" "$USR/lib"
mv "$BATIK/batik-dom-1.17.jar" "$USR/lib"
mv "$BATIK/batik-ext-1.17.jar" "$USR/lib"
mv "$BATIK/batik-gvt-1.17.jar" "$USR/lib"
mv "$BATIK/batik-i18n-1.17.jar" "$USR/lib"
mv "$BATIK/batik-parser-1.17.jar" "$USR/lib"
mv "$BATIK/batik-script-1.17.jar" "$USR/lib"
mv "$BATIK/batik-svg-dom-1.17.jar" "$USR/lib"
mv "$BATIK/batik-transcoder-1.17.jar" "$USR/lib"
mv "$BATIK/batik-util-1.17.jar" "$USR/lib"
mv "$BATIK/batik-xml-1.17.jar" "$USR/lib"
mv "$BATIK/xml-apis-ext-1.3.04.jar" "$USR/lib"
mv "$BATIK/xmlgraphics-commons-2.9.jar" "$USR/lib"
wget https://github.com/remkop/picocli/releases/download/v4.7.6/picocli-4.7.6.jar -P "$USR/lib"

# Compile Java
javac -cp "$SRC:$USR/lib/batik-anim-1.17.jar:$USR/lib/batik-awt-util-1.17.jar:$USR/lib/batik-bridge-1.17.jar:$USR/lib/batik-constants-1.17.jar:$USR/lib/batik-css-1.17.jar:$USR/lib/batik-dom-1.17.jar:$USR/lib/batik-ext-1.17.jar:$USR/lib/batik-gvt-1.17.jar:$USR/lib/batik-i18n-1.17.jar:$USR/lib/batik-parser-1.17.jar:$USR/lib/batik-script-1.17.jar:$USR/lib/batik-svg-dom-1.17.jar:$USR/lib/batik-transcoder-1.17.jar:$USR/lib/batik-util-1.17.jar:$USR/lib/batik-xml-1.17.jar:$USR/lib/xml-apis-ext-1.3.04.jar:$USR/lib/xmlgraphics-commons-2.9.jar:$USR/lib/picocli-4.7.6.jar" -h "$JNIX11" -d "$USR/bin" "$SRC/com/grimpirate/SVGWall.java"

# Compile native library
cc -fPIC -I/usr/lib/jvm/java-22-openjdk/include/ -I/usr/lib/jvm/java-22-openjdk/include/linux/ -lX11 -shared -o "$USR/lib/libjnix11.so" "$JNIX11/com_grimpirate_SVGWall.c"
chmod 0644 "$USR/lib/libjnix11.so"

# Embedded JRE
jlink --no-header-files --no-man-pages --strip-debug --compress=zip-9 --add-modules java.desktop,jdk.xml.dom --output "$USR/bin/jre"
rm -rf "$USR/bin/jre/legal" "$USR/bin/jre/release" "$USR-bin/jre/bin/keytool"

# Finalize AppDir
mv "$APPIMAGE/AppRun" "$USR/bin"
chmod 0755 "$USR/bin/AppRun"
mv "$APPIMAGE/svgwall.desktop" "$APPDIR"
chmod 0755 "$APPDIR/svgwall.desktop"
mv "$APPIMAGE/svgwall.appdata.xml" "$USR/share/metainfo"
mv "$APPIMAGE/svgwall.png" "$USR/share/icons/hicolor/256x256/apps"
mv "$APPIMAGE/svgwall.svg" "$USR/share/icons/hicolor/scalable/apps"
ln -sT "usr/bin/AppRun" "$APPDIR/AppRun"
ln -sT "usr/share/icons/hicolor/256x256/apps/svgwall.png" "$APPDIR/.DirIcon"
ln -sT "usr/share/icons/hicolor/scalable/apps/svgwall.svg" "$APPDIR/svgwall.svg"
wget https://github.com/AppImageCommunity/AppImageUpdate/releases/latest/download/appimageupdatetool-x86_64.AppImage -P "$USR/bin"
chmod 0755 "$USR/bin/appimageupdatetool-x86_64.AppImage"
wget https://github.com/AppImage/AppImageKit/releases/latest/download/appimagetool-x86_64.AppImage -P "$APPIMAGE"
chmod 0755 "$APPIMAGE/appimagetool-x86_64.AppImage"

# Package AppImage
"$APPIMAGE/appimagetool-x86_64.AppImage" "$APPDIR" -u "gh-releases-zsync|grimpirate|SVGWall|latest|SVGWall-*x86_64.AppImage.zsync"

# Cleanup
rm -rf "$APPDIR" "$APPIMAGE" "$JNIX11" "$SRC" screenshot.png README.md LICENSE build.sh

# Test SVGs
# https://gist.githubusercontent.com/vschmidt94/7ae2c23fede9f53bf63da4d7ace5fc14/raw/e41ed2bd565a54e90b33209dc820086e93121ab5/retro_gruvbox_linux_wallpaper.svg
# https://upload.wikimedia.org/wikipedia/commons/7/74/Debian_wallpaper.svg
# https://user-images.githubusercontent.com/510119/187742819-37e8f777-ee2c-4a3c-bfb7-719c89fe1a8a.svg
# https://upload.wikimedia.org/wikipedia/commons/0/05/Lcars_wallpaper.svg
# https://fa.ml.com/new-yorknew-york/new-york/The-Elliott-Group/mediahandler/media/240015/Default_Wealth_Mobile_Hero_Background.svg
# https://xmple.com/wallpaper/yellow-gradient-red-linear-3840x2160-c2-feeb10-fe4010-a-240-f-14.svg
# https://raw.githubusercontent.com/pablocorbalann/arch-minimal-wallpapers/main/other/template.svg
# https://raw.githubusercontent.com/connorslade/ArchPapers/main/data/arch.svg
# https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/0_pink_planet_dust/2_pink_dust_dark.svg
# https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/2_eye_of_the_universe/eye.svg
# https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/3_stroke_pattern/1_pink_pattern_darker.svg
# https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/4_code/code.svg

# ldd jre/bin/java
