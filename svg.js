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
 * Requires fonts
 * JetBrains Mono: https://www.jetbrains.com/lp/mono/
 * Anurati Regular: https://troisieme-type.com/anurati-pro
 */

const viewBox = `0 0 ${Platform.width} ${Platform.height}`.match(/[\d\.]+/gi).map(parseFloat);
const MARGIN = Platform.height / 200.0 * 3;
const LINESPACE = Platform.height / 45.0;
const svg = SVG.blank();
const used = '31G';
const available = '926G';
const percent = 4/100.0;
const battery = 79;
const fonts = {
	standard : `fill: #eadcb2; font-size: ${Platform.height / 70.0}pt; font-family: 'JetBrains Mono Medium';`,
};
svg.setAttribute('viewBox', viewBox.join(' '));
const defs = SVG.element('defs');
const gradient = SVG.element('linearGradient', {
	id: 'batteryGradient',
	x1: '0%',
	x2: '100%',
	y1: '0%',
	y2: '0%',
});
gradient.appendChild(SVG.element('stop', {
	offset: '0%',
	'stop-color': '#cc241d',
}));
gradient.appendChild(SVG.element('stop', {
	offset: '50%',
	'stop-color': '#d89920',
}));
gradient.appendChild(SVG.element('stop', {
	offset: '100%',
	'stop-color': '#689e6a',
}));
defs.appendChild(gradient);
const blur = SVG.element('filter', {
	id: 'blur',
});
blur.appendChild(SVG.element('feGaussianBlur', {
	in: 'SourceGraphic',
	stdDeviation: 1,
}));
defs.appendChild(blur);
svg.appendChild(defs);
svg.appendChild(SVG.element('rect', {
	x: 0,
	y: 0,
	width: Platform.width,
	height: Platform.height,
	fill: '#1d2021',
}));
const offset = Platform.height * 0.035;
svg.appendChild(SVG.element('rect', {
	x: 0,
	y: Platform.height * 0.5 - offset - offset,
	width: Platform.width,
	height: offset,
	fill: '#689e6a',
	transform: `rotate(-45 ${Platform. width * 0.8} ${Platform.height * 0.25})`,
}));
svg.appendChild(SVG.element('rect', {
	x: 0,
	y: Platform.height * 0.5 - offset,
	width: Platform.width,
	height: offset,
	fill: '#468488',
	transform: `rotate(-45 ${Platform. width * 0.8} ${Platform.height * 0.25})`,
}));
svg.appendChild(SVG.element('rect', {
	x: 0,
	y: Platform.height * 0.5,
	width: Platform.width,
	height: offset,
	fill: '#d89920',
	transform: `rotate(-45 ${Platform. width * 0.8} ${Platform.height * 0.25})`,
}));
svg.appendChild(SVG.element('rect', {
	x: 0,
	y: Platform.height * 0.5 + offset,
	width: Platform.width,
	height: offset,
	fill: '#cc241d',
	transform: `rotate(-45 ${Platform. width * 0.8} ${Platform.height * 0.25})`,
}));
const image = SVG.element('image', {
	width: Platform.width,
	height: Platform.height,
	preserveAspectRation: 'xMidYMid slice',
});
image.setAttributeNS('http://www.w3.org/1999/xlink', 'href', 'https://raw.githubusercontent.com/ItsTerm1n4l/Wallpapers-old-archive/refs/heads/main/Fantasy/Small-memory-sunrise.jpg');
svg.appendChild(image);

