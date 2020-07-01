package org.hypnoteyes;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Level {
  private int backgroundIndex;
  private List<List<Vector2>>bounds;
  private Vector2 jarLocation;
  private List<Vector2> eyeLocations;
  private Vector2 tvLocation;

  public Level(
    int backgroundIndex,
    List<List<Vector2>>bounds,
    Vector2 jarLocation,
    List<Vector2> eyeLocations,
    Vector2 tvLocation
  ) {
    this.backgroundIndex = backgroundIndex;
    this.bounds = bounds;
    this.jarLocation = jarLocation;
    this.eyeLocations = eyeLocations;
    this.tvLocation = tvLocation;
  }

  public int eyeCount() {
    return eyeLocations.size();
  }

  public int backgroundIndex() {
    return backgroundIndex;
  }

  public List<List<Vector2>> bounds() {
    return bounds;
  }

  public Vector2 jarLocation() {
    return jarLocation;
  }

  public List<Vector2> eyeLocations() {
    return eyeLocations;
  }

  public Vector2 tvLocation() {
    return tvLocation;
  }
}