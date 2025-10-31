//const svg = SVG.load('https://upload.wikimedia.org/wikipedia/commons/7/74/Debian_wallpaper.svg');
//const svg = SVG.load('https://user-images.githubusercontent.com/510119/187742819-37e8f777-ee2c-4a3c-bfb7-719c89fe1a8a.svg');
//const svg = SVG.load('https://upload.wikimedia.org/wikipedia/commons/0/05/Lcars_wallpaper.svg');
//const svg = SVG.load('https://xmple.com/wallpaper/yellow-gradient-red-linear-3840x2160-c2-feeb10-fe4010-a-240-f-14.svg');
//const svg = SVG.load('https://raw.githubusercontent.com/pablocorbalann/arch-minimal-wallpapers/main/other/template.svg');
//const svg = SVG.load('https://raw.githubusercontent.com/connorslade/ArchPapers/main/data/arch.svg');
//const svg = SVG.load('https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/0_pink_planet_dust/2_pink_dust_dark.svg');
//const svg = SVG.load('https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/2_eye_of_the_universe/eye.svg');
//const svg = SVG.load('https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/3_stroke_pattern/1_pink_pattern_darker.svg');
//const svg = SVG.load('https://raw.githubusercontent.com/Mikluki/awesome_svg_wallpaper/04055c39e2941d54e9753e09afe8277552f93878/4_code/code.svg');
const svg = SVG.load('https://gist.githubusercontent.com/vschmidt94/7ae2c23fede9f53bf63da4d7ace5fc14/raw/e41ed2bd565a54e90b33209dc820086e93121ab5/retro_gruvbox_linux_wallpaper.svg');
const MARGIN = 3;
const LINESPACE = 5;
const viewBox = svg.getAttribute('viewBox').match(/[\d\.]+/gi).map(parseFloat);
const width = viewBox[2] ?? 0;
const height = viewBox[3] ?? 0;
svg.appendChild(SVG.element('style', null, `
text
{
	fill: white;
	font-size: 4pt;
	font-family: monospace;
}
`));
svg.appendChild(SVG.element('text', {
	x: width - MARGIN,
	y: height - MARGIN,
	'text-anchor': 'end'
}, Platform.time));
svg.appendChild(SVG.element('text', {
	x: width - MARGIN,
	y: height - MARGIN - LINESPACE,
	'text-anchor': 'end'
}, Platform.version));
svg.appendChild(SVG.element('text', {
	x: '50%',
	y: height - MARGIN,
	'text-anchor': 'middle'
}, '--grimpirate.com--'));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: height - MARGIN
}, Platform.jvm));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: height - MARGIN - LINESPACE
}, Platform.os_ver));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: height - MARGIN - 2 * LINESPACE
}, Platform.os_arch));
