package dev.shader.BuiltinShaders;

import dev.shader.ShaderInputs;
import dev.shader.ShaderFunctions.IShaderEntry;
import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec3;
import dev.shader.ShaderTypes.vec4;

import dev.shader.ShaderFunctions;

public class DeformShader implements IShaderEntry {
  protected vec3 col = new vec3(0.0f);
  protected vec3 lineCol = new vec3(0.0f);
  @Override
  public void mainImage(final ShaderInputs si, vec4 fragColor, final vec2 fragCoord) {
    vec2 p;
    vec2 m;
    vec2 uv = new vec2(0);

    float a1;
    float r1;
    float a2;
    float r2;

    p = fragCoord.mult(2.0f).sub(si.iResolution).div(si.iResolution.y);
    m = new vec2((float) Math.sin(si.iTime)).mult(2.0f).sub(si.iResolution).div(si.iResolution.y);

    a1 = (float) Math.atan2(p.y-m.y, p.x-m.x);
    r1 = ShaderFunctions.length(p.sub(m));
    a2 = (float) Math.atan2(p.y+m.y, p.x+m.x);
    r2 = ShaderFunctions.length(p.add(m));

    uv.x = 0.2f * (r1-r2) + 0.01f * si.iTime;
    uv.y = (float) Math.asin(Math.sin(a1-a2)/3.1415927f);

    float w = ShaderFunctions.exp2(-18.0f * r1*r1) + ShaderFunctions.exp2(-18.0f * r2 * r2);
    w += 0.20f*ShaderFunctions.smoothstep(0.93f,1.0f,(float) Math.sin(128.0f*uv.x));
    w += 0.20f*ShaderFunctions.smoothstep(0.93f,1.0f,(float) Math.sin(128.0f*uv.y));

    fragColor.x = col.x + w * lineCol.x;
    fragColor.y = col.y + w * lineCol.y;
    fragColor.z = col.z + w * lineCol.z;
    fragColor.w = 1.0f;
  }
}

