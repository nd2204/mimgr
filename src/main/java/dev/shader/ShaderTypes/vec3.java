package dev.shader.ShaderTypes;

public class vec3 {
  public float x = 0;
  public float y = 0;
  public float z = 0;

  public vec3(float v) {
    this.x = v;
    this.y = v;
    this.z = v;
  }

  public vec3(vec2 v2) {
    this.x = v2.x;
    this.y = v2.y;
  }

  public vec3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public vec3(vec2 xy, float z) {
    this.x = xy.x;
    this.y = xy.y;
    this.z = z;
  }

  public vec3(float x, vec2 yz) {
    this.x = x;
    this.y = yz.x;
    this.z = yz.y;
  }

  public static vec3 add(vec3 a, vec3 b) {
    return new vec3(a.x + b.x, a.y + b.y, a.z + b.z);
  }

  public static vec3 add(vec3 v, float f) {
    return new vec3(v.x + f, v.y + f, v.z + f);
}

  public static vec3 sub(vec3 a, vec3 b) {
    return new vec3(a.x - b.x, a.y - b.y, a.z - b.z);
  }

  public static vec3 sub(vec3 a, float b) {
    return new vec3(a.x - b, a.y - b, a.z - b);
  }

  public static vec3 div(vec3 a, vec3 b) {
    return new vec3(a.x / b.x, a.y / b.y, a.z / b.z);
  }

  public static vec3 div(vec3 a, float b) {
    return new vec3(a.x / b, a.y / b, a.z / b);
  }

  public static vec3 mult(vec3 a, vec3 b) {
    return new vec3(a.x * b.x, a.y * b.y, a.z * b.z);
  }

  public static vec3 mult(vec3 a, float b) {
    return new vec3(a.x * b, a.y * b, a.z * b);
  }

  public vec3 add(vec3 b) { return add(this, b); }
  public vec3 add(float v) { return add(this, v); }

  public vec3 sub(vec3 b) { return sub(this, b); }
  public vec3 sub(float b) { return sub(this, b); }

  public vec3 div(vec3 b) { return div(this, b); }
  public vec3 div(float b) { return div(this, b); }

  public vec3 mult(vec3 b) { return mult(this, b); }
  public vec3 mult(float b) { return mult(this, b); }
}
