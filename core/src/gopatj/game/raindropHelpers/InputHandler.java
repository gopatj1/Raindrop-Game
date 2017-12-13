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

public class InputHandler implements InputProcessor {

    private Drop game;
    private MainMenuScreen mainMenu;

    private List<SimpleButton> menuButtons; //список кнопок

    private SimpleButton startButton; //кнопка старт
    private SimpleButton difficultButton; //кнопка сложности
    private SimpleButton helpButton; //кнопка помощь
    private SimpleButton rateButton; //кнопка оценка
    private SimpleButton exitButton; //кнопка выход
    private SimpleButton closeDifficultButton; //кнопка выход из сложности
    private SimpleButton closeHelpButton; //кнопка выход из помощи
    private SimpleButton difficultButtonTouch; //кнопка сложности касание
    private SimpleButton difficultButtonKeys; //кнопка сложности кнопки
    private SimpleButton difficultButtonAccelrometr; //кнопка сложности акселерометр

    private float scaleFactorX; //масштабируем касания (которые сейчас зависят от размера экрана)
    private float scaleFactorY; //масштабируем касания (которые сейчас зависят от размера экрана)

    public InputHandler(Drop game, MainMenuScreen menu,float scaleFactorX, float scaleFactorY) {

        this.game = game;
        this.mainMenu = menu;

        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

        //массив кнопок
        menuButtons = new ArrayList<SimpleButton>();

        //генерарируем кнопки
        startButton = new SimpleButton(480 / 2 - 90, 800 / 2 + 50, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        menuButtons.add(startButton); //добавляем в массив

        difficultButton = new SimpleButton(480 / 2 - 90, 800 / 2 - 50, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        menuButtons.add(difficultButton); //добавляем в массив

        helpButton = new SimpleButton(480 / 2 - 90, 800 / 2 - 150, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        menuButtons.add(helpButton); //добавляем в массив

        rateButton = new SimpleButton(480 / 2 - 90, 800 / 2 - 250, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        menuButtons.add(rateButton); //добавляем в массив

        exitButton = new SimpleButton(480 / 2 - 90, 800 / 2 - 350, 180, 60, AssetLoader.buttonUp, AssetLoader.buttonDown, true);
        menuButtons.add(exitButton); //добавляем в массив

        //одиночные кнопки
        closeDifficultButton = new SimpleButton(400, 470, 60, 60, AssetLoader.closeButtonUp, AssetLoader.closeButtonDown, true);
        closeHelpButton = new SimpleButton(400, 730, 60, 60, AssetLoader.closeButtonUp, AssetLoader.closeButtonDown, true);

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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //координаты нажатия
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        //если состояние меню
        if (mainMenu.isMenu()) {
            //нажимаем кнопку старт
            startButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку сложности
            difficultButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку помощь
            helpButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку оценка
            rateButton.isTouchDown(screenX, screenY);

            //нажимаем кнопку выход
            exitButton.isTouchDown(screenX, screenY);
        }

        //если состояние сложность
        if (mainMenu.isDifficult()) {
            //нажимаем на кнопку касание
            difficultButtonTouch.isTouchDown(screenX, screenY);

            //нажимаем на кнопку кнопки
            difficultButtonKeys.isTouchDown(screenX, screenY);

            //нажимаем на кнопку акселлерометр
            difficultButtonAccelrometr.isTouchDown(screenX, screenY);

            //нажимаем кнопку закрыть
            closeDifficultButton.isTouchDown(screenX, screenY);
        }

        //если состояние помощь
        if (mainMenu.isHelp()) {
            //нажимаем кнопку закрыть
            closeHelpButton.isTouchDown(screenX, screenY);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //координаты нажатия
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        //кнопка старт отпущена
        if (startButton.isTouchUp(screenX, screenY)) {
            AssetLoader.mainMenuMusic.stop(); //остановка музыки
            game.setScreen(new GameScreen(game)); //переход в режим игры
            mainMenu.dispose();
            return true;
        }

        //кнопка сложности отпущена
        if (difficultButton.isTouchUp(screenX, screenY)) {
            mainMenu.setDifficult(); //открываем сложность
            return true;
        }

        //кнопка помощи отпущена
        if (helpButton.isTouchUp(screenX, screenY)) {
            mainMenu.setHelp(); //открываем помощь
            return true;
        }

        //кнопка выход отпущена
        if (rateButton.isTouchUp(screenX, screenY)) {
            //переход по ссылке в плей-маркет
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=gopatj.game");
            return true;
        }

        //кнопка выход отпущена
        if (exitButton.isTouchUp(screenX, screenY)) {
            mainMenu.dispose();
            game.dispose();
            System.exit(0); //выход из приложения
            return true;
        }

        //кнопка сложности касание отпущена
        if (difficultButtonTouch.isTouchUp(screenX, screenY)) {
            AssetLoader.setDifficult(1); //устанавливаем выбранную сложность
            return true;
        }

        //кнопка сложности кнопки отпущена
        if (difficultButtonKeys.isTouchUp(screenX, screenY)) {
            AssetLoader.setDifficult(2); //устанавливаем выбранную сложность
            return true;
        }

        //кнопка сложности акселлерометр отпущена
        if (difficultButtonAccelrometr.isTouchUp(screenX, screenY)) {
            AssetLoader.setDifficult(3); //устанавливаем выбранную сложность
            return true;
        }

        //кнопка закрыть отпущена
        if (closeDifficultButton.isTouchUp(screenX, screenY) || closeHelpButton.isTouchUp(screenX, screenY)) {
            mainMenu.setMenu(); //открываем меню
            return true;
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
            //если состояние меню, то выход из приложения
            if (mainMenu.isMenu()) {
                Gdx.input.setCatchBackKey(true); //сигнал нажатия кнопки назад
                mainMenu.dispose();
                game.dispose();
                System.exit(0); //выход из приложения
                return true;
            }

            //если состояние сложность, то переход к гланому меню
            if (mainMenu.isDifficult()) {
                Gdx.input.setCatchBackKey(true); //сигнал нажатия кнопки назад
                mainMenu.setMenu();
                return true;
            }

            //если состояние помощь, то переход к гланому меню
            if (mainMenu.isHelp()) {
                Gdx.input.setCatchBackKey(true); //сигнал нажатия кнопки назад
                mainMenu.setMenu();
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

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

    public List<SimpleButton> getMenuButtons() {
        return menuButtons;
    }

    public SimpleButton getCloseDifficultButton() {
        return closeDifficultButton;
    }

    public SimpleButton getCloseHelpButton() {
        return closeHelpButton;
    }

    public SimpleButton getDifficultButtonTouch() { return difficultButtonTouch; }

    public SimpleButton getDifficultButtonKeys() { return difficultButtonKeys; }

    public SimpleButton getDifficultButtonAccelrometr() { return difficultButtonAccelrometr; }
}

