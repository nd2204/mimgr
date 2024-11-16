package dev.shader.BuiltinShaders;

import dev.shader.ShaderInputs;
import dev.shader.ShaderFunctions.IShaderEntry;
import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec3;
import dev.shader.ShaderTypes.vec4;

import dev.shader.ShaderFunctions;

public class TriLatticeShader implements IShaderEntry {
  private final vec2 co = new vec2(12.9898f, 78.233f).mult(43758.5453f);
  private final vec2 lt = new vec2(1.0f, 0.5f);
  private final vec2 rt = new vec2(-1.0f, 0.5f);
  protected vec3 col = new vec3(0.0f);
  protected vec3 lineCol = new vec3(1.0f);

  private float rand(vec2 uv) {
    return ShaderFunctions.fract((float) Math.sin(ShaderFunctions.dot(uv, co)));
  }

  private vec2 uv2tri(vec2 uv) {
    float sx = uv.x - uv.y / 2.0f; // skewed x
    float offs =  ShaderFunctions.step(ShaderFunctions.fract(1.0f - uv.y), ShaderFunctions.fract(sx));
    return new vec2((float) Math.floor(sx) * 2.0f + offs, (float) Math.floor(uv.y));
  }

  @Override
  public vec4 mainImage(ShaderInputs si, vec2 fragCoord) {
    vec2 uv = (si.iResolution.div(2.0f).sub(fragCoord)).div(si.iResolution.y).mult(32.0f);

    vec3 p = new vec3(ShaderFunctions.dot(uv, lt), ShaderFunctions.dot(uv, rt), uv.y);
    vec3 p1 = ShaderFunctions.fract(p);
    vec3 p2 = ShaderFunctions.fract(new vec3(-p.x, -p.y, -p.z));

    float d1 = Math.min(Math.min(p1.x, p1.y), p1.z);
    float d2 = Math.min(Math.min(p2.x, p2.y), p2.z);
    float d = Math.min(d1, d2);

    vec2 tri = uv2tri(uv);
    float r = rand(tri) * 2.0f + tri.x / 16.0f + si.iTime * 1.0f;

    vec3 c = ShaderFunctions.mix(
      col,
      lineCol,
      ShaderFunctions.smoothstep(-0.02f,0.0f,d-0.2f*(1.0f+(float)Math.sin(r)))
    );

    vec4 fragColor = new vec4(1.0f);
    fragColor.x = c.x;
    fragColor.y = c.y;
    fragColor.z = c.z;

    return fragColor;
  }
}
