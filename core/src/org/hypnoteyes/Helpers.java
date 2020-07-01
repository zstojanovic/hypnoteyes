package org.hypnoteyes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Helpers {

  default Vector2 vec(double x, double y) {
    return new Vector2((float)x, (float)y);
  }

  default <T> List<T> seq(T... ts) {
    return new ArrayList<>(Arrays.asList(ts));
  }

  default Animation<TextureRegion> loadAnimationSheet(String internalPath, int cols, int rows, int frameCount, int fps) {
    var texture = new Texture(internalPath);
    var allTiles = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);
    var frames = new TextureRegion[frameCount];
    for (int i = 0; i < frameCount; i++) {
      frames[i] = allTiles[i / cols][i % cols];
    }

    return new Animation<>(1f / fps, frames);
  }
}