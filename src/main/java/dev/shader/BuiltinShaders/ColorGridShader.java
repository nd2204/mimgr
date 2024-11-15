package dev.shader.BuiltinShaders;

import dev.shader.ShaderInputs;
import dev.shader.ShaderFunctions.IShaderEntry;
import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec3;
import dev.shader.ShaderTypes.vec4;
import dev.shader.ShaderFunctions;

public class ColorGridShader implements IShaderEntry {
  @Override
  public vec4 mainImage(ShaderInputs si, vec2 fragCoord) {
    vec2 px = (new vec2(si.iResolution.x * -1, si.iResolution.y * -1)
      .add(fragCoord.mult(2.0f))).mult(8.0f)
      .div(si.iResolution.y);

    float id = 0.5f + 0.5f * (float) Math.cos(si.iTime + (float) Math.sin(ShaderFunctions.dot(ShaderFunctions.floor(px.add(0.5f)),new vec2(113.1f,17.81f)))*43758.545f);

    vec3 co = ShaderFunctions.cos(new vec3(0.3f,1.0f,1.0f).add(si.iTime + 2.0f * id)).mult(0.1f).add(0.1f);

    vec2 pa = ShaderFunctions.smoothstep(0.0f, 0.2f, (ShaderFunctions.cos(px.mult(6.2831f)).mult(0.5f).add(0.5f)).mult(id));

    return new vec4( co.mult(pa.x * pa.y), 1.0f );
  }
}

