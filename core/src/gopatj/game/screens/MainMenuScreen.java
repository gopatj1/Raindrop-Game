package gopatj.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.List;

import gopatj.game.raindrop.Drop;
import gopatj.game.raindropHelpers.AssetLoader;
import gopatj.game.raindropHelpers.InputHandler;
import gopatj.game.ui.SimpleButton;

public class MainMenuScreen implements Screen{

    private final Drop game;
    private OrthographicCamera camera; //область мира игры с разрешением
    private SpriteBatch batch; //для отрисовки 2д изображений
    private ShapeRenderer shapeRenderer; //отрисовщик форм и линии
    private float runTime = 0; //счетчик продолжительности запуска меню

    private List<SimpleButton> menuButtons; //лист из кнопок
    private SimpleButton closeDifficultButton, closeHelpButton; //кнопоки закрыть
    private SimpleButton difficultButtonTouch, difficultButtonKeys, difficultButtonAccelrometr; //кнопоки сложности и управления

    //переменная для определения в каком состоянии меню
    private GameState currentState;
    public enum GameState {
        MENU, DIFFICULT, HELP
    }

    public MainMenuScreen (Drop gam) {

        currentState = GameState.MENU; //состояние меню
        this.game = gam;

        //создаем объект неперевернутую ортографическую камеру с разрешением игры
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.gameWidth, game.gameHeight);
        batch = new SpriteBatch(); //создаем отрисовщик 2д изображений
        shapeRenderer = new ShapeRenderer(); //создаем отрисовщик форм и линий
        shapeRenderer.setProjectionMatrix(camera.combined);

        //говорим libGDX использовать наш новый InputHandler как его же собственный процессор
        //Gdx.input.setInputProcessor() принимает на вход объект типа InputProcessor. Так как мы реализовали InputProcessor
        //в нашем InputHandler, мы можем передать наш InputHandler на вход
        //и сам MainMenuScreen, то есть this на вход
        //а также параментры соотношения размеров экрана устройства к размерам игры
        Gdx.input.setInputProcessor(new InputHandler(game, this, game.screenWidth / game.gameWidth, game.screenHeight / game.gameHeight));

        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons(); //кнопки меню
        this.closeDifficultButton = ((InputHandler) Gdx.input.getInputProcessor()).getCloseDifficultButton(); //кнопка закрыть
        this.closeHelpButton = ((InputHandler) Gdx.input.getInputProcessor()).getCloseHelpButton(); //кнопка закрыть
        this.difficultButtonTouch = ((InputHandler) Gdx.input.getInputProcessor()).getDifficultButtonTouch(); //кнопка касание
        this.difficultButtonKeys = ((InputHandler) Gdx.input.getInputProcessor()).getDifficultButtonKeys(); //кнопка кнопки
        this.difficultButtonAccelrometr = ((InputHandler) Gdx.input.getInputProcessor()).getDifficultButtonAccelrometr(); //кнопка акселлерометр

        //музыка
        AssetLoader.mainMenuMusic.setLooping(true); //зацикливаем
        AssetLoader.mainMenuMusic.play(); //музыка главного меню
        AssetLoader.gameoverMusic.stop(); //останавливаем звук геймовера
    }

    //выводим меню
    private void drawMenuUI() {

        batch.begin();

        //логотип-надпись название игры
        AssetLoader.font.getData().setScale(1.8f); //размер
        AssetLoader.font.setColor(Color.valueOf("#000000")); //черный цвет для тени
        AssetLoader.font.draw(batch, "RA  NDR   P", 5 + 3, 750 - 4);
        AssetLoader.font.setColor(Color.ORANGE); //оранжевый цвет
        AssetLoader.font.draw(batch, "RA  NDR   P", 5, 750);
        batch.draw(AssetLoader.bombImage, 127, 680, 41, 95);
        batch.draw(AssetLoader.bucketImage, 354, 682, 64, 64);

        //рисуем кнопки
        for (SimpleButton button : menuButtons) {
            button.draw(batch);
        }

        //надпси на кнопках
        AssetLoader.font.getData().setScale(1f); //размер
        AssetLoader.font.setColor(Color.BROWN); //цвет
        AssetLoader.font.draw(batch, "START", 480 / 2 - 70, 800 / 2 + 100);
        AssetLoader.font.draw(batch, "HELP", 480 / 2 - 50, 800 / 2 - 100);
        AssetLoader.font.draw(batch, "EXIT", 480 / 2 - 50, 800 / 2 - 300);
        AssetLoader.font.getData().setScale(0.62f, 1f);
        AssetLoader.font.draw(batch, "DIFFICULTY", 480 / 2 - 84, 800 / 2 + 1);
        AssetLoader.font.getData().setScale(0.8f, 1f);
        AssetLoader.font.draw(batch, "RATE US", 480 / 2 - 75, 800 / 2 - 200);

        batch.end();
    }

    private void drawDifficult() {

        // Отрисуем Background цвет солжности
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(40, 190, 400, 300);
        shapeRenderer.end();

        batch.begin();

        //рисуем кнопки
        closeDifficultButton.draw(batch);
        AssetLoader.font.getData().setScale(1.1f, 0.7f);
        AssetLoader.font.draw(batch, "X", 415, 513);
        difficultButtonTouch.draw(batch);
        batch.draw(AssetLoader.touchIcon, 80, 360);
        difficultButtonKeys.draw(batch);
        batch.draw(AssetLoader.keysIcon, 217, 365);
        difficultButtonAccelrometr.draw(batch);
        batch.draw(AssetLoader.accelrometrIcon, 315, 370);

        //рисуем текст
        AssetLoader.font.getData().setScale(0.7f); //размер
        AssetLoader.font.setColor(Color.valueOf("#ffffff")); //белый цвет
        AssetLoader.font.draw(batch, "EASY", 70, 335);
        AssetLoader.font.draw(batch, "HARD", 320, 335);
        AssetLoader.font.getData().setScale(0.6f, 0.7f);
        AssetLoader.font.draw(batch, "MEDIUM", 180, 335);

        AssetLoader.font.getData().setScale(0.35f);
        AssetLoader.font.draw(batch, "Using touch\nscreen. Not\ninteresting.\nPlay if you\nare loser!", 60, 300);
        AssetLoader.font.draw(batch, "Using keys.\nExciting game\nprocess. Play\nif you are have\nexperience!", 180, 300);
        AssetLoader.font.draw(batch, "Using accele-\nrometer.Very\ndynamically.\nPlay if you are\ncrazy man!", 310, 300);

        batch.end();
    }

    private void drawHelp() {

        // Отрисуем Background цвет помощи
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(40, 30, 400, 720);
        shapeRenderer.end();

        batch.begin();

        //кнопка закрыть
        closeHelpButton.draw(batch);
        AssetLoader.font.getData().setScale(1.1f, 0.7f);
        AssetLoader.font.draw(batch, "X", 415, 773);

        //иконки
        batch.draw(AssetLoader.dropImage, 55, 428, 42 / 1.45f, 64 / 1.45f);
        batch.draw(AssetLoader.goldenImage, 200, 430, 45 / 1.7f, 64 / 1.6f);
        batch.draw(AssetLoader.iceImage, 335, 425, 44 / 1.8f, 83 / 2);
        batch.draw(AssetLoader.orangeImage, 53, 369, 74 / 1.8f, 68 / 1.8f);
        batch.draw(AssetLoader.alcoImage, 205, 362, 30 / 1.8f, 100 / 2);
        batch.draw(AssetLoader.bombImage, 335, 362, 44 / 1.8f, 89 / 1.7f);
        batch.draw(AssetLoader.lifeImage, 55, 320, 52 / 1.5f, 43 / 1.5f);
        batch.draw(AssetLoader.cloudImage, 50, 280, 354 / 7, 184 / 6);
        batch.draw(AssetLoader.windImage, 50, 240, 150 / 3, 90 / 3.3f);
        batch.draw(AssetLoader.stormImage, 50, 200, 206 / 3.8f, 93 / 3.8f);
        batch.draw(AssetLoader.pipeBonusImage, 60, 160, 49 / 1.8f, 50 / 1.8f);
        batch.draw(AssetLoader.goldenFeverBonusImage, 55, 115, 69 / 1.9f, 74 / 2);
        batch.draw(AssetLoader.waterHoseBonusImage, 55, 79, 50 / 1.4f, 56 / 1.8f);
        batch.draw(AssetLoader.defaultSpeedBonusImage, 55, 38, 78 / 2.2f, 71 / 2);

        //текст
        AssetLoader.font.getData().setScale(0.35f); //размер
        AssetLoader.font.setColor(Color.valueOf("#ffffff")); //белый цвет
        AssetLoader.font.draw(batch, "Collect the drops and bonuses to set a new\n" +
                                     "record. However, weather and trash will not\n" +
                                     "let you do this!\n" +
                                     "\n" +
                                     "Watch the number of lives and scores. If they\n" +
                                     "fall to 0, you lose!\n" +
                                     "\n" +
                                     "The more scores, the more difficult. The size\n" +
                                     "of the bucket and the drops decreases. About\n" +
                                     "it tells the scale of filling of the bucket", 50, 725);

        AssetLoader.font.getData().setScale(0.5f);
        AssetLoader.font.draw(batch, "Game objects", 150, 500);
        AssetLoader.font.draw(batch, "+10              +100            -100" +
                                     "\n\n" +
                                     "  -300             -500            -1000", 85, 455);

        AssetLoader.font.getData().setScale(0.32f); //размер
        AssetLoader.font.draw(batch, "+1 to lifes count\n" +
                                     "\n" +
                                     "Limit the visible area\n" +
                                     "\n" +
                                     "Changes the trajectory of the drops\n" +
                                     "\n" +
                                     "Increases the amount and speed of the\n" +
                                      "droplets" +
                                     "\n" +
                                     "A bonus that creates a drainage pipe that\n" +
                                     "catch useful objects" +
                                     "\n" +
                                     "A bonus that creates a lot of gold drops in\n" +
                                     "different place" +
                                     "\n" +
                                     "A bonus that creates a water hose and lots\n" +
                                     "of drops" +
                                     "\n" +
                                     "A bonus that sets the default speed of the\n" +
                                     "droplets that increases over time ", 110, 340);

        batch.end();
    }

    private void drawQuakeDrop(float delta) {

        //рисуем анимацию дрожжащих капель на крыше
        batch.begin();
        batch.draw((TextureRegion) AssetLoader.dropQuakeAnimation.getKeyFrame(delta), 21, 476);
        batch.draw((TextureRegion) AssetLoader.dropQuakeAnimation.getKeyFrame(delta), 100, 435);
        batch.draw((TextureRegion) AssetLoader.dropQuakeAnimation.getKeyFrame(delta), 184, 515);
        batch.draw((TextureRegion) AssetLoader.dropQuakeAnimation.getKeyFrame(delta), 261, 584);
        batch.draw((TextureRegion) AssetLoader.dropQuakeAnimation.getKeyFrame(delta), 345, 497);
        batch.draw((TextureRegion) AssetLoader.dropQuakeAnimation.getKeyFrame(delta), 410, 440);
        batch.end();
    }

    //моментально изменяем цвета кнопок сложности при выборе
    private void reinitDifficultButtons() {

        //сложность касание
        if (AssetLoader.getDifficult() == 1) //нажата
            difficultButtonTouch = new SimpleButton(50, 340, 120, 120, AssetLoader.difficultButtonChecked, AssetLoader.difficultButtonChecked, true);
        else //отпущенна
            difficultButtonTouch = new SimpleButton(50, 340, 120, 120, AssetLoader.difficultButtonUnChecked, AssetLoader.difficultButtonUnChecked, true);

        //сложность кнопки
        if (AssetLoader.getDifficult() == 2) //нажата
            difficultButtonKeys = new SimpleButton(180, 340, 120, 120, AssetLoader.difficultButtonChecked, AssetLoader.difficultButtonChecked, true);
        else //отпущенна
            difficultButtonKeys = new SimpleButton(180, 340, 120, 120, AssetLoader.difficultButtonUnChecked, AssetLoader.difficultButtonUnChecked, true);

        //сложность акселлерометр
        if (AssetLoader.getDifficult() == 3) //нажата
            difficultButtonAccelrometr = new SimpleButton(310, 340, 120, 120, AssetLoader.difficultButtonChecked, AssetLoader.difficultButtonChecked, true);
        else //отпущенна
            difficultButtonAccelrometr = new SimpleButton(310, 340, 120, 120, AssetLoader.difficultButtonUnChecked, AssetLoader.difficultButtonUnChecked, true);
    }

    @Override
    public void render(float delta) {

        runTime += delta; //счетчик продолжительности запуска меню
        camera.update(); //обновляем камеру
        batch.setProjectionMatrix(camera.combined); //использование матрицы камеры
        batch.begin();
        batch.draw(AssetLoader.menuBackgroundImage, 0, 0); //задний фон
        batch.end();

        //изменяем цвет кнопки в зависимости от выбора сложности
        reinitDifficultButtons();

        switch (currentState) {
            case MENU:
                drawQuakeDrop(runTime); //рисуем анимацию капель
                drawMenuUI(); //рисуем меню
                break;

            case DIFFICULT:
                drawQuakeDrop(runTime); //рисуем анимацию капель
                drawDifficult(); //рисуем сложность
                break;

            case HELP:
                drawHelp(); //рисуем помощь
                break;

            default:
                break;
        }
    }

    public void setMenu() {
        currentState = GameState.MENU;
    }

    public void setDifficult() {
        currentState = GameState.DIFFICULT;
    }

    public void setHelp() { currentState = GameState.HELP; }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isDifficult() {
        return currentState == GameState.DIFFICULT;
    }

    public boolean isHelp() {
        return currentState == GameState.HELP;
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}