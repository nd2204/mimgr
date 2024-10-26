package dev.mimgr.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

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
    float t = clamp((x - edge0) / (edge1 - edge0), 0, 1);
    return t * t * (3 - 2 * t);
  }

  // mix
  public static float mix(float x, float y, float a) {
    return x * (1 - a) + y * a;
  }

  // fract
  public static float fract(float x) {
    return x - (float) Math.floor(x);
  }

  // clamp
  public static float clamp(float x, float minVal, float maxVal) {
    return Math.max(minVal, Math.min(maxVal, x));
  }
}
