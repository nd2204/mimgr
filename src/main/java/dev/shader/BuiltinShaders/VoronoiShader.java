package dev.shader.BuiltinShaders;

import dev.shader.ShaderInputs;
import dev.shader.ShaderFunctions.IShaderEntry;
import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec3;
import dev.shader.ShaderTypes.vec4;
import dev.shader.ShaderFunctions;

class VoronoiShader implements IShaderEntry {
  float hash1( float n ) {
    return ShaderFunctions.fract((float) Math.sin(n)*43758.5453f);
  }

  private vec2 hash2( vec2  p ) {
    p = new vec2(
      ShaderFunctions.dot(p, new vec2(127.1f,311.7f)),
      ShaderFunctions.dot(p, new vec2(269.5f,183.3f))
    );
    return ShaderFunctions.fract(ShaderFunctions.sin(p).mult(new vec2(43758.5453f)));
  }

  // // The parameter w controls the smoothness
  private vec4 voronoi(ShaderInputs si, vec2 x, float w) {
    vec2 n = ShaderFunctions.floor( x );
    vec2 f = ShaderFunctions.fract( x );

    vec4 m = new vec4( 8.0f, 0.0f, 0.0f, 0.0f );
    for( int j=-2; j<=2; j++ )
      for( int i=-2; i<=2; i++ ) {
        vec2 g = new vec2( i, j );
        vec2 o = hash2( n.add(g) );

        // animate
        o = ShaderFunctions.sin(o.mult(6.2831f).add(si.iTime)).mult(0.5f).add(0.5f);

        // distance to cell		
        float d = ShaderFunctions.length(g.sub(f).add(o));

        // cell color
        vec3 col = ShaderFunctions.sin(
          new vec3(hash1(ShaderFunctions.dot(n.add(g),new vec2(7.0f,113.0f))) * 2.5f + 3.5f).add(new vec3(2.0f,3.0f,0.0f))
        ).mult(0.5f).add(0.5f);
        // in linear space
        col = col.mult(col);

        // do the smooth min for colors and distances		
        float h = ShaderFunctions.smoothstep( -1.0f, 1.0f, (m.x-d)/w );
        m.x   = ShaderFunctions.mix( m.x, d, h ) - h*(1.0f-h)*w/(1.0f+3.0f*w); // distance
        m = new vec4(m.x,  ShaderFunctions.mix( new vec3(m.y, m.z, m.w), col, h ).sub(h*(1.0f-h)*w/(1.0f+3.0f*w))); // color)
      }

    return m;
  }

  @Override
  public void mainImage(final ShaderInputs si, vec4 fragColor, final vec2 fragCoord) {
    vec2  p = fragCoord.div(new vec2(si.iResolution.y));
    vec4 v = voronoi(si ,p.mult(6.0f), 0.3f );
    // gamma
    vec3 col = ShaderFunctions.sqrt(new vec3(v.y, v.z, v.w));
    col = col.mult(new vec3(1.0f-0.8f*v.x*ShaderFunctions.step(p.y,0.0f)));
    fragColor.x = col.x;
    fragColor.y = col.y;
    fragColor.z = col.z;
    fragColor.w = 1.0f;
  }
}

