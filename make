#!/bin/sh

JNIX11="$HOME/xlib_workspace"
BATIK="$JNIX11/batik-1.17/lib"
PICOCLI="$JNIX11/picocli-4.7.6"
SRC="$HOME/eclipse-workspace/SVGWall/src"
APPIMAGE="$HOME/appimage-workspace"
APPDIR="$APPIMAGE/svgwall.AppDir"
USR="$APPDIR/usr"

# AppDir structure
mkdir -p "$USR/lib"
mkdir -p "$USR/bin"
mkdir -p "$USR/share/metainfo"
mkdir -p "$USR/share/icons/hicolor/256x256/apps"
mkdir -p "$USR/share/icons/hicolor/scalable/apps"

# Compile Java
javac -cp "$SRC:$BATIK/batik-anim-1.17.jar:$BATIK/batik-awt-util-1.17.jar:$BATIK/batik-bridge-1.17.jar:$BATIK/batik-constants-1.17.jar:$BATIK/batik-css-1.17.jar:$BATIK/batik-dom-1.17.jar:$BATIK/batik-ext-1.17.jar:$BATIK/batik-gvt-1.17.jar:$BATIK/batik-i18n-1.17.jar:$BATIK/batik-parser-1.17.jar:$BATIK/batik-script-1.17.jar:$BATIK/batik-svg-dom-1.17.jar:$BATIK/batik-transcoder-1.17.jar:$BATIK/batik-util-1.17.jar:$BATIK/batik-xml-1.17.jar:$BATIK/xml-apis-ext-1.3.04.jar:$BATIK/xmlgraphics-commons-2.9.jar:$PICOCLI/picocli-4.7.6.jar" -h "$JNIX11" -d "$USR/bin" "$SRC/com/grimpirate/SVGWall.java"

# Compile native library
cc -fPIC -I/usr/lib/jvm/java-22-openjdk/include/ -I/usr/lib/jvm/java-22-openjdk/include/linux/ -lX11 -shared -o "$JNIX11/libjnix11.so" "$JNIX11/com_grimpirate_SVGWall.c"

# Needed lib(s)
install -m 0644 "$JNIX11/libjnix11.so" "$USR/lib/"
cp "$BATIK/batik-anim-1.17.jar" "$USR/lib"
cp "$BATIK/batik-awt-util-1.17.jar" "$USR/lib"
cp "$BATIK/batik-bridge-1.17.jar" "$USR/lib"
cp "$BATIK/batik-constants-1.17.jar" "$USR/lib"
cp "$BATIK/batik-css-1.17.jar" "$USR/lib"
cp "$BATIK/batik-dom-1.17.jar" "$USR/lib"
cp "$BATIK/batik-ext-1.17.jar" "$USR/lib"
cp "$BATIK/batik-gvt-1.17.jar" "$USR/lib"
cp "$BATIK/batik-i18n-1.17.jar" "$USR/lib"
cp "$BATIK/batik-parser-1.17.jar" "$USR/lib"
cp "$BATIK/batik-script-1.17.jar" "$USR/lib"
cp "$BATIK/batik-svg-dom-1.17.jar" "$USR/lib"
cp "$BATIK/batik-transcoder-1.17.jar" "$USR/lib"
cp "$BATIK/batik-util-1.17.jar" "$USR/lib"
cp "$BATIK/batik-xml-1.17.jar" "$USR/lib"
cp "$BATIK/xml-apis-ext-1.3.04.jar" "$USR/lib"
cp "$BATIK/xmlgraphics-commons-2.9.jar" "$USR/lib"
cp "$PICOCLI/picocli-4.7.6.jar" "$USR/lib"

# Finalize AppDir
"$APPIMAGE/assets/appimageupdatetool-x86_64.AppImage" --self-update
"$APPIMAGE/assets/appimageupdatetool-x86_64.AppImage" -r "$APPIMAGE/assets/appimagetool-x86_64.AppImage"
cp "$APPIMAGE/assets/appimageupdatetool-x86_64.AppImage" "$USR/bin"
cp -R "$APPIMAGE/assets/jre" "$USR/bin"
cp "$APPIMAGE/assets/AppRun" "$USR/bin"
cp "$APPIMAGE/assets/svgwall.desktop" "$APPDIR"
cp "$APPIMAGE/assets/svgwall.appdata.xml" "$USR/share/metainfo"
cp "$APPIMAGE/assets/svgwall.png" "$USR/share/icons/hicolor/256x256/apps"
cp "$APPIMAGE/assets/svgwall.svg" "$USR/share/icons/hicolor/scalable/apps"
ln -sT "usr/bin/AppRun" "$APPDIR/AppRun"
ln -sT "usr/share/icons/hicolor/256x256/apps/svgwall.png" "$APPDIR/.DirIcon"
ln -sT "usr/share/icons/hicolor/scalable/apps/svgwall.svg" "$APPDIR/svgwall.svg"

# Package AppImage
"$APPIMAGE/assets/appimagetool-x86_64.AppImage" "$APPDIR" -u "gh-releases-zsync|grimpirate|SVGWall|latest|SVGWall-*x86_64.AppImage.zsync"

# Cleanup
rm -rf "$APPDIR"

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

# Embedded JRE
# jlink --no-header-files --no-man-pages --strip-debug --compress=zip-9 --add-modules java.desktop,jdk.xml.dom --output jre
# ldd jre/bin/java
