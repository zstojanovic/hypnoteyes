package org.hypnoteyes.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import org.hypnoteyes.Hypnoteyes;

public class HtmlLauncher extends GwtApplication {

  @Override
  public GwtApplicationConfiguration getConfig() {
    var config = new GwtApplicationConfiguration(1280, 720);
    config.antialiasing = true;
    return config;
  }

  @Override
  public Preloader.PreloaderCallback getPreloaderCallback() {
    return createPreloaderPanel(GWT.getHostPageBaseURL() + "preloadlogo.png");
  }

  @Override
  protected void adjustMeterPanel(Panel meterPanel, Style meterStyle) {
    meterPanel.setStyleName("gdx-meter");
    meterPanel.addStyleName("nostripes");
    meterStyle.setProperty("backgroundColor", "#ffffff");
    meterStyle.setProperty("backgroundImage", "none");
  }

  @Override
  public ApplicationListener createApplicationListener() {
    return new Hypnoteyes();
  }
}