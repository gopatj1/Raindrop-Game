package gopatj.game.raindrop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import gopatj.game.raindropHelpers.AssetLoader;

public class Drop extends Game{

    public float screenWidth, screenHeight, gameWidth, gameHeight; //размеры экрана устройства и игры

    @Override
    public void create() {

        screenWidth = Gdx.graphics.getWidth(); //узнаем ширину устройства
        screenHeight = Gdx.graphics.getHeight(); //узнаем высоту устройства
        gameWidth = 480; //ширина игры
        gameHeight = 800; //высота игры

        AssetLoader.load(); //загрузка всех картинок
        this.setScreen(new gopatj.game.screens.SplashScreen(this)); //устанавливаем экран приветствия
    }

    @Override
    public void render() {
        super.render();
    }

    //освобождаем ресурсы
    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
