package com.mygdx.pong;

import com.badlogic.gdx.Game;


public class Main extends Game {
	
	public static AbstractScreen gameScreen;
	public AbstractScreen menuScreen;
	
	@Override
	public void create () {
		menuScreen = new MainMenu(this);
		setScreen(menuScreen);
	}
	
	public void startNewGame(){
		gameScreen = new GameScreen(this);
		this.setScreen(gameScreen);
	}
}
