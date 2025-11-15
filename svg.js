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

/*
 * FONTS
 * JetBrains Mono: https://www.jetbrains.com/lp/mono/
 * Anurati Regular: https://troisieme-type.com/anurati-pro
 * Poppins: https://fonts.google.com/specimen/Poppins
 */

function lerp(from, to, time)
{
	from = from.match(/[^#]{2}/g).map(c => parseInt(c, 16));
	to = to.match(/[^#]{2}/g).map(c => parseInt(c, 16));
	tween = [];
	
	for(let c = 0; c < 3; c++)
		tween[c] = Math.round(from[c] + (to[c] - from[c]) * time);

	return `#${tween.map(c => c.toString(16).padStart(2, '0')).join('')}`;
}

/*
 * Configuration
 */

const MARGIN = Platform.height / 200.0 * 3;
const LINESPACE = Platform.height / 45.0;
const used = '32G';
const available = '925G';
const percent = 4/100.0;
const battery = 79;
const colors = {
	dark: '#1d2021',
	green : '#689e6a',
	blue: '#468488',
	yellow: '#d89920',
	red: '#cc241d',
	light: '#eadcb2',
};
const fonts = {
	standard : `font-size: ${Platform.height / 70.0}pt; font-family: 'Poppins';`,
	title: `font-size: ${Platform.height / 15.0}pt; font-family: 'Anurati';`,
};

/*
 * Empty SVG
 */

const svg = SVG.blank();
const viewBox = `0 0 ${Platform.width} ${Platform.height}`.match(/[\d\.]+/gi).map(parseFloat);
svg.setAttribute('viewBox', viewBox.join(' '));

/*
 * Solid color backdrop
 */

svg.appendChild(SVG.element('rect', {
	x: 0,
	y: 0,
	width: Platform.width,
	height: Platform.height,
	fill: colors.dark,
}));

/*
 * Diagonal bands
 */

const offset = Platform.height * 0.035;
const band_colors = [
	colors.red,
	colors.yellow,
	colors.blue,
	colors.green,
];
for(let i = 2; i > -2; i++)
	svg.appendChild(SVG.element('rect', {
		x: 0,
		y: Platform.height * 0.5 - i * offset,
		width: Platform.width,
		height: offset,
		fill: band_colors[i + 1],
		transform: `rotate(-45 ${Platform. width * 0.8} ${Platform.height * 0.25})`,
	}));

/*
 * Raster background
 */

const image = SVG.element('image', {
	width: Platform.width,
	height: Platform.height,
	preserveAspectRation: 'xMidYMid slice',
});
image.setAttributeNS('http://www.w3.org/1999/xlink', 'href', 'https://raw.githubusercontent.com/ItsTerm1n4l/Wallpapers-old-archive/refs/heads/main/Fantasy/Small-memory-sunrise.jpg');
svg.appendChild(image);

/*
 * Battery percentage
 */

const horizontal = SVG.element('g', {
	transform: `translate(${Platform.width * 0.15} ${Platform.height * 0.75})`,
});
horizontal.appendChild(SVG.element('line', {
	x1: -40,
	y1: 0,
	x2: 40,
	y2: 0,
	fill: 'none',
	stroke: 'white',
	'stroke-opacity': 0.2,
	'stroke-linecap': 'butt',
	'stroke-width': 17,
	'stroke-dasharray': `80 80`,
}));
horizontal.appendChild(SVG.element('line', {
	x1: -40,
	y1: 0,
	x2: 40,
	y2: 0,
	fill: 'none',
	stroke: battery / 100.0 < 0.5 ? lerp(colors.red, colors.yellow, battery / 50.0) : lerp(colors.yellow, colors.green, battery / 50.0 - 1),
	'stroke-linecap': 'butt',
	'stroke-width': 17,
	'stroke-dasharray': `${0.8 * battery} 80`,
}));
horizontal.appendChild(SVG.element('text', {
	x: 0,
	y: '2em',
	'text-anchor': 'middle',
	fill: colors.light,
	style: fonts.standard,
}, `⚡ ${battery}%`));
svg.appendChild(horizontal);

/*
 * Drive space
 */

const gauge = SVG.element('g', {
	transform: `translate(${Platform.width * 0.15} ${Platform.height * 0.65})`,
});
gauge.appendChild(SVG.element('circle', {
	cx: 0,
	cy: 0,
	r: 42.5,
	fill: 'none',
	stroke: 'white',
	'stroke-width': 15,
	'stroke-linecap': 'butt',
	'stroke-dasharray': '200 267',
	'stroke-opacity': 0.2,
	transform: 'rotate(135, 0, 0)',
}));
gauge.appendChild(SVG.element('circle', {
	cx: 0,
	cy: 0,
	r: 42.5,
	fill: 'none',
	stroke: percent < 0.5 ? lerp(colors.green, colors.yellow, 2 * percent) : lerp(colors.yellow, colors.red, 2 * percent - 1),
	'stroke-width': 15,
	'stroke-linecap': 'butt',
	'stroke-dasharray': `${percent * 200} 267`,
	transform: 'rotate(135, 0, 0)',
}));
const text = SVG.element('text', {
	x: 0,
	y: 0,
	fill: colors.light,
	style: fonts.standard,
	'text-anchor': 'middle',
}, `${used} / ${available}`);
/*text.appendChild(SVG.element('tspan', {
	x: 0,
	y: '-0.5em',
}, `Used: ${used}`));
text.appendChild(SVG.element('tspan', {
	x: 0,
	y: '0.5em',
}, `Available: ${available}`));*/
gauge.appendChild(text);
svg.appendChild(gauge);

/*
 * Right text
 */

const right_texts = [
	Platform.jvm,
	Platform.version,
].reverse();
for(let i = 0; i < right_texts.length; i++)
	svg.appendChild(SVG.element('text', {
		x: Platform.width - MARGIN,
		y: Platform.height - MARGIN - i * LINESPACE,
		'text-anchor': 'end',
		fill: colors.light,
		style: fonts.standard,
	}, right_texts[i]));

/*
 * Left text
 */

const left_texts = [
	Platform.os_arch,
	Platform.os_ver,
	`${Platform.network.getNetworkInterface()} - ${Platform.network.getInetAddress()}`,
].reverse();
for(let i = 0; i < left_texts.length; i++)
	svg.appendChild(SVG.element('text', {
		x: MARGIN,
		y: Platform.height - MARGIN - i * LINESPACE,
		fill: colors.light,
		style: fonts.standard,
	}, left_texts[i]));

/*
 * Resist
 */

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
	stroke: colors.light,
	'stroke-linejoin': 'round',
	'stroke-width': 13,
}));
resize.appendChild(SVG.element('circle', {
	cx: 300,
	cy: 295,
	r: 170,
	fill: 'none',
	'stroke-width': 20,
	stroke: colors.light,
}));
svg.appendChild(logo);

/*
 * Day of week
 */

const delta = Platform.height * 0.005;
const delta_colors = [
	colors.light,
	colors.green,
	colors.blue,
	colors.yellow,
	colors.red,
];
for(let i = delta_colors.length - 1; i > -1; i--)
	svg.appendChild(SVG.element('text', {
		x: Platform.width * 0.5 + i * delta,
		y: Platform.height * 0.15,
		'text-anchor': 'middle',
		fill: delta_colors[i],
		style: fonts.title,
	}, Platform.chrono.ofPattern('cccc').toUpperCase()));

/*
 * Date and time
 */

svg.appendChild(SVG.element('text', {
	x: '50%',
	y: Platform.height * 0.15 + Platform.height * 0.05,
	'text-anchor': 'middle',
	fill: colors.light,
	style: `font-size: ${Platform.height / 40.0}pt; font-family: 'JetBrains Mono Medium';`,
}, Platform.chrono.ofPattern('yyyy LLL d').toUpperCase()));
svg.appendChild(SVG.element('text', {
	x: '50%',
	y: Platform.height * 0.15 + Platform.height * 0.09,
	'text-anchor': 'middle',
	fill: colors.light,
	style: `font-size: ${Platform.height / 50.0}pt; font-family: 'JetBrains Mono Medium';`,
}, Platform.chrono.ofPattern('· HH:mm ·').toUpperCase()));