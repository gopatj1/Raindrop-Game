package gopatj.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gopatj.game.raindrop.Drop;
import gopatj.game.TweenAccessors.SpriteAccessor;
import gopatj.game.raindropHelpers.AssetLoader;

public class SplashScreen implements Screen {

    private TweenManager manager; //новый accessor
    private SpriteBatch batcher; //отрисовщик
    private Sprite sprite; //спрайт
    private Drop game; //главный клас

    public SplashScreen(Drop game) {

        this.game = game;
    }

    @Override
    public void show() {

        //приветственный логотип
        sprite = new Sprite(AssetLoader.logo);
        sprite.setColor(0, 0, 0, 0); //черный прозрачный цвет

        float width = Gdx.graphics.getWidth(); //ширина устройства
        float height = Gdx.graphics.getHeight(); //высота устройства

        //указываем размер спрайта и позицию
        sprite.setSize(width, height);
        sprite.setPosition(0, 0);
        setupTween(); //функция плавного появления
        batcher = new SpriteBatch();
    }

    private void setupTween() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor()); //регистрирует новый Accessor
        manager = new TweenManager(); //менеджер Tween Engine

        //callback чей метод onEvent (который вызывается, когда Tweening закончился), перенаправит нас на GameScreen
        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new MainMenuScreen(game)); //смена экрана
            }
        };

        Tween.to(sprite, SpriteAccessor.ALPHA, 2f).target(1)
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, 1f)
                .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);

        /*
        Tween.to(sprite, SpriteAccessor.ALPHA, 2f).target(1)
        Хотим изменить объект типа sprite, используя tweenType ALPHA (прозрачность) из SpriteAccessor. Хотим,
        чтобы эта операция длилась 2 секунды. Хотим изменить стартовое значение (это указано в классе SpriteAccessor) в новое,
        равное 1.

        .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, 1f)
        Мы хотим использовать квадратичную интерполяцию и повторить это действие
        один раз как Yoyo (за 1 секунду между повторениями).

        .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
        Используем callback который ранее создали и назвали как cb, и уведомить его когда Tweening закончилось.

        .start(manager);
        Указываем какой менеджер выполнит всю эту работу.
        */
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0); //отчищаем экран
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //в черный цвет
        batcher.begin();
        sprite.draw(batcher); //рисуем спрайт
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
