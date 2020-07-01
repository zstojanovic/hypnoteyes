package org.hypnoteyes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen extends ScreenAdapter {
  private Hypnoteyes game;
  private SpriteBatch batch = new SpriteBatch();
  private Texture title = new Texture("titlescreen.png");

  public TitleScreen(Hypnoteyes game) {
    this.game = game;
  }

  @Override
  public void show() {
    Hypnoteyes.music.setLooping(true);
    Hypnoteyes.music.play();
    Gdx.input.setInputProcessor(new InputAdapter() {
      @Override
      public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ENTER) game.setScreen(new GameScreen());
        if (keyCode == Input.Keys.ESCAPE || keyCode == Input.Keys.Q) Gdx.app.exit();
        if (keyCode == Input.Keys.M) Hypnoteyes.music.setVolume(1 - Hypnoteyes.music.getVolume());
        return true;
      }
    });
  }

  @Override
  public void render(float delta) {
    batch.begin();
    batch.draw(title, 0, 0);
    batch.end();
  }
}