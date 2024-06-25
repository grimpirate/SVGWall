importPackage(Packages.com.grimpirate);

const MARGIN = 10;
[
	////////////////////////////////
	// MAKE EDITS BELOW THIS LINE //
	////////////////////////////////

	{
		x: MARGIN,
		y: 0,
		t: SVGWall.jvm,
		a: Anchor.LEFT
	},{
		x: MARGIN,
		y: -18,
		t: SVGWall.os_ver,
		a: Anchor.LEFT
	},{
		x: MARGIN,
		y: - 2 * 18,
		t: SVGWall.os_arch,
		a: Anchor.LEFT
	},{
		x: SVGWall.width * 0.5,
		y: 0,
		t: '--grimpirate.com--',
		a: Anchor.CENTER
	},{
		x: SVGWall.width - MARGIN,
		y: 0,
		t: SVGWall.time,
		a: Anchor.RIGHT
	},{
		x: SVGWall.width - MARGIN,
		y: -18,
		t: SVGWall.version,
		a: Anchor.RIGHT
	},

	//////////////////////////////
	// NO EDITS BELOW THIS LINE //
	//////////////////////////////
]
.map(a => 'a' in a ? new CoordinateText(a.x, SVGWall.height - MARGIN + a.y, a.t, a.a) : new CoordinateText(SVGWall.height - MARGIN + a.x, a.y, a.t));
