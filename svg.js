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
//const svg = SVG.load('https://gist.githubusercontent.com/vschmidt94/7ae2c23fede9f53bf63da4d7ace5fc14/raw/e41ed2bd565a54e90b33209dc820086e93121ab5/retro_gruvbox_linux_wallpaper.svg');
const viewBox = `0 0 ${Platform.width} ${Platform.height}`.match(/[\d\.]+/gi).map(parseFloat);
const MARGIN = Platform.height / 200.0 * 3;
const LINESPACE = Platform.height / 45.0;
const svg = SVG.blank();
const used = '31G';
const available = '926G';
const percent = 4/100.0;
const battery = 79;
svg.setAttribute('viewBox', viewBox.join(' '));
svg.appendChild(SVG.element('style', null, `
rect
{
	fill: black;
}
text
{
	fill: white;
	font-size: ${Platform.height / 70.0}pt;
	font-family: monospace;
}
.underlay
{
	fill: none;
	stroke-width: 0.3;
	stroke-linecap: butt;
	stroke-dasharray: 4.0 5.34;
	stroke: white;
	stroke-opacity: 0.2;
}
.overlay
{
	fill: none;
	stroke-width: 0.3;
	stroke-linecap: butt;
	stroke-dasharray: ${percent * 4.0} 5.34;
	stroke: cornflowerblue;
}
`));
svg.appendChild(SVG.element('rect', {
	x: 0,
	y: 0,
	width: Platform.width,
	height: Platform.height,
}, Platform.time));
const horizontal = SVG.element('g', {
	transform: `translate(${Platform.width * 0.5} ${Platform.height * 0.25})`,
});
horizontal.appendChild(SVG.element('line', {
	x1: -1,
	y1: 0,
	x2: 1,
	y2: 0,
	'stroke-dasharray': '2 2',
	'stroke-linecap': 'butt',
	'stroke-width': 0.3,
	fill: 'none',
	stroke: 'white',
	'stroke-opacity': 0.2,
	transform: 'scale(100 100)',
}));
horizontal.appendChild(SVG.element('line', {
	x1: -1,
	y1: 0,
	x2: 1,
	y2: 0,
	'stroke-dasharray': `${0.02 * battery} 2`,
	'stroke-linecap': 'butt',
	'stroke-width': 0.3,
	fill: 'none',
	stroke: 'chartreuse',
	transform: 'scale(100 100)',
}));
horizontal.appendChild(SVG.element('text', {
	x: 0,
	y: '2em',
	'text-anchor': 'middle'
}, `Battery ${battery}%`));
svg.appendChild(horizontal);
const gauge = SVG.element('g', {
	transform: `translate(${Platform.width * 0.5} ${Platform.height * 0.5})`,
});
gauge.appendChild(SVG.element('circle', {
	cx: 0,
	cy: 0,
	r: 0.85,
	class: 'underlay',
	transform: 'scale(100,100) rotate(135, 0, 0)',
}));
gauge.appendChild(SVG.element('circle', {
	cx: 0,
	cy: 0,
	r: 0.85,
	class: 'overlay',
	transform: 'scale(100,100) rotate(135, 0, 0)',
}));
const text = SVG.element('text', {
	x: 0,
	y: 0,
	'text-anchor': 'middle'
});
text.appendChild(SVG.element('tspan', {
	x: 0,
	y: '-0.5em',
}, `Used: ${used}`));
text.appendChild(SVG.element('tspan', {
	x: 0,
	y: '0.5em',
}, `Available: ${available}`));
gauge.appendChild(text);
svg.appendChild(gauge);
svg.appendChild(SVG.element('text', {
	x: Platform.width - MARGIN,
	y: Platform.height - MARGIN,
	'text-anchor': 'end'
}, Platform.time));
svg.appendChild(SVG.element('text', {
	x: Platform.width - MARGIN,
	y: Platform.height - MARGIN - LINESPACE,
	'text-anchor': 'end'
}, Platform.version));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: Platform.height - MARGIN
}, Platform.jvm));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: Platform.height - MARGIN - LINESPACE
}, Platform.os_ver));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: Platform.height - MARGIN - 2 * LINESPACE
}, Platform.os_arch));
const logo = SVG.element('g', {
	transform: `translate(${Platform.width * 0.5} ${Platform.height * 0.96})`,
});
const resize = SVG.element('g', {
	transform: `scale(0.1 0.1) translate(-300, -295) `,
});
logo.appendChild(resize);
resize.appendChild(SVG.element('path', {
	d: 'M476.3 303.55h-40.89l-28.13-65.086-38.442 110.719-47.952-110.702-.024-.017-38.442 110.73-.057-.011-47.851-110.719-38.442 110.72-28.13-64.97h-40.891',
	fill: 'none',
	stroke: 'white',
	'stroke-linejoin': 'round',
	'stroke-width': 13,
}));
resize.appendChild(SVG.element('circle', {
	cx: 300,
	cy: 295,
	r: 170,
	fill: 'none',
	'stroke-width': 20,
	stroke: 'white',
}));
svg.appendChild(logo);