package dev.shader;

import java.awt.Color;
import java.awt.image.BufferedImage;

import dev.shader.ShaderTypes.*;

public class ShaderFunctions {
  // texture
  public static Color texture(BufferedImage texture, float u, float v) {
    // Clamp u and v to [0, 1]
    u = clamp(u, 0, 1);
    v = clamp(v, 0, 1);

    int x = (int) (u * (texture.getWidth() - 1));
    int y = (int) (v * (texture.getHeight() - 1));

    return new Color(texture.getRGB(x, y), true);
  }

  // smoothstep
  public static float smoothstep(float edge0, float edge1, float x) {
    float t = clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
    return t * t * (3.0f - 2.0f * t);
  }

  // Vector smoothstep for vec2
  public static vec2 smoothstep(float edge0, float edge1, vec2 v) {
    return new vec2(smoothstep(edge0, edge1, v.x), smoothstep(edge0, edge1, v.y));
  }

  // Vector smoothstep for vec3
  public static vec3 smoothstep(float edge0, float edge1, vec3 v) {
    return new vec3(smoothstep(edge0, edge1, v.x), smoothstep(edge0, edge1, v.y), smoothstep(edge0, edge1, v.z));
  }

  // Vector smoothstep for vec4
  public static vec4 smoothstep(float edge0, float edge1, vec4 v) {
    return new vec4(smoothstep(edge0, edge1, v.x), smoothstep(edge0, edge1, v.y), smoothstep(edge0, edge1, v.z), smoothstep(edge0, edge1, v.w));
  }

  // Scalar mix
  public static float mix(float a, float b, float t) {
    return a * (1.0f - t) + b * t;
  }

  // Vector mix for vec2
  public static vec2 mix(vec2 a, vec2 b, float t) {
    return new vec2(mix(a.x, b.x, t), mix(a.y, b.y, t));
  }

  // Vector mix for vec3
  public static vec3 mix(vec3 a, vec3 b, float t) {
    return new vec3(mix(a.x, b.x, t), mix(a.y, b.y, t), mix(a.z, b.z, t));
  }

  // Vector mix for vec4
  public static vec4 mix(vec4 a, vec4 b, float t) {
    return new vec4(mix(a.x, b.x, t), mix(a.y, b.y, t), mix(a.z, b.z, t), mix(a.w, b.w, t));
  }

  // fract
  public static float fract(float x) {
    return x - (float) Math.floor(x);
  }

  public static vec2 fract(vec2 v) {
    return new vec2(fract(v.x), fract(v.y));
  }

  public static vec3 fract(vec3 v) {
    return new vec3(fract(v.x), fract(v.y), fract(v.z));
  }

  public static vec4 fract(vec4 v) {
    return new vec4(fract(v.x), fract(v.y), fract(v.z), fract(v.w));
  }

  // Square root function for vec2
  public static vec2 sqrt(vec2 v) {
    return new vec2((float) Math.sqrt(v.x), (float) Math.sqrt(v.y));
  }

  // Square root function for vec3
  public static vec3 sqrt(vec3 v) {
    return new vec3((float) Math.sqrt(v.x), (float) Math.sqrt(v.y), (float) Math.sqrt(v.z));
  }

  // Square root function for vec4
  public static vec4 sqrt(vec4 v) {
    return new vec4((float) Math.sqrt(v.x), (float) Math.sqrt(v.y), (float) Math.sqrt(v.z), (float) Math.sqrt(v.w));
  }

  // Step
  public static float step(float edge, float x) {
    return x < edge ? 0.0f : 1.0f;
  }

  // Step function for vec2
  public static vec2 step(float edge, vec2 v) {
    return new vec2(step(edge, v.x), step(edge, v.y));
  }

  // Step function for vec3
  public static vec3 step(float edge, vec3 v) {
    return new vec3(step(edge, v.x), step(edge, v.y), step(edge, v.z));
  }

  // Step function for vec4
  public static vec4 step(float edge, vec4 v) {
    return new vec4(step(edge, v.x), step(edge, v.y), step(edge, v.z), step(edge, v.w));
  }

  // clamp
  public static float clamp(float x, float minVal, float maxVal) {
    return Math.max(minVal, Math.min(maxVal, x));
  }

  // Clamp function for vec2
  public static vec2 clamp(vec2 v, float minVal, float maxVal) {
    return new vec2(clamp(v.x, minVal, maxVal), clamp(v.y, minVal, maxVal));
  }

  // Clamp function for vec3
  public static vec3 clamp(vec3 v, float minVal, float maxVal) {
    return new vec3(clamp(v.x, minVal, maxVal), clamp(v.y, minVal, maxVal), clamp(v.z, minVal, maxVal));
  }

  // Clamp function for vec4
  public static vec4 clamp(vec4 v, float minVal, float maxVal) {
    return new vec4(clamp(v.x, minVal, maxVal), clamp(v.y, minVal, maxVal), clamp(v.z, minVal, maxVal), clamp(v.w, minVal, maxVal));
  }

  // Dot product for vec2
  public static float dot(vec2 a, vec2 b) {
    return a.x * b.x + a.y * b.y;
  }

  // Dot product for vec3
  public static float dot(vec3 a, vec3 b) {
    return a.x * b.x + a.y * b.y + a.z * b.z;
  }

  // Dot product for vec4
  public static float dot(vec4 a, vec4 b) {
    return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
  }

  // Sine for vec2
  public static vec2 sin(vec2 v) {
    return new vec2((float) Math.sin(v.x), (float) Math.sin(v.y));
  }

  // Sine for vec3
  public static vec3 sin(vec3 v) {
    return new vec3((float) Math.sin(v.x), (float) Math.sin(v.y), (float) Math.sin(v.z));
  }

  // Sine for vec4
  public static vec4 sin(vec4 v) {
    return new vec4((float) Math.sin(v.x), (float) Math.sin(v.y), (float) Math.sin(v.z), (float) Math.sin(v.w));
  }

  // Method for component-wise cosine calculation
  public static vec3 cos(vec3 v) {
    return new vec3((float) Math.cos(v.x), (float) Math.cos(v.y), (float) Math.cos(v.z));
  }

  // Method for component-wise cosine calculation
  public static vec2 cos(vec2 v) {
    return new vec2((float) Math.cos(v.x), (float) Math.cos(v.y));
  }

  // Floor for vec2
  public static vec2 floor(vec2 v) {
    return new vec2((float) Math.floor(v.x), (float) Math.floor(v.y));
  }

  // Floor for vec3
  public static vec3 floor(vec3 v) {
    return new vec3((float) Math.floor(v.x), (float) Math.floor(v.y), (float) Math.floor(v.z));
  }

  // Floor for vec4
  public static vec4 floor(vec4 v) {
    return new vec4((float) Math.floor(v.x), (float) Math.floor(v.y), (float) Math.floor(v.z), (float) Math.floor(v.w));
  }

  public static float length(vec2 v) {
    return (float) Math.sqrt(v.x * v.x + v.y * v.y);
  }

  public static float length(vec3 v) {
    return (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
  }

  public static float length(vec4 v) {
    return (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z + v.w * v.w);
  }

  public static float exp2(float x) {
    return (float) Math.pow(2, x);
  }

  public interface IShaderEntry {
    public void mainImage(final ShaderInputs si, vec4 fragColor, final vec2 fragCoord);
  }
}
