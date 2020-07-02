package org.hypnoteyes;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends ScreenAdapter implements Helpers {
  private Preferences preferences = Gdx.app.getPreferences("hypnoteyes");
  private Status status = Status.RUNNING;
  private int levelIndex;
  private int collectedEyeCount = 0;
  private float elapsedTime = 0;
  private int totalScore = preferences.getInteger("currentTotalScore", 0);
  private String message;

  private OrthographicCamera camera = new OrthographicCamera();
  private ExtendViewport viewport = new ExtendViewport(40, 22.5f, camera);
  private World world = new World(vec(0, -5f), true);

  private List<Texture> landscapes =
    seq(new Texture("background0.png"), new Texture("background1.png"),
    new Texture("background2.png"), new Texture("background3.png"));
  private Animation<TextureRegion> tvAnim;
  private Animation<TextureRegion> eyeAnim;
  private List<Texture> jarTextures = seq(new Texture("jar0.png"), new Texture("jar1.png"), new Texture("jar2.png"),
    new Texture("jar3.png"), new Texture("jar4.png"), new Texture("jar5.png"));
  private Texture overlay = new Texture("overlay.png");

  private Sound collectEyeSound = Gdx.audio.newSound(Gdx.files.internal("rise01.mp3"));
  private Sound squashEyeSound = Gdx.audio.newSound(Gdx.files.internal("toyrubberlizardsqueezesqueak.mp3"));
  private Sound crashSound = Gdx.audio.newSound(Gdx.files.internal("bottle-break.mp3"));

  private PolygonSpriteBatch batch = new PolygonSpriteBatch();
  private ShapeRenderer boundsRenderer = new ShapeRenderer();
  private SpriteBatch textBatch = new SpriteBatch();
  private BitmapFont font = new BitmapFont(Gdx.files.internal("evilempire.fnt"));
  private GlyphLayout layout = new GlyphLayout();

  private Body tv = createTvBody();
  private List<Body> eyes = new ArrayList<>();
  private List<Body> boundsBodies = new ArrayList<>();
  private List<Body> deleteList = new ArrayList<>();
  private Array<Vector2> jarPolygon;

  private Level level;
  private Vector2 jarLocation;

  private Body createEyeBody(Vector2 location) {
    var bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(location);
    var body = world.createBody(bodyDef);
    var shape = new CircleShape();
    shape.setRadius(0.375f);
    var fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.004f;
    body.createFixture(fixtureDef);
    body.setLinearDamping(0.5f);
    shape.dispose();
    return body;
  }

  private void createBounds() {
    boundsBodies.forEach(world::destroyBody);
    boundsBodies.clear();
    boundsBodies = level.bounds().stream().map(this::createChainBody).collect(Collectors.toList());
  }

  private Body createChainBody(List<Vector2> chain) {
    var bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    var fixtureDef = new FixtureDef();
    fixtureDef.friction = 1;
    var shape = new ChainShape();
    shape.createChain(chain.toArray(new Vector2[0]));
    fixtureDef.shape = shape;
    var body = world.createBody(bodyDef);
    body.createFixture(fixtureDef);
    shape.dispose();
    return body;
  }

  private Body createTvBody() {
    var bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.fixedRotation = true;
    var body = world.createBody(bodyDef);
    var shape = new PolygonShape();
    shape.set(new float[]{-1.0625f,-1.1875f, -1.0625f,0.5f, 1.0625f,0.5f, 1.0625f,-1.1875f});
    var fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.0043f;
    body.createFixture(fixtureDef);
    body.setLinearDamping(0.5f);
    shape.dispose();
    return body;
  }

  @Override
  public void show() {
    eyeAnim = loadAnimationSheet("eyesheet.png", 6, 8, 48, 12);
    tvAnim = loadAnimationSheet("tvsheet.png", 6, 1, 6, 12);
    viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    world.setContactListener(new ContactListener() {
      @Override
      public void beginContact(Contact contact) {
        var bodyA = contact.getFixtureA().getBody();
        var bodyB = contact.getFixtureB().getBody();
        if (bodyA == tv && eyes.contains(bodyB)) {
          eyes.remove(bodyB);
          deleteList.add(bodyB);
          squashEyeSound.play();
          if (eyes.size() + collectedEyeCount < level.eyeCount() * 0.8) failedWithKill();
          if (eyes.isEmpty() && collectedEyeCount >= level.eyeCount() * 0.8) success();
        }
        if (bodyB == tv && boundsBodies.contains(bodyA)) failedWithCrash();
      }
      @Override public void endContact(Contact contact) {}
      @Override public void preSolve(Contact contact, Manifold oldManifold) {}
      @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
    });

    Gdx.input.setInputProcessor(new InputHandler());
    boundsRenderer.setProjectionMatrix(camera.combined);
    startLevel(preferences.getInteger("currentLevelIndex", 0));
  }

  private void startLevel(int index) {
    if (index == 0) totalScore = 0;
    levelIndex = index;
    level = Hypnoteyes.levels[levelIndex];
    jarLocation = level.jarLocation();
    tv.setTransform(level.tvLocation(), 0);
    tv.setLinearVelocity(0, 0);
    tv.setAngularVelocity(0);
    eyes.forEach(world::destroyBody);
    eyes.clear();
    level.eyeLocations().forEach(l -> eyes.add(createEyeBody(l)));
    jarPolygon = new Array<>(new Vector2[]{
      new Vector2(jarLocation.x-0.58f, jarLocation.y), new Vector2(jarLocation.x+0.58f, jarLocation.y),
      new Vector2(jarLocation.x+0.58f, jarLocation.y+1.72f), new Vector2(jarLocation.x-0.58f, jarLocation.y+1.72f)});
    createBounds();

    Hypnoteyes.music.play();
    collectedEyeCount = 0;
    elapsedTime = 0;
    status = Status.RUNNING;
  }

  @Override
  public void render(float delta) {
    batch.begin();
    batch.draw(landscapes.get(level.backgroundIndex()), 0, 0);
    var currentEye = eyeAnim.getKeyFrame(elapsedTime, true);
    eyes.forEach(eye ->
      batch.draw(currentEye,
        eye.getPosition().x*32 - currentEye.getRegionWidth()/2f, eye.getPosition().y*32 - currentEye.getRegionHeight()/2f,
              currentEye.getRegionWidth()/2f, currentEye.getRegionHeight()/2f,
              (float)currentEye.getRegionWidth(), (float)currentEye.getRegionHeight(),
              0.5f, 0.5f, eye.getTransform().getRotation()* MathUtils.radDeg)
    );
    var jarTexture = jarTextures.get(Math.min(collectedEyeCount, 5));
    batch.draw(jarTexture, jarLocation.x*32 - jarTexture.getWidth()/4f, jarLocation.y*32, jarTexture.getWidth()/2f, jarTexture.getHeight()/2f);
    var tvTexture = tvAnim.getKeyFrame(elapsedTime, true);
    batch.draw(tvTexture, tv.getPosition().x*32 - tvTexture.getRegionWidth()/4f, tv.getPosition().y*32 - tvTexture.getRegionHeight()/4f, tvTexture.getRegionWidth()/2f, tvTexture.getRegionHeight()/2f);
    batch.end();

    boundsRenderer.begin(ShapeRenderer.ShapeType.Filled);
    boundsRenderer.setColor(Color.GRAY);
    level.bounds().forEach( item -> {
      for (int i = 0; i < item.size() - 1; i++) {
        boundsRenderer.rectLine(item.get(i), item.get(i + 1), 0.1f);
      }
    });
    boundsRenderer.end();

    textBatch.begin();
    if (status != Status.RUNNING) {
      textBatch.draw(overlay, 0, 0);
      writeCentered(font, textBatch, message);
    }
    font.draw(textBatch, "Collected " + collectedEyeCount + "/" + level.eyeCount(), 10, 710);
    font.draw(textBatch, "Level " + (levelIndex + 1), 10, 35);
    writeAlignRight(font, textBatch, formatTime(elapsedTime), vec(1270, 710));
    writeAlignRight(font, textBatch, "Total Score " + totalScore, vec(1270, 35));
    textBatch.end();

    if (status == Status.RUNNING) step(delta);
  }

  private String formatTime(float time) {
    return (int)time + "." + (int)(time * 10) % 10;
  }

  private void success() {
    var score = collectedEyeCount * 100;
    var timeBonus = (int)(5000/elapsedTime);
    var levelScore = score + timeBonus;
    totalScore += levelScore;
    var scoreMessage = "\n\nScore: " + score + "\nTime Bonus: " + timeBonus + "\n\nLevel Score: " + levelScore;
    if (levelIndex + 1 == Hypnoteyes.levels.length) {
      save(0);
      status = Status.THE_END;
      message = "That's it, you've got'em all!\n\nPress R to start all over again" + scoreMessage;
    } else {
      save(levelIndex + 1);
      status = Status.SUCCESS;
      message = successMessages.remove(0);
      successMessages.add(message);
      message += scoreMessage + "\n\nPress Enter for next level";
    }
  }

  private List<String> successMessages = seq(
    "Great success!", "Slow clap!", "Hell yeah!", "Much rejoicing!", "Yay you!", "Victorious!");

  private List<String> crashMessages = seq(
    "Did you just crash the TV?!", "Do you know how much one of these things cost?!",
    "Technical difficulties, please stand by", "I don't think the insurance is gonna cover that",
    "Lost signal", "Slap some ducktape on that, no one is gonna notice");

  private List<String> killMessages = seq(
    "Please be gentle with the eyes", "No eye contact, please!", "The eyes belong to the jar, not under the TV",
    "Was that one giving you the evil eye?", "That guy had it coming anyway.", "Oof, you poked that one right in the cornea!");

  private void failedWithCrash() {
    status = Status.FAILED_WITH_CRASH;
    Hypnoteyes.music.pause();
    crashSound.play();
    message = crashMessages.remove(0);
    crashMessages.add(message);
    message += "\n\nPress Enter to try again";
  }

  private void failedWithKill() {
    status = Status.FAILED_WITH_KILL;
    message = killMessages.remove(0);
    killMessages.add(message);
    message += "\n\nPress Enter to try again";
  }

  private void step(float delta) {
    elapsedTime += delta;
    eyes.forEach(eye -> {
      if (Intersector.isPointInPolygon(jarPolygon, eye.getWorldCenter())) {
        deleteList.add(eye);
        collectEyeSound.play();
        collectedEyeCount += 1;
        if (eyes.size() == 1) success();
      }
    });
    eyes.removeAll(deleteList);
    deleteList.forEach(world::destroyBody);
    deleteList.clear();

    world.step(delta, 6, 2);
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) tv.applyForceToCenter(0.04f, 0f, true);
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) tv.applyForceToCenter(-0.04f, 0f, true);
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) tv.applyForceToCenter(0f, 0.2f, true);
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) tv.applyForceToCenter(0f, -0.2f, true);

    eyes.forEach(eye -> {
      if (tv.getWorldCenter().dst2(eye.getWorldCenter()) < 20) {
        var dx = eye.getWorldCenter().x - tv.getWorldCenter().x;
        eye.applyForceToCenter((dx < 0) ? 0.015f : -0.015f, 0, true);
      }
    });
  }

  private class InputHandler extends InputAdapter {
    @Override
    public boolean keyDown(int keyCode) {
      if (keyCode == Input.Keys.ENTER && (status == Status.FAILED_WITH_KILL || status == Status.FAILED_WITH_CRASH)) startLevel(levelIndex);
      if (keyCode == Input.Keys.ENTER && status == Status.SUCCESS) startLevel(levelIndex + 1);
      if (keyCode == Input.Keys.R && status == Status.THE_END) startLevel(0);
      if (keyCode == Input.Keys.M) Hypnoteyes.music.setVolume(1- Hypnoteyes.music.getVolume());
      if (keyCode == Input.Keys.ESCAPE || keyCode == Input.Keys.Q) Gdx.app.exit();
      return true;
    }
  }

  private void writeCentered(BitmapFont font, SpriteBatch batch, String text) {
    layout.setText(font, text);
    font.draw(batch, text,
      (Gdx.graphics.getWidth() - layout.width)/2, (Gdx.graphics.getHeight() - layout.height)/2 + layout.height, layout.width,
      Align.center, false);
  }

  private void writeAlignRight(BitmapFont font, SpriteBatch batch, String text, Vector2 p1) {
    layout.setText(font, text);
    font.draw(batch, text, p1.x - layout.width, p1.y);
  }

  private void save(int levelIndex) {
    preferences.putInteger("currentLevelIndex", levelIndex);
    preferences.putInteger("currentTotalScore", totalScore);
    preferences.flush();
  }
}

enum Status {
  STARTING, RUNNING, PAUSED, FAILED_WITH_CRASH, FAILED_WITH_KILL, SUCCESS, THE_END
}