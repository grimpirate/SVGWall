importPackage(Packages.com.grimpirate);
importPackage(java.lang);
importPackage(java.time);
importPackage(java.time.format);

const MARGIN = 10;
[
	////////////////////////////////
	// MAKE EDITS BELOW THIS LINE //
	////////////////////////////////

	{
		x: MARGIN,
		y: 0,
		t: `${s('java.runtime.name')} ${s('java.runtime.version')}`,
		a: Anchor.LEFT
	},{
		x: MARGIN,
		y: -18,
		t: s('os.version'),
		a: Anchor.LEFT
	},{
		x: MARGIN,
		y: - 2 * 18,
		t: `${s('os.name')} ${s('os.arch')}`,
		a: Anchor.LEFT
	},{
		x: SVGWall.width * 0.5,
		y: 0,
		t: '--grimpirate.com--',
		a: Anchor.CENTER
	},{
		x: SVGWall.width - MARGIN,
		y: 0,
		t: t(),
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

function s(p)
{
	return System.getProperty(p);
}

function t()
{
	return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
}
