# SVGWall
SVG Wallpaper Utility for Xorg

    Usage: SVGWall-x86_64.AppImage [-chuV] -j=FILE
      -c, --check             Check for SVGWall-x86_64.AppImage update(s) and exit.
      -h, --help              Show this help message and exit.
      -j, --javascript=FILE   SVG generator JavaScript file
      -u, --update            Update SVGWall-x86_64.AppImage and exit.
      -V, --version           Print version information and exit.


![Screenshot](https://raw.githubusercontent.com/grimpirate/SVGWall/main/screenshot.png "SVGWall")

### Compile & Package from source
Requires a JDK

    git clone https://github.com/grimpirate/SVGWall
    cd SVGWall
    chmod 0755 build.sh
    ./build.sh
