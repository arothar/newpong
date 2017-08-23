package com.mygdx.pong;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.mygdx.pong.GameScreen;
import com.mygdx.pong.Main;

public class MainMenu extends AbstractScreen {
	
	private Stage scene;
	private Skin skin;

	public MainMenu(final Main game) {
		super(game);
		scene = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		Table tblMenu = new Table();
		tblMenu.setFillParent(true);

		TextButton btnPlay = new TextButton("Jugar", skin);
		btnPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (status != "InGame"){
					game.startNewGame();
				}
			}
		});

		TextButton btnExit = new TextButton("Salir", skin);
		btnExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});		
	
		tblMenu.add(btnPlay).expandX().width(200).height(60).space(20);
		tblMenu.row();
		tblMenu.add(btnExit).expandX().width(200).height(60).space(20);
		

		scene.addActor(tblMenu);
		scene.setDebugAll(false);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(scene);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl20.glClearColor(0.22f, 0.25f, 0.23f, 0.6f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		scene.draw();
		scene.act();
	}

	@Override
	public void resize(int width, int height) {
		scene.getViewport().update(width, height);
	}	
	
	public void dispose(){
		scene.dispose();
	}

}
