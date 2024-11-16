package dev.shader.ShaderTypes;

public class vec4 {
  public float x = 0;
  public float y = 0;
  public float z = 0;
  public float w = 0;

  public vec4(float a, float b, float c, float d) {
    x = a;
    y = b;
    z = b;
    w = b;
  }

  public vec4(vec4 v) {
    x = v.x;
    y = v.y;
    z = v.z;
    w = v.w;
  }

  public vec4(float v) {
    x = v;
    y = v;
    z = v;
    w = v;
  }

  public vec4(vec3 v3, float w) {
    this.x = v3.x;
    this.y = v3.y;
    this.z = v3.z;
    this.w = w;
  }

  public vec4(float x, vec3 v3) {
    this.x = x;
    this.y = v3.x;
    this.z = v3.y;
    this.w = v3.z;
  }

  public vec4(vec3 v3) {
    this.x = v3.x;
    this.y = v3.y;
    this.z = v3.z;
  }

  public vec4(vec2 v2) {
    this.x = v2.x;
    this.y = v2.y;
  }

  public vec4(vec2 v2, float z, float w) {
    this.x = v2.x;
    this.y = v2.y;
    this.z = z;
    this.w = w;
  }

  public vec4(float x, vec2 yz, float w) {
    this.x = x;
    this.y = yz.x;
    this.z = yz.y;
    this.w = w;
  }

  public vec4(float x, float y, vec2 zw) {
    this.x = x;
    this.y = y;
    this.z = zw.x;
    this.w = zw.y;
  }

  public static vec4 add(vec4 a, vec4 b) {
    return new vec4(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
  }

  public static vec4 sub(vec4 a, vec4 b) {
    return new vec4(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
  }


  public static vec4 div(vec4 a, vec4 b) {
    return new vec4(a.x / b.x, a.y / b.y, a.z / b.z, a.w / b.w);
  }

  public static vec4 mult(vec4 a, vec4 b) {
    return new vec4(a.x * b.x, a.y * b.y, a.z * b.z, a.w * b.w);
  }

  public vec4 add(vec4 b) { return new vec4(this.x + b.x, this.y + b.y, this.z + b.z, this.w + b.w); }
  public vec4 add(float b) { return new vec4(this.x + b, this.y + b, this.z + b, this.w + b); }

  public vec4 sub(vec4 b) { return new vec4(this.x - b.x, this.y - b.y, this.z - b.z, this.w - b.w); }
  public vec4 sub(float b) { return new vec4(this.x - b, this.y - b, this.z - b, this.w - b); }

  public vec4 div(vec4 b) { return new vec4(this.x / b.x, this.y / b.y, this.z / b.z, this.w / b.w); }
  public vec4 div(float b) { return new vec4(this.x / b, this.y / b, this.z / b, this.w / b); }

  public vec4 mult(vec4 b) { return new vec4(this.x * b.x, this.y * b.y, this.z * b.z, this.w * b.w); }
  public vec4 mult(float b) { return new vec4(this.x * b, this.y * b, this.z * b, this.w * b); }
}