const horizontal = SVG.element('g', {
	transform: `translate(${Platform.width * 0.15} ${Platform.height * 0.75})`,
});
horizontal.appendChild(SVG.element('rect', {
	x: -0.8,
	y: -0.17,
	width: 1.6,
	height: 0.34,
	fill: 'white',
	opacity: 0.2,
	transform: 'scale(50 50)',
}));
horizontal.appendChild(SVG.element('rect', {
	x: -0.8,
	y: -0.17,
	width: 0.016 * battery,
	height: 0.34,
	fill: 'url(#batteryGradient)',
	transform: 'scale(50 50)',
}));
horizontal.appendChild(SVG.element('text', {
	x: 0,
	y: '2em',
	'text-anchor': 'middle',
	style: fonts.standard,
}, `⚡ ${battery}%`));
svg.appendChild(horizontal);
const gauge = SVG.element('g', {
	transform: `translate(${Platform.width * 0.15} ${Platform.height * 0.65})`,
});
gauge.appendChild(SVG.element('circle', {
	cx: 0,
	cy: 0,
	r: 0.85,
	fill: 'none',
	stroke: 'white',
	'stroke-width': 0.3,
	'stroke-linecap': 'butt',
	'stroke-dasharray': '4.0 5.34',
	'stroke-opacity': 0.2,
	transform: 'scale(50,50) rotate(135, 0, 0)',
}));
gauge.appendChild(SVG.element('circle', {
	cx: 0,
	cy: 0,
	r: 0.85,
	fill: 'none',
	stroke: 'url(#batteryGradient)',
	'stroke-width': 0.3,
	'stroke-linecap': 'butt',
	'stroke-dasharray': `${percent * 4.0} 5.34`,
	transform: 'scale(50,50) rotate(135, 0, 0)',
}));
const text = SVG.element('text', {
	x: 0,
	y: 0,
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
svg.appendChild(SVG.element('text', {
	x: Platform.width - MARGIN,
	y: Platform.height - MARGIN,
	'text-anchor': 'end',
	style: fonts.standard,
}, Platform.version));
svg.appendChild(SVG.element('text', {
	x: Platform.width - MARGIN,
	y: Platform.height - MARGIN - LINESPACE,
	'text-anchor': 'end',
	style: fonts.standard,
}, Platform.jvm));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: Platform.height - MARGIN,
	style: fonts.standard,
}, Platform.os_ver));
svg.appendChild(SVG.element('text', {
	x: MARGIN,
	y: Platform.height - MARGIN - LINESPACE,
	style: fonts.standard,
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
	stroke: '#eadcb2',
	'stroke-linejoin': 'round',
	'stroke-width': 13,
}));
resize.appendChild(SVG.element('circle', {
	cx: 300,
	cy: 295,
	r: 170,
	fill: 'none',
	'stroke-width': 20,
	stroke: '#eadcb2',
}));
svg.appendChild(logo);

const delta = Platform.height * 0.005;
svg.appendChild(SVG.element('text', {
	x: Platform.width * 0.5 + 4 * delta,
	y: Platform.height * 0.15,
	'text-anchor': 'middle',
	style: `fill: #cc241d; font-size: ${Platform.height / 15.0}pt; font-family: 'Anurati';`,
}, Platform.chrono.ofPattern('cccc').toUpperCase()));
svg.appendChild(SVG.element('text', {
	x: Platform.width * 0.5 + 3 * delta,
	y: Platform.height * 0.15,
	'text-anchor': 'middle',
	style: `fill: #d89920; font-size: ${Platform.height / 15.0}pt; font-family: 'Anurati';`,
}, Platform.chrono.ofPattern('cccc').toUpperCase()));
svg.appendChild(SVG.element('text', {
	x: Platform.width * 0.5 + 2 * delta,
	y: Platform.height * 0.15,
	'text-anchor': 'middle',
	style: `fill: #468488; font-size: ${Platform.height / 15.0}pt; font-family: 'Anurati';`,
}, Platform.chrono.ofPattern('cccc').toUpperCase()));
svg.appendChild(SVG.element('text', {
	x: Platform.width * 0.5 + delta,
	y: Platform.height * 0.15,
	'text-anchor': 'middle',
	style: `fill: #689e6a; font-size: ${Platform.height / 15.0}pt; font-family: 'Anurati';`,
}, Platform.chrono.ofPattern('cccc').toUpperCase()));
svg.appendChild(SVG.element('text', {
	x: Platform.width * 0.5,
	y: Platform.height * 0.15,
	'text-anchor': 'middle',
	style: `fill: #eadcb2; font-size: ${Platform.height / 15.0}pt; font-family: 'Anurati';`,
}, Platform.chrono.ofPattern('cccc').toUpperCase()));


svg.appendChild(SVG.element('text', {
	x: '50%',
	y: Platform.height * 0.15 + Platform.height * 0.05,
	'text-anchor': 'middle',
	style: `fill: #eadcb2; font-size: ${Platform.height / 40.0}pt; font-family: 'JetBrains Mono';`,
}, Platform.chrono.ofPattern('yyyy LLL d').toUpperCase()));
svg.appendChild(SVG.element('text', {
	x: '50%',
	y: Platform.height * 0.15 + Platform.height * 0.09,
	'text-anchor': 'middle',
	style: `fill: #eadcb2; font-size: ${Platform.height / 50.0}pt; font-family: 'JetBrains Mono';`,
}, Platform.chrono.ofPattern('— HH:mm —').toUpperCase()));
