#ifdef GL_ES
    #define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 iResolution;
uniform float iTime;

const float n = 5.1;
const float s = 0.08;

float Hack( vec2 p, in float s) {
    vec3 p2 = vec3(p.xy,17.3 * abs(sin(s)));
    return fract(sin(dot(p2,vec3(17.3,61.7, 12.4)))*173713.1);
}

float HackNoise(in vec2 p, in float s) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f *= f * (3.0-2.0*f);

    return mix(
        mix(
            Hack(i + vec2(0.,0.), s), Hack(i + vec2(1.,0.), s), f.x
        ),
        mix(
            Hack(i + vec2(0.,1.), s), Hack(i + vec2(1.,1.), s), f.x
        ),
        f.y
    ) * s;
}

float Thunder(vec2 p) {
    float v = -HackNoise(p * 15., 0.5);
    v += HackNoise(p * 1.1, 0.5) - HackNoise(p * 1.1, 0.25);
    v += HackNoise(p * 2.1, 0.25) - HackNoise(p * 2.1, 0.125);
    v += HackNoise(p * 6.1, 0.125) - HackNoise(p * 12.1, 0.0625);
    v += HackNoise(p * 8.1, 0.0625);
    v += HackNoise(p * 16.1, 0.02125);
    return v*8.0;
}

float pd(vec2 p0, vec2 p1) {
    return sqrt((p0.x - p1.x) * (p0.x - p1.x) + (p0.y - p1.y) * (p0.y - p1.y));
}

void main() {
    vec2 uv = (gl_FragCoord.xy / iResolution.xy) * 2.0 - 1.0;
    uv.y *= iResolution.y / iResolution.x;

    vec2 sampP = vec2(gl_FragCoord.xy);
    float t = Thunder(uv + iTime * s);
    t *= sin(iTime * 3.1415926 / 2.0) * 2.0;
    sampP.x += t;
    sampP.y += t;
    vec4 samp = texture2D(u_texture, sampP / iResolution.xy);
    samp += smoothstep(0.0, 8.0, t) * vec4(0.5F, 0.1F, 0.1F, 0.0F);
    samp = clamp(samp, vec4(0.0), vec4(1.0));
    gl_FragColor = samp;
}