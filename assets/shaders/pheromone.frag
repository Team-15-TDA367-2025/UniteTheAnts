#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform float u_time;

// Decode pheromone type to actual color
vec3 getTypeColor(float typeIndex) {
    if (typeIndex < 0.2) {
        return vec3(0.3, 0.5, 1.0);  // GATHER - blue
    } else if (typeIndex < 0.5) {
        return vec3(1.0, 0.3, 0.3);  // ATTACK - red
    } else {
        return vec3(0.3, 0.9, 0.4);  // EXPLORE - green
    }
}

// Deterministic hash for consistent randomness
float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

void main() {
    // Decode packed data
    float trailStrength = v_color.g;  // 1.0 at start, 0.0 at end
    float seedVal = v_color.b;        // Random seed per pheromone
    vec3 baseColor = getTypeColor(v_color.r);
    
    vec2 uv = v_texCoords; // 0..1
    
    float alpha = 0.0;
    
    // Number of squares based on strength (distance)
    // Strong trails (1.0) get more squares, weak trails get fewer
    float count = 2000.0;
    
    // Spread radius relative to texture size
    // Texture covers 4x4 cells. We want spread around 1 tile (0.25 texture units)
    float spread = 0.15; 
    
    // Size of each square (in UV units)
    // 0.04 is roughly 1/25 of texture, or ~1/6 of a tile width
    float size = 0.04; 

    for (int i = 0; i < 8; i++) {
        if (float(i) >= count) break;
        
        // Use seed + index to generate unique random position
        vec2 p = vec2(seedVal * 100.0 + float(i) * 13.0, seedVal * 50.0 + float(i) * 7.0);
        
        vec2 offset = vec2(hash(p), hash(p + 17.0));
        
        // Map 0..1 to -spread..+spread
        offset = (offset - 0.5) * spread * 2.0;
        
        // Center of this square
        vec2 center = vec2(0.5) + offset;
        
        // Check if current pixel is inside this square
        vec2 d = abs(uv - center);
        if (d.x < size && d.y < size) {
            alpha = 1.0;
            break;
        }
    }
    
    if (alpha < 0.1) discard;
    
    // Solid color squares
    gl_FragColor = vec4(baseColor, 0.8);
}
