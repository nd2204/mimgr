package dev.shader.ShaderTypes;

public class vec2 {
  public float x = 0;
  public float y = 0;

  public vec2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public vec2(float v) {
    this.x = v;
    this.y = v;
  }

  public static vec2 add(vec2 a, vec2 b) {
    return new vec2(a.x + b.x, a.y + b.y);
  }

  public static vec2 add(vec2 a, float v) {
    return new vec2(a.x + v, a.y + v);
  }

  public static vec2 sub(vec2 a, vec2 b) {
    return new vec2(a.x - b.x, a.y - b.y);
  }

  public static vec2 sub(vec2 a, float b) {
    return new vec2(a.x - b, a.y - b);
  }

  public static vec2 div(vec2 a, vec2 b) {
    return new vec2(a.x / b.x, a.y / b.y);
  }

  public static vec2 div(vec2 a, float b) {
    return new vec2(a.x / b, a.y / b);
  }

  public static vec2 mult(vec2 a, vec2 b) {
    return new vec2(a.x * b.x, a.y * b.y);
  }

  public static vec2 mult(vec2 a, float v) {
    return new vec2(a.x * v, a.y * v);
  }

  public vec2 add(vec2 b) { return add(this, b); }
  public vec2 add(float v) { return add(this, v); }

  public vec2 sub(vec2 b) { return sub(this, b); }
  public vec2 sub(float b) { return sub(this, b); }

  public vec2 div(vec2 b) { return div(this, b); }
  public vec2 div(float b) { return div(this, b); }

  public vec2 mult(vec2 b) { return mult(this, b); }
  public vec2 mult(float v) { return mult(this, v); }
}
