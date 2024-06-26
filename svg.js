const MARGIN = 3;
const LINESPACE = 5;
const svg = SVG.load('https://gist.githubusercontent.com/vschmidt94/7ae2c23fede9f53bf63da4d7ace5fc14/raw/e41ed2bd565a54e90b33209dc820086e93121ab5/retro_gruvbox_linux_wallpaper.svg');
svg.appendChild(SVG.element('style', null, `
text
{
	fill: white;
	font-size: 4pt;
	font-family: monospace;
}
`));
svg.appendChild(SVG.element('text', {
	x: 508 - MARGIN,
	y: 285.75 - MARGIN,
	'text-anchor': 'end'
}, Platform.time));
svg.appendChild(SVG.element('text', {
	x: 508 - MARGIN,
	y: 285.75 - MARGIN - LINESPACE,
	'text-anchor': 'end'
}, Platform.version));
svg.appendChild(SVG.element('text', {
	x: '50%',
	y: 285.75 - MARGIN,
	'text-anchor': 'middle'
}, '--grimpirate.com--'));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: 285.75 - MARGIN
}, Platform.jvm));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: 285.75 - MARGIN - LINESPACE
}, Platform.os_ver));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: 285.75 - MARGIN - 2 * LINESPACE
}, Platform.os_arch));
