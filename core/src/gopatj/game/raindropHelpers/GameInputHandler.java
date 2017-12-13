package gopatj.game.raindropHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

import gopatj.game.raindrop.Drop;
import gopatj.game.screens.GameScreen;
import gopatj.game.screens.MainMenuScreen;
import gopatj.game.ui.SimpleButton;

public class GameInputHandler implements InputProcessor {

    private Drop game;
    private GameScreen gameScreen;

    private List<SimpleButton> pauseMenuButtons; //список кнопок паузы
    private List<SimpleButton> gameoverButtons; //список кнопок геймовера
    private List<SimpleButton> moveButtons; //сенсорные кнопки управления

    private SimpleButton pauseButton; //кнопка пауза
    private SimpleButton continueButton; //кнопка продолжить
    private SimpleButton restartButton; //кнопка рестарт
    private SimpleButton toMainMenuButton; //кнопка в главное меню
    private SimpleButton exitButton; //кнопка выход
    private SimpleButton closePauseButton; //кнопка выход из паузы
    private SimpleButton touchLeftButton; //сенсорная кнопка влево
    private SimpleButton touchRightButton; //сенсорная кнопка вправо

    private float scaleFactorX; //масштабируем касания (которые сейчас зависят от размера экрана)
    private float scaleFactorY; //масштабируем касания (которые сейчас зависят от размера экрана)

    public GameInputHandler(Drop game, GameScreen gameScreen, float scaleFactorX, float scaleFactorY) {

        this.game = game;
        this.gameScreen = gameScreen;

        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

        //генерируем одиночные кнопки пауза и закрыть
        pauseButton = new SimpleButton(400, 720, 60, 60, AssetLoader.closeButtonUp, AssetLoader.closeButtonDown, true);
        closePauseButton = new SimpleButton(350, 580, 60, 60, AssetLoader.closeButtonUp, AssetLoader.closeButtonDown, true);

        //массив кнопок
        pauseMenuButtons = new ArrayList<SimpleButton>();
        gameoverButtons = new ArrayList<SimpleButton>();
        moveButtons = new ArrayList<SimpleButton>();

        //генерарируем кнопки в массивах
        continueButton = new SimpleButton(480 / 2 - 90, 800 / 2 + 100, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        pauseMenuButtons.add(continueButton); //добавляем в массив паузы

        restartButton = new SimpleButton(480 / 2 - 90, 800 / 2, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        pauseMenuButtons.add(restartButton); //добавляем в массив паузы
        gameoverButtons.add(restartButton); //добавляем в массив геймовера

        toMainMenuButton = new SimpleButton(480 / 2 - 90, 800 / 2 - 100, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        pauseMenuButtons.add(toMainMenuButton); //добавляем в массив паузы
        gameoverButtons.add(toMainMenuButton); //добавляем в массив геймовера

        exitButton = new SimpleButton(480 / 2 - 90, 800 / 2 - 200, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        pauseMenuButtons.add(exitButton); //добавляем в массив паузы
        gameoverButtons.add(exitButton); //добавляем в массив геймовера

        touchLeftButton = new SimpleButton(0, 0, 240, 150, AssetLoader.touchButtonLeft, AssetLoader.touchButtonLeft, false);
        touchRightButton = new SimpleButton(240, 0, 240, 150, AssetLoader.touchButtonRight, AssetLoader.touchButtonRight, false);
        moveButtons.add(touchLeftButton); //добавляем в массив сенсорных кнопок управления
        moveButtons.add(touchRightButton); //добавляем в массив сенсорных кнопок управления
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //координаты нажатия
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        //если состояние запущенна
        if(gameScreen.isRunning()) {
            //нажимаем кнопку пауза
            pauseButton.isTouchDown(screenX, screenY);

            //если установлена средняя сложность
            if (AssetLoader.getDifficult() == 2) {
                //нажимаем кнопку влево
                if (touchLeftButton.isTouchDown(screenX, screenY)) {
                    gameScreen.setLeftMove(true);
                    return true;
                }

                //нажимаем кнопку вправо
                if (touchRightButton.isTouchDown(screenX, screenY)) {
                    gameScreen.setRightMove(true);
                    return true;
                }
            }
        }

        //если состояние пауза
        if (gameScreen.isPause()) {
            //нажимаем кнопку продолжить
            continueButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку рестарт
            restartButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку в главное меню
            toMainMenuButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку выход
            exitButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку закрыть
            closePauseButton.isTouchDown(screenX, screenY);
        }

        //если состояние геймовер
        if (gameScreen.isGameover()) {
            //нажимаем на кнопку рестарт
            restartButton.isTouchDown(screenX, screenY);

            //нажимаем на кнопку в главное меню
            toMainMenuButton.isTouchDown(screenX, screenY);

            //нажимаем на кнопку выход
            exitButton.isTouchDown(screenX, screenY);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //координаты нажатия
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        //кнопка пауза отпущена
        if (pauseButton.isTouchUp(screenX, screenY)) {
            gameScreen.pause(); //переход в режим паузы
            return true;
        }

        //кнопка продолжить или закрыть отпущена
        if (continueButton.isTouchUp(screenX, screenY) || closePauseButton.isTouchUp(screenX, screenY)) {
            gameScreen.runOrContinue(); //переход в режим игры
            return true;
        }

        //кнопка рестарт отпущена
        if (restartButton.isTouchUp(screenX, screenY)) {
            gameScreen.restart(); //рестарт
            return true;
        }

        //кнопка в главное меню отпущена
        if (toMainMenuButton.isTouchUp(screenX, screenY)) {
            game.setScreen(new MainMenuScreen(game)); //смена экрана
            gameScreen.dispose();
            return true;
        }

        //кнопка выход отпущена
        if (exitButton.isTouchUp(screenX, screenY)) {
            game.dispose();
            gameScreen.dispose();
            System.exit(0); //выход из приложения
            return true;
        }

        //если установлена средняя сложность
        if (AssetLoader.getDifficult() == 2) {
            //нажимаем кнопку влево
            if (touchLeftButton.isTouchUp(screenX, screenY)) {
                gameScreen.setLeftMove(false);
                return true;
            }

            //нажимаем кнопку вправо
            if (touchRightButton.isTouchUp(screenX, screenY)) {
                gameScreen.setRightMove(false);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

        //нажата кнопка назад
        if(keycode == Input.Keys.BACK){
            //нажата во время запущенной игры ставим на паузу
            if (gameScreen.isRunning()) {
                Gdx.input.setCatchBackKey(true); //сигнал о нажатии кнопки назад
                gameScreen.pause(); //переход в режим паузы
                return true;
            }

            //нажата во время паузы или геймовеа, то переход в главное меню
            if (gameScreen.isPause() || gameScreen.isGameover()){
                Gdx.input.setCatchBackKey(true); //сигнал о нажатии кнопки назад
                game.setScreen(new MainMenuScreen(game)); //смена экрана
                gameScreen.dispose();
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) { return true; }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    public List<SimpleButton> getPauseMenuButtons() {
        return pauseMenuButtons;
    }

    public List<SimpleButton> getGameoverButtons() { return gameoverButtons; }

    public List<SimpleButton> getMoveButtons() { return moveButtons; }

    public SimpleButton getPauseButton() {
        return pauseButton;
    }

    public SimpleButton getClosePauseButton() {
        return closePauseButton;
    }
}

