package org.hypnoteyes.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.hypnoteyes.Hypnoteyes;

public class DesktopLauncher {
  public static void main(String[] arg) {
    var config = new LwjglApplicationConfiguration();
    config.width = 1280;
    config.height = 720;
    config.samples = 3;
    config.resizable = false;
    new LwjglApplication(new Hypnoteyes(), config);
  }
}