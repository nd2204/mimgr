package dev.shader.BuiltinShaders;

import dev.shader.ShaderInputs;
import dev.shader.ShaderFunctions.IShaderEntry;
import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec3;
import dev.shader.ShaderTypes.vec4;
import dev.shader.ShaderFunctions;

public class PalleteShader implements IShaderEntry {
  private static final vec3 co = new vec3(6.28318f);

  private vec3 pal(float t, vec3 a, vec3 b, vec3 c, vec3 d) {
    return vec3.add(
      a, b.mult(ShaderFunctions.cos(c.mult(d.add(new vec3(t))).mult(co)))
    );
  }

  @Override
  public vec4 mainImage(ShaderInputs si, vec2 fragCoord) {
    vec2 p = fragCoord.div(si.iResolution);
    vec4 fragColor = new vec4(1.0f);
    vec3 col = new vec3(0.0f);

    // animate
    p.x += 0.05f * si.iTime;

    // compute colors
    col = pal( p.x, new vec3(0.5f,0.5f,0.5f),new vec3(0.5f,0.5f,0.5f),new vec3(1.0f,1.0f,1.0f),new vec3(0.0f,0.33f,0.67f) );
    if( p.y>(1.0/7.0) ) col = pal( p.x, new vec3(0.5f,0.5f,0.5f),new vec3(0.5f,0.5f,0.5f),new vec3(1.0f,1.0f,1.0f),new vec3(0.0f,0.10f,0.20f) );
    if( p.y>(2.0/7.0) ) col = pal( p.x, new vec3(0.5f,0.5f,0.5f),new vec3(0.5f,0.5f,0.5f),new vec3(1.0f,1.0f,1.0f),new vec3(0.3f,0.20f,0.20f) );
    if( p.y>(3.0/7.0) ) col = pal( p.x, new vec3(0.5f,0.5f,0.5f),new vec3(0.5f,0.5f,0.5f),new vec3(1.0f,1.0f,0.5f),new vec3(0.8f,0.90f,0.30f) );
    if( p.y>(4.0/7.0) ) col = pal( p.x, new vec3(0.5f,0.5f,0.5f),new vec3(0.5f,0.5f,0.5f),new vec3(1.0f,0.7f,0.4f),new vec3(0.0f,0.15f,0.20f) );
    if( p.y>(5.0/7.0) ) col = pal( p.x, new vec3(0.5f,0.5f,0.5f),new vec3(0.5f,0.5f,0.5f),new vec3(2.0f,1.0f,0.0f),new vec3(0.5f,0.20f,0.25f) );
    if( p.y>(6.0/7.0) ) col = pal( p.x, new vec3(0.8f,0.5f,0.4f),new vec3(0.2f,0.4f,0.2f),new vec3(2.0f,1.0f,1.0f),new vec3(0.0f,0.25f,0.25f) );

    // band
    float f = ShaderFunctions.fract(p.y * 7.0f);
    // borders
    col = col.mult( new vec3(ShaderFunctions.smoothstep(0.49f, 0.47f, Math.abs(f-0.5f))) );
    // shadowing
    col = col.mult( new vec3(0.5f + 0.5f * (float) Math.sqrt(4.0f*f*(1.0f-f))) );

    fragColor.x = col.x;
    fragColor.y = col.y;
    fragColor.z = col.z;
    return fragColor;
  }
}

