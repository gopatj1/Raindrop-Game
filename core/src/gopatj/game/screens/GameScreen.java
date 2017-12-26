package gopatj.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gopatj.game.TweenAccessors.Value;
import gopatj.game.TweenAccessors.ValueAccessor;
import gopatj.game.raindrop.Drop;
import gopatj.game.raindropHelpers.AssetLoader;
import gopatj.game.raindropHelpers.GameInputHandler;
import gopatj.game.ui.SimpleButton;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {

	private final Drop game; //новая переменная класса Drop для обращения к batch, font

    private OrthographicCamera camera; //область мира игры с размешением
    private SpriteBatch batch; //для отрисовки 2д изображений
    private ShapeRenderer shapeRenderer; //отрисовщик формы и линии

    private Rectangle bucket; //ведро
    private Vector3 touchPos; //3х мерный вектор для ведра
    private Array<Rectangle> rainDrops; // капли
    private Array<Rectangle> lifeDrops; //падающая жизнь
    private Array<Rectangle> goldenDrops; //падающая золотая каля
    private Array<Rectangle> trashDrops; // мусор (сосульки, петарда и прочее)
    private Array<Rectangle> bonusPipeDrops; // падающий бонус-труба
    private Array<Rectangle> bonusGoldenFeverDrops; // падающий бонус золотая лихорадка
    private Array<Rectangle> bonusWaterHostDrops; // падающий бонус шланг
    private Array<Rectangle> bonusDefaultSpeedDrops; // падающий бонус стандартной скорости

    private float runTime = 0; //время в режиме запущенна
    private int score = 0; //счетчик очков
    private int lifes = 3; //счетчик жизней
    private int secondOverForSpeed = 0; //считает пойманные капли до 2х, и потом увеличивает speed на один
    private int speed = 0; //новая скорость капель после двух пойманных капель
    private int trashId; //тип падающего мусора
    private int rainDropWidth = 42; //ширина капли, зависящая от сосуда
    private int rainDropHeight = 64; //высота капли, зависящая от сосуда

    private int xPos = random(0, 480 - rainDropWidth); //случайная позиция для бонуса шланг
    private int count = 0; //количество капель с одной траекторие в бонусе шланг

    private float lastDropTime; //время последнего появления капли
    private float lastTrashTime; //время последнего появления мусора
    private float lastLifeTime; //время последнего появления падающей жизни
    private float lastGoldenTime; //время последнего появления золотой капли

    private float lastCloudTime; //время последнего появления тумана
    private float newCloudTime; //время нового тумана

    private float lastWindTime; //время последнего появления ветра
    private float newWindTime; //время нового ветра
    private float windDirectionTime; //время изменения направления ветра
    private float windX; //направление ветра

    private float lastRainstormTime; //время последнего появления шторма
    private float newRainstormTime; //время нового шторма

    private float lastPipesTime; //время последнего бонуса труб
    private float lastGoldenFeverTime; //время последнего бонуса золотой лихорадки
    private float lastWaterHoseTime; //время последнего бонуса шланга
    private float lastDefaultSpeedTime;  //время последнего бонуса стандартной скорости капель

    private boolean pipeActivated = false; //бонус труба, когда выключен капля видна
    private boolean goldenFeverActivated = false; //бонус золотая лихорадка
    private boolean waterHoseBonusActivated = false; //бонус сабвей выключен

    private boolean leftMove = false; //движение ведра влево
    private boolean rightMove = false; //движение ведра вправо

    //переменная для определения в каком состоянии игра
    private GameState currentGameState;
    private enum GameState { READY, RUNNING, PAUSE, GAMEOVER }

    private List<SimpleButton> pauseMenuButtons; //лист из кнопок паузы
    private List<SimpleButton> gameoverButtons; //лист из кнопок геймовера
    private List<SimpleButton> moveButtons; //сенсорные кнопки управления ведром
    private SimpleButton pauseButton, closePauseButton; //кнопоки пауза и закрыть

    //Tween Manager для вспышки и плавного перехода
    private TweenManager manager; //генерируем accessor
    private Value alpha = new Value(); //новые значения для прозрачности
    private Color transitionColor; //цвет вспышки

	public GameScreen (final Drop gam) {
        currentGameState = GameState.READY; //состояние игры запущенна
        this.game = gam;

		//создаем объект неперевернутую ортографическую камеру с разрешением игры
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.gameWidth, game.gameHeight);
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
		touchPos = new Vector3();

		//воспроизводим дождь и зацикливаем
        AssetLoader.rainMusic.setLooping(true);
        AssetLoader.rainMusic.play();

		//создаем объект сосуд, ширину и высоту укажем в зависимости от типа сосуда
		bucket = new Rectangle();
		bucket.x = 480 / 2 - 100 / 2; //впервые появляется в центре
		bucket.y = 10; //отсуп над землей

        //объявляем массивы падающих объектов
        rainDrops = new Array<Rectangle>(); //капли
        lifeDrops = new Array<Rectangle>(); //жизни
        goldenDrops = new Array<Rectangle>(); //золотые капли
        trashDrops = new Array<Rectangle>(); //мусор
        bonusPipeDrops = new Array<Rectangle>(); //бонус труба
        bonusGoldenFeverDrops = new Array<Rectangle>(); //бонус золотая лихорадка
        bonusWaterHostDrops = new Array<Rectangle>(); //бонус шланг
        bonusDefaultSpeedDrops = new Array<Rectangle>(); //бонус стандартной скорости

        //капли падающие от краев крыши после перехода из главного меню
        spawnFallObject(23, 476, rainDropWidth, rainDropHeight, rainDrops);
        spawnFallObject(103, 435, rainDropWidth, rainDropHeight, rainDrops);
        spawnFallObject(186, 515, rainDropWidth, rainDropHeight, rainDrops);
        spawnFallObject(263, 584, rainDropWidth, rainDropHeight, rainDrops);
        spawnFallObject(347, 497, rainDropWidth, rainDropHeight, rainDrops);
        spawnFallObject(412, 440, rainDropWidth, rainDropHeight, rainDrops);

		lastCloudTime = runTime; //для определения, когда был последний туман
		newCloudTime = 30f; //первый туман через 30 секунд

		lastWindTime = runTime; //для определения, когда был последний ветер
		newWindTime = 50f; //первый шторм через 50 секунд

		lastRainstormTime = runTime; //для определения, когда был последний шторм
		newRainstormTime = 70f; //первый шторм через 70 секунд

        //говорим libGDX использовать наш новый GameInputHandler как его же собственный процессор
        //Gdx.input.setInputProcessor() принимает на вход объект типа GameInputProcessor. Так как мы реализовали InputProcessor
        //в нашем GameInputHandler, мы можем передать наш GameInputHandler на вход
        //и сам GameScreen, то есть this на вход
        //а также параментры соотношения размеров экрана устройства к размерам игры
        Gdx.input.setInputProcessor(new GameInputHandler(game, this, game.screenWidth / game.gameWidth, game.screenHeight / game.gameHeight));

        this.pauseMenuButtons = ((GameInputHandler) Gdx.input.getInputProcessor()).getPauseMenuButtons(); //кнопки меню паузы
        this.gameoverButtons = ((GameInputHandler) Gdx.input.getInputProcessor()).getGameoverButtons(); //кнопк и меню геймовера
        this.pauseButton = ((GameInputHandler) Gdx.input.getInputProcessor()).getPauseButton(); //кнопка пауза
        this.closePauseButton = ((GameInputHandler) Gdx.input.getInputProcessor()).getClosePauseButton(); //кнопка закрыть
        this.moveButtons = ((GameInputHandler) Gdx.input.getInputProcessor()).getMoveButtons(); //сенсорные кнопки управления ведром

        //цвет для переходов и вспышек
        transitionColor = new Color();
        prepareTransition(0, 0, 0, 0f);
    }

    //выводим кнопку паузы
    private void drawPauseButton () {
        batch.begin();
        pauseButton.draw(batch);
        AssetLoader.font.getData().setScale(1.1f, 0.8f);
        AssetLoader.font.setColor(Color.WHITE);
        AssetLoader.font.draw(batch, "II", 410, 765);
        batch.end();
    }

    //выводим меню паузы
    private void drawPauseMenu () {

        // Отрисуем Background цвет
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(100, 160, 280, 450);
        shapeRenderer.end();
        batch.begin();

        //рисуем кнопки паузы
        for (SimpleButton button : pauseMenuButtons) {
            button.draw(batch);
        }
        closePauseButton.draw(batch);
        AssetLoader.font.getData().setScale(1.1f, 0.7f);
        AssetLoader.font.setColor(Color.WHITE);
        AssetLoader.font.draw(batch, "X", 365, 623);

        //надпси на кнопках
        AssetLoader.font.getData().setScale(0.9f, 1f); //размер
        AssetLoader.font.setColor(Color.BROWN); //цвет
        AssetLoader.font.draw(batch, "RESUME", 480 / 2 - 82, 800 / 2 + 150);
        AssetLoader.font.getData().setScale(0.85f, 1f);
        AssetLoader.font.draw(batch, "RESTART", 480 / 2 - 84, 800 / 2 + 50);
        AssetLoader.font.getData().setScale(1f);
        AssetLoader.font.draw(batch, "MENU", 480 / 2 - 65, 800 / 2 - 50);
        AssetLoader.font.draw(batch, "EXIT", 480 / 2 - 50, 800 / 2 - 150);

        batch.end();
    }

    //выводим таблицу рекордов и кнопки геймовера
    private void drawGameoverMenu () {

        batch.begin();

        //таблица рекордов
        batch.draw(AssetLoader.scoreBoardImage, 0, 470, 505, 335);
        AssetLoader.font.getData().setScale(0.6f); //размер
        AssetLoader.font.setColor(Color.SKY); //цвет
        AssetLoader.font.draw(batch, "          SCOREBOARD\n" +
                                     "                  TOP        YOU", 60, 660);

        //отображаем очки игрока, либо крестик в зависимости от уровня сложности
        AssetLoader.font.setColor(Color.CHARTREUSE); //зеленый цвет
        AssetLoader.font.draw(batch, "EASY          " + AssetLoader.prefs.getInteger("highscoreEasy"), 60, 585);
        if (AssetLoader.getDifficult() == 1)
            AssetLoader.font.draw(batch, "" + score, 320, 585); //очки игрока
        else
            batch.draw(AssetLoader.redCrossImage, 320, 565, 23, 23); //крестик

        AssetLoader.font.setColor(Color.GOLD); //желтый цвет
        AssetLoader.font.draw(batch, "MEDIUM   " + AssetLoader.prefs.getInteger("highscoreMedium"), 60, 552);
        if (AssetLoader.getDifficult() == 2)
            AssetLoader.font.draw(batch, "" + score, 320, 552); //очки игрока
        else
            batch.draw(AssetLoader.redCrossImage, 320, 530, 23, 23); //крестик

        AssetLoader.font.setColor(Color.valueOf("ff3333")); //красный цвет
        AssetLoader.font.draw(batch, "HARD        " + AssetLoader.prefs.getInteger("highscoreHard"), 60, 515);
        if (AssetLoader.getDifficult() == 3)
            AssetLoader.font.draw(batch, "" + score, 320, 515); //очки игрока
        else
            batch.draw(AssetLoader.redCrossImage, 320, 490, 23, 23); //крестик

        //рисуем кнопки геймовера
        for (SimpleButton button : gameoverButtons) {
            button.draw(batch);
        }

        //надпси на кнопках
        AssetLoader.font.getData().setScale(0.85f, 1f); //размер
        AssetLoader.font.setColor(Color.BROWN); //цвет
        AssetLoader.font.draw(batch, "RESTART", 480 / 2 - 84, 800 / 2 + 50);
        AssetLoader.font.getData().setScale(1f);
        AssetLoader.font.draw(batch, "MENU", 480 / 2 - 65, 800 / 2 - 50);
        AssetLoader.font.draw(batch, "EXIT", 480 / 2 - 50, 800 / 2 - 150);

        batch.end();
    }

    //выводим пользовательский интерфейс. Очки, жизни и шкалу наполнения
    private void drawGameUI () {

        //рисуем кнопки управления если средняя сложность
        if (AssetLoader.getDifficult() == 2)
            for (SimpleButton button : moveButtons) {
                button.draw(batch);
            }

        //отрисовываем тень текста с рекордом и жизнями
        AssetLoader.font.setColor(Color.valueOf("#000000")); //черный цвет шрифта
        AssetLoader.font.getData().setScale(1f); //размер шрифта
        AssetLoader.font.draw(batch, "SCORE: " + score, 20 + 3, 785 - 4); //текст очков
        AssetLoader.font.draw(batch, " X " + lifes, 70 + 3, 740 - 4); //текст жизней

        //отрисовываем основной текст с рекордом и жизнями
        AssetLoader.font.setColor(Color.valueOf("#ffbf00")); //золотой шрифт
        AssetLoader.font.draw(batch, "SCORE: " + score, 20, 785); //текст очков
        batch.draw(AssetLoader.lifeImage, 20, 698); //иконка сердца, счетчик жизней
        AssetLoader.font.draw(batch, " X " + lifes, 70, 740); //текст жизней
        batch.draw(AssetLoader.emptyTexture, 0, 0); //пустая текстура для shapeRenderer

        //отрисовываем шкалу наполнения сосуда
        if (score >= 0 && score < 8000) {
            // Отрисуем цвет, заполняющий шкалу
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.valueOf("#0099ff"));
            if (score < 500)
                shapeRenderer.rect(20, 480, 30, (int) (score / 2.5f));
            if (score >= 500 && score < 1500)
                shapeRenderer.rect(20, 480, 30, (int) ((score - 500) / 5f));
            if (score >= 1500 && score < 3000)
                shapeRenderer.rect(20, 480, 30, (int) ((score - 1500) / 7.5f));
            if (score >= 3000 && score < 5000)
                shapeRenderer.rect(20, 480, 30, (int) ((score - 3000) / 10f));
            if (score >= 5000 && score < 8000)
                shapeRenderer.rect(20, 480, 30, (int) ((score - 5000) / 15f));
            shapeRenderer.end();

            // Отрисуем белые линии шкалы
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(20, 480, 30, 200);
            shapeRenderer.end();
        }
    }

    //отрисовываем сосуд в зависимости от очков
    private void drawBucket () {
        if (score < 500)
            batch.draw(AssetLoader.basinImage, bucket.x, bucket.y); //рисуем тазик
        if (score >= 500 && score < 1500)
            batch.draw(AssetLoader.bucketImage, bucket.x, bucket.y); //рисуем ведро
        if (score >= 1500 && score < 3000)
            batch.draw(AssetLoader.collerImage, bucket.x, bucket.y); //рисуем куллер
        if (score >= 3000 && score < 5000)
            batch.draw(AssetLoader.glassImage, bucket.x, bucket.y); //рисуем стакан
        if (score >= 5000 && score < 8000)
            batch.draw(AssetLoader.waterBottleImage, bucket.x, bucket.y); //рисуем пластиковую бутылку
        if (score >= 8000)
            batch.draw(AssetLoader.handsImage, bucket.x, bucket.y); //рисуем ладони
    }

    //рисуем каплю
    private void drawDrop () {
        for (Rectangle rainDrop : rainDrops)
            if (pipeActivated) //если активирован бонус труб
                if ((rainDrop.y > 650 || rainDrop.y < 200 - rainDrop.height / 2)) { //если вне области трубы
                    if (score < 3000)
                        batch.draw(AssetLoader.dropImage, rainDrop.x, rainDrop.y); //то отображаем стандартную каплю
                    else
                        batch.draw(AssetLoader.smallDropImage, rainDrop.x, rainDrop.y); //то отображаем маленькую каплю

                } else ; //в области трубы, то не отрисовываем и ничего не пишем
            else { //если бонус не активен
                if (score < 3000)
                    batch.draw(AssetLoader.dropImage, rainDrop.x, rainDrop.y); //то отображаем стандартную каплю
                else
                    batch.draw(AssetLoader.smallDropImage, rainDrop.x, rainDrop.y); //то отображаем маленькую каплю
            }
    }

    //рисуем жизнь
    private void drawLife () {
        for (Rectangle lifeDrop : lifeDrops)
            if (pipeActivated) //если активирован бонус трубы
                if ((lifeDrop.y > 650 || lifeDrop.y < 200 - lifeDrop.height / 2)) { //если вне области трубы
                    batch.draw(AssetLoader.lifeImage, lifeDrop.x, lifeDrop.y);
                } else ;//если в области трубы, то ниичего не отрисовываем и ничего не пишем
            else { //если бонус не активен
                batch.draw(AssetLoader.lifeImage, lifeDrop.x, lifeDrop.y); //рисуем всегда, если бонус не активен
            }
    }

    //рисуем золотую каплю
    private void drawGoldenDrop () {
        for (Rectangle goldenDrop : goldenDrops)
            if (pipeActivated) //если активирован бонус труб
                if ((goldenDrop.y > 650 || goldenDrop.y < 200 - goldenDrop.height / 2)) { //если вне области трубы
                    batch.draw(AssetLoader.goldenImage, goldenDrop.x, goldenDrop.y); //рисуем
                } else ;//если вне области трубы, то не отрисовываем и ничего не пишем
            else { //если бонус не активен
                batch.draw(AssetLoader.goldenImage, goldenDrop.x, goldenDrop.y); //рисуем всегда, если бонус не активен
            }
    }

    //рисуем мусор
    private void drawTrash () {
        for (Rectangle trashDrop : trashDrops) {
            switch (trashId) {
                case 1:
                    batch.draw(AssetLoader.iceImage, trashDrop.x, trashDrop.y); //рисуем лед
                    break;
                case 2:
                    batch.draw(AssetLoader.bombImage, trashDrop.x, trashDrop.y); //рисуем бомбу
                    break;
                case 3:
                    batch.draw(AssetLoader.alcoImage, trashDrop.x, trashDrop.y); //рисуем шампанское
                    break;
                case 4:
                    batch.draw(AssetLoader.orangeImage, trashDrop.x, trashDrop.y); //рисуем мандарин
                    break;
            }
        }
    }

    //рисуем трубу
    private void drawPipes () {
        if (pipeActivated) { //активирован бонус труба
            batch.draw(AssetLoader.pipeTopImage, 0, 625); //рисуем трубу (верхнаяя часть)
            batch.draw(AssetLoader.pipeBottomImage, 279, 625, 0, 0, 424, 111, 1, 1, -90); //рисуем трубу (нижняя часть)
        }
    }

    //рисуем водопроводный шланг
    private void drawWaterHose () {
        //если активирован бонус шланг
        if (waterHoseBonusActivated) {
            if (pipeActivated) //если активирован бонус труба
                batch.draw(AssetLoader.waterHoseImage, xPos - 52, 700); //рисуем шланг над трубой
            else
                batch.draw(AssetLoader.waterHoseImage, xPos - 52, 600); //рисуем шланг
        }
    }

    //рисуем падающие бонусы
    private void drawFallingBonuses () {
        //рисуем падающий бонус-трубу
        for (Rectangle bonusPipeDrop : bonusPipeDrops)
            batch.draw(AssetLoader.pipeBonusImage, bonusPipeDrop.x, bonusPipeDrop.y);
        //рисуем падающий бонус шланг
        for (Rectangle bonusWaterHostDrop : bonusWaterHostDrops)
            batch.draw(AssetLoader.waterHoseBonusImage, bonusWaterHostDrop.x, bonusWaterHostDrop.y);
        //рисуем падающий бонус золотую лихорадку
        for (Rectangle bonusGoldenDropGolden : bonusGoldenFeverDrops)
            batch.draw(AssetLoader.goldenFeverBonusImage, bonusGoldenDropGolden.x, bonusGoldenDropGolden.y);
        //рисуем падающий бонус стандартной скорости
        for (Rectangle bonusDefaultSpeedDrop : bonusDefaultSpeedDrops)
            batch.draw(AssetLoader.defaultSpeedBonusImage, bonusDefaultSpeedDrop.x, bonusDefaultSpeedDrop.y);
    }

    //рисуем туман и облако
    private void drawCloud () {
        //туман, заполняющий облаком пол экрана
        if (runTime - lastCloudTime > newCloudTime) {
            batch.draw(AssetLoader.cloudImage, -200, 300, 850, 550); //рисуем туман
            //туман закончился через 10-20 секунд
            if (runTime - lastCloudTime > newCloudTime + random(10f, 20f)) {
                newCloudTime = random(25f, 50f); //новый туман через 25-50 секунд
                lastCloudTime = runTime; //фиксируем последнее время тумана
            }
        }
    }

    //рисуем ветер
    private void drawWind () {
        //ветер, двигающий капли вбок
        if (runTime - lastWindTime > newWindTime) {
            AssetLoader.windMusic.play(); //воспроизводим звук ветра
            batch.draw(AssetLoader.windImage, -100, 420, 150 * 2, 90 * 2); //рисуем ветер
            batch.draw(AssetLoader.windImage, -40, 580, 150 * 2, 90 * 2); //рисуем ветер
            batch.draw(AssetLoader.windImage, 170, 400, 150 * 2, 90 * 2); //рисуем ветер
            batch.draw(AssetLoader.windImage, 200, 600, 150 * 2, 90 * 2); //рисуем ветер
        }
    }

    //рисуем грозу и шторм
    private void drawRainstorm () {
        //шторм, увеличивающий кол-во и скоростю капель
        if ((runTime - lastRainstormTime >  newRainstormTime) || isReady()) {
            AssetLoader.rainstormMusic.play(); //воспроизводим звук грома
            batch.draw(AssetLoader.stormImage, -100, 500, 206 * 2, 93 * 2); //рисуем молнию
            batch.draw(AssetLoader.stormImage, 70, 620, 196 * 2, 92 * 2); //рисуем молнию
            batch.draw(AssetLoader.stormImage, 200, 500, 196 * 2, 92 * 2); //рисуем молнию
        }
    }

    //фнкция порождения падающих объектов
    private void  spawnFallObject(int x, int y, int width, int height, Array<Rectangle> objects) {
        Rectangle object = new Rectangle(); //падающий объект
        object.width = width; //ширина
        object.height = height; //высота
        object.x = x; //позвиция по х
        object.y = y; //позвиция по y
        objects.add(object); //добавляем к массиву объектов
    }

    //фнкция порождения мусора
    private void spawnTrash(int trashId) {
        //тип мусора
        switch (trashId) {
            case 1: //сосулька
                spawnFallObject(random(0, 480 - 44), 800, 44, 83, trashDrops);
                break;
            case 2: //петарда
                spawnFallObject(random(0, 480 - 44), 800, 44, 99, trashDrops);
                break;
            case 3: //шампанское
                spawnFallObject(random(0, 480 - 30), 800, 30, 100, trashDrops);
                break;
            case 4: //мандарин
                spawnFallObject(random(0, 480 - 74), 800, 74, 68, trashDrops);
                break;
        }
        lastTrashTime = runTime; //фиксируем время создания мусора
    }

    //порождаем все падающие объекты
    private void spawnAllFallObjects () {
        //порождаем каплю
        //если активирован бонус шланг
        if (waterHoseBonusActivated) {
            if (runTime - lastDropTime > 0.2f) { //создаем много капель
                if (pipeActivated) //по одной линии, ниже высоты экрана, для эффекта, что течет из шланга и попадает в трубу
                    spawnFallObject(xPos, 700 - rainDropHeight / 2, rainDropWidth, rainDropHeight, rainDrops);
                else //по одной линии, ниже высоты экрана, для эффекта, что течет из шланга
                    spawnFallObject(xPos, 600 - rainDropHeight / 2, rainDropWidth, rainDropHeight, rainDrops);
                lastDropTime = runTime; //фиксируем время создания последней капли
                count++; //увеличиваем счетчик капель в этой линии
            }

            if (count > random(3, 6)) { //если счетчик капель в линии достиг лимита
                //то меняем место новой линии
                //если не выходит за границы экрана, то в создаем новую линию в случайном месте
                if (xPos + 150 + rainDropWidth <= 480 && xPos - 150 >= 0)
                    xPos += random(-150, 150);
                else if (xPos + 150 + rainDropWidth > 480) //выходит за правую границу
                    xPos += random(-150, 0);
                else if (xPos - 150 < 0) //выходит за левую границу
                    xPos += random(0, 150);
                count = 0;
            }
        } else //если бонус шланг не активен
            //создаем новую каплю через 1 секунду в случайном месте по ширине и вверху экрана
            if (runTime - lastDropTime > 1f) {
                spawnFallObject(random(0, 480 - rainDropWidth), 800, rainDropWidth, rainDropHeight, rainDrops);
                lastDropTime = runTime;
            }

        //порождаем падающую жизнь
        if (runTime - lastLifeTime > random(50f, 100f)) {
            spawnFallObject(random(0, 480 - 52), 800, 52, 43, lifeDrops);
            lastLifeTime = runTime;
        }

        //порождаем золотые капли
        if (!goldenFeverActivated) {//если бонус золотая лихорадка не активен, пораждаем новую золотую каплю
            if (score > 200)
                if (runTime - lastGoldenTime > random(10f, 30f)) {
                    spawnFallObject(random(0, 480 - 45), 800, 45, 64, goldenDrops);
                    lastGoldenTime = runTime;
                }
        }

        //порождаем мусор каждые 3 секунды
        if (score > 100)
            if (runTime - lastTrashTime > 3f)
                spawnTrash(trashId = random(1, 4));

        //порождаем бонус трубу
        if (score > 800)
            if (runTime - lastPipesTime > random(60f, 240f)) {
                spawnFallObject(random(0, 480 - 49), 800, 49, 50, bonusPipeDrops);
                lastPipesTime = runTime; //фиксируем время создания бонуса трубы
            }

        //порождаем бонус водопроводный шланг
        if (score > 1200)
            if (runTime - lastWaterHoseTime > random(60f, 240f)) {
                spawnFallObject(random(0, 480 - 78), 800, 78, 71, bonusWaterHostDrops);
                lastWaterHoseTime = runTime; //фиксируем время создания бонуса шлнга
            }

        //порождаем бонус золотую лихорадку
        if (score > 2500)
            if (runTime - lastGoldenFeverTime > random(60f, 240f)) {
                spawnFallObject(random(0, 480 - 69), 800, 69, 74, bonusGoldenFeverDrops);
                lastGoldenFeverTime = runTime; //фиксируем время создания бонуса золотой лихорадки
            }

        //порождаем бонус стандартной скорости
        if (score > 3500)
            if (runTime - lastDefaultSpeedTime > random(60f, 240f)) {
                spawnFallObject(random(0, 480 - 50), 800, 50, 56, bonusDefaultSpeedDrops);
                lastDefaultSpeedTime = runTime; //фиксируем время создания бонуса стандартной скорости
            }
    }

    //перемещаем сосуд в зависимости от сложности
    private void moveBucket () {
        //если сложность легко - касания
        if (AssetLoader.getDifficult() == 1) {
            //если прикоснулись к экрану, то перемещаем ведро
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0); //берем координаты нажатия
                camera.unproject(touchPos); //преобразование в систему кординат камеры
                //если точка касания не на координатах кнопки пауза
                if ( !((touchPos.x >= 400 && touchPos.x <= 460) && (touchPos.y >= 720 && touchPos.y <= 780)) ) {
                    bucket.x = (int) (touchPos.x - bucket.width / 2); //устанавливаем ведру координату
                }
            }
        }

        //если сложность средне - кнопки
        if (AssetLoader.getDifficult() == 2) {
            //если нажата клавиша перемещаем сосуд на 400пикселей в секунду
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftMove)
                bucket.x -= 600 * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightMove)
                bucket.x += 600 * Gdx.graphics.getDeltaTime();
        }

        //если сложность тяжело - акселлерометр
        if (AssetLoader.getDifficult() == 3) {
            //перемещение сосуда с помощью акселероментра
            float accelerometer = Gdx.input.getAccelerometerX(); //значения акселерометра
            float speed = 220; //ускоряем перемещение
            if (Math.abs(accelerometer) > 0.3f) // если значение < -0.3 и > 0.3, значит двигаем
                if (Math.abs(accelerometer) < 5f) // если значение < 5 и > 5, значит двигаем
                    bucket.x -= accelerometer * speed * Gdx.graphics.getDeltaTime();
                else if (accelerometer > 5f){ //лимит на скорость передвижения
                    accelerometer = 5;
                    bucket.x -= accelerometer * speed * Gdx.graphics.getDeltaTime();
                }
                else if (accelerometer < -5f){ //лимит на скорость передвижения
                    accelerometer = -5;
                    bucket.x -= accelerometer * speed * Gdx.graphics.getDeltaTime();
                }
        }

        //проверка не выходит ли за границы экрана сосуд
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 480 - bucket.width) bucket.x = 480 - bucket.width;
    }

    //изменяем тип сосуда и размер капли
    private void changeBucketType () {

        //изначально установлено в качестве сосуда тазик
        if(score < 500) {
            bucket.width = 99;
            bucket.height = 50;

            //изменяем каплю на стандартную
            rainDropWidth = 42;
            rainDropHeight = 64;
        }

        //изменение сосуда с тазика на ведро
        if(score >= 500 && score < 1500) {
            bucket.width = 64;
            bucket.height = 64;

            //изменяем каплю на стандартную
            rainDropWidth = 42;
            rainDropHeight = 64;
        }

        //изменение сосуда с тазика на куллер
        if(score >= 1500 && score < 3000) {
            bucket.width = 47;
            bucket.height = 80;

            //изменяем каплю на стандартную
            rainDropWidth = 42;
            rainDropHeight = 64;
        }

        //изменение сосуда с куллера на кружку
        if(score >= 3000 && score < 5000) {
            bucket.width = 44;
            bucket.height = 50;

            //изменяем каплю на маленькую
            rainDropWidth = 27;
            rainDropHeight = 45;

        }

        //изменение сосуда с кружки на бутылку
        if(score >= 5000 && score < 8000) {
            bucket.width = 35;
            bucket.height = 100;

            //изменяем каплю на маленькую
            rainDropWidth = 27;
            rainDropHeight = 45;
        }

        //изменение сосуда с бутылки на ладони
        if(score >= 8000) {
            bucket.width = 88;
            bucket.height = 100;

            //изменяем каплю на маленькую
            rainDropWidth = 27;
            rainDropHeight = 45;
        }
    }

    //устанавливаем непрерывное движение сосуда влево
    public void setLeftMove (boolean activateLeftMove) {
        if(rightMove && activateLeftMove) rightMove = false; //отключаем вправо
        leftMove = activateLeftMove;
    }

    //устанавливаем непрерывное движение сосуда вправо
    public void setRightMove (boolean activateRightMove) {
        if(leftMove && activateRightMove) leftMove = false; //отключаем влево
        rightMove = activateRightMove;
    }

    //падение капель
    public void moveDrops () {
        Iterator<Rectangle> iterDrop = rainDrops.iterator();
        while (iterDrop.hasNext()) {
            Rectangle rainDrop = iterDrop.next();

            if (!pipeActivated) { //не активирован бонус труб
                //ветер, двигающий капли вбок
                if (runTime - lastWindTime > newWindTime) {
                    //каждые пол секунды меняем направление капель
                    if (runTime - windDirectionTime > 0.5f) {
                        windX = random(-200, 200); //отпределяем направление
                        windDirectionTime = runTime; //фиксируем время изменения направления капель
                    }
                    rainDrop.x += windX * Gdx.graphics.getDeltaTime(); //меняем нарпавление

                    //ветер закончился через 10-20 секунд
                    if (runTime - lastWindTime >  newWindTime + random(10f, 20f)) {
                        lastWindTime = runTime; //фиксируем последний ветер
                        newWindTime = random(25f, 50f); //новый ветер через 25-50 секунд
                        AssetLoader.windMusic.stop(); //останавливаем звук ветра, некрасиво прерывается
                    }
                }
            }

            //шторм, увеличивающий кол-во и скоростю капель
            if (runTime - lastRainstormTime >  newRainstormTime) {
                //создаем новую каплю через 0,5 секунды в случайном месте по ширине и вверху экрана
                if (runTime - lastDropTime > 0.5f) {
                    spawnFallObject(random(0, 480 - rainDropWidth), 800, rainDropWidth, rainDropHeight, rainDrops);
                    lastDropTime = runTime;
                }

                //вниз 275 пикселей в секунду
                rainDrop.y -= (275 + speed) * Gdx.graphics.getDeltaTime();

                //шторм закончился через 10-20 секунд
                if (runTime - lastRainstormTime >  newRainstormTime + random(10f, 20f)) {
                    lastRainstormTime = runTime; //фиксируем последний шторм
                    newRainstormTime = random(25f, 50f); //новый шторм через 25-50 секунд
                }
            } else {//обычная скорость падения
                rainDrop.y -= (200 + speed) * Gdx.graphics.getDeltaTime(); //вниз 200 пикселей в секунду
            }


            //если капля за правой или левой границей или нижней экрана удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления изза частоты кадро
            if (rainDrop.x + rainDrop.width / 2 < 0 || rainDrop.x + rainDrop.width / 2 > 480 || rainDrop.y < 0) {
                if (rainDrop.y < 0) {//пересекается с дном
                    lifes--; //уменьшаем жизнь
                    AssetLoader.lostDropSound.play(); //звук упавшей кали
                    prepareTransition(255, 255, 255, .3f); //вспышка
                }
                isOverlaps = false; //не может больше пересекаться
                iterDrop.remove();
            }

            //капля пересекается с ведром
            if (rainDrop.overlaps(bucket) && isOverlaps) {
                score += 10; //увеличение кол-ва очков
                AssetLoader.dropCollectSound.play(); //воспроизводим звук капли
                iterDrop.remove(); //удаляем из массива
                if (speed < 200) { //увеличиваем скорось капли, ограничение 200 + обычная скорость, либо скорость во время шторма
                    secondOverForSpeed++;
                    if (secondOverForSpeed == 2) { //если пойманно две капли
                        speed++; //увеличиваем скорость на 1
                        secondOverForSpeed = 0; //обнуляем счетчик капель
                    }
                }
            }

            //капля попадает в область трубы и становится невидимой (будто пересекается с ней, но нет)
            if (rainDrop.y < 650 && rainDrop.y > 200 - rainDropHeight / 2 && pipeActivated) {
                rainDrop.x = 286; //перемещаем, чтоб капля оказалась на выходе из трубы
            }
        }
    }

    //падение мусора
    public void moveTrash () {
        Iterator<Rectangle> iterTrash = trashDrops.iterator();
        while (iterTrash.hasNext()) {
            Rectangle trashDrop = iterTrash.next();

            // скорость падения
            trashDrop.y -= 350 * Gdx.graphics.getDeltaTime(); //вниз 300 пикселей в секунду

            //если мусор за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления из-за частоты кадров
            if (trashDrop.y < 0) { //в зоне удаления
                iterTrash.remove();
                isOverlaps = false; //не может больше пересекаться
            }

            if (trashDrop.overlaps(bucket) && isOverlaps) { //мусор пересекается с ведром
                switch (trashId) { //в зависимости от вида мусора выполняем действие
                    case 1:
                        score -= 100; //уменьшение очков
                        AssetLoader.iceCollectSound.play(); //воспроизводим звук пойманного мусора
                        break;
                    case 2:
                        score -= 1000; //уменьшение очков
                        AssetLoader.bombCollectSound.play(); //воспроизводим звук пойманного мусора
                        break;
                    case 3:
                        score -= 500; //уменьшение очков
                        AssetLoader.bottleCollectSound.play(); //воспроизводим звук пойманного мусора
                        break;
                    case 4:
                        score -= 300; //уменьшение очков
                        AssetLoader.orangeCollectSound.play(); //воспроизводим звук пойманного мусора
                        break;
                }
                iterTrash.remove(); //удаляем из массива
            }
            if (pipeActivated) //активирован бонус труб
                if (trashDrop.y < 650 && trashDrop.y > 600) { //если мусор падает на трубу
                    iterTrash.remove(); //удаляем его
                    AssetLoader.trashOnPipeSound.play();//воспроизводим звук
                }
        }
    }

    //падение сердца
    public void moveLife () {
        Iterator<Rectangle> iterLife = lifeDrops.iterator();
        while (iterLife.hasNext()) {
            Rectangle lifeDrop = iterLife.next();

            // скорость падения
            lifeDrop.y -= 550 * Gdx.graphics.getDeltaTime(); //вниз 550 пикселей в секунду

            //если сердце за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления изза частоты кадров
            if (lifeDrop.y < 0) { //в зоне удаления
                iterLife.remove();
                isOverlaps = false; //не может больше пересекаться
            }

            if (lifeDrop.overlaps(bucket) && isOverlaps) { //сердце пересекается с ведром
                lifes++;
                AssetLoader.bonusCollectSound.play();
                iterLife.remove(); //удаляем из массива
            }

            //жизнь попадает в область трубы и становится невидимой (будто пересекается с ней, но нет)
            if (lifeDrop.y < 650 && lifeDrop.y > 200 && pipeActivated) {
                lifeDrop.x = 286; //перемещаем, чтоб жизнь оказалась на выходе из трубы
            }
        }
    }

    //падение золотой капли
    public void moveGoldenDrop () {
        Iterator<Rectangle> iterGoldenDrop = goldenDrops.iterator();
        while (iterGoldenDrop.hasNext()) {
            Rectangle goldenDrop = iterGoldenDrop.next();

            // скорость падения
            goldenDrop.y -= 550 * Gdx.graphics.getDeltaTime(); //вниз 550 пикселей в секунду

            //если капля за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления из-за частоты кадров
            if (goldenDrop.y < 0) { //в зоне удаления
                iterGoldenDrop.remove();
                isOverlaps = false; //не может больше пересекаться
            }

            if (goldenDrop.overlaps(bucket) && isOverlaps) { //золотая капля пересекается с ведром
                score += 100;
                AssetLoader.bonusCollectSound.play();
                iterGoldenDrop.remove(); //удаляем из массива
            }

            //золотая капля попадает в область трубы и становится невидимой (будто пересекается с ней, но нет)
            if (goldenDrop.y < 650 && goldenDrop.y > 200 && pipeActivated) {
                goldenDrop.x = 286; //перемещаем, чтоб золотая капля оказалась на выходе из трубы
            }
        }
    }

    //падение бонуса трубы
    public void movePipeBonus () {
        Iterator<Rectangle> iterPipeBonus = bonusPipeDrops.iterator();
        while (iterPipeBonus.hasNext()) {
            Rectangle bonusDrop = iterPipeBonus.next();

            bonusDrop.y -= 550 * Gdx.graphics.getDeltaTime(); //вниз 550 пикселей в секунду

            //если бонус за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления из-за частоты кадров
            if (bonusDrop.y < 0) { //внизу экрана
                iterPipeBonus.remove();
                isOverlaps = false; //не может больше пересекаться
            }

            if (bonusDrop.overlaps(bucket) && isOverlaps) { //бонус пересекается с ведром
                pipeActivated = true; //бонус активирован
                iterPipeBonus.remove(); //удаляем из массива
            }
        }
        if (pipeActivated)
            AssetLoader.pipeBonusMusic.play(); //звук воды в трубе

        //бонус-труба закончился через 15 секунд
        if (runTime - lastPipesTime > 15f) {
            pipeActivated = false; //бонус деактивирован
            AssetLoader.pipeBonusMusic.stop(); //останавливаем звук воды в трубе
        }
    }

    //падение бонуса золотой лихорадки
    public void moveGoldenFeverBonus () {
        Iterator<Rectangle> iterGoldenFeverBonus = bonusGoldenFeverDrops.iterator();
        while (iterGoldenFeverBonus.hasNext()) {
            Rectangle bonusDrop = iterGoldenFeverBonus.next();

            bonusDrop.y -= 550 * Gdx.graphics.getDeltaTime(); //вниз 550 пикселей в секунду

            //если бонус за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления из-за частоты кадров
            if (bonusDrop.y < 0) { //внизу экрана
                iterGoldenFeverBonus.remove();
                isOverlaps = false; //не может больше пересекаться
            }
            if (bonusDrop.overlaps(bucket) && isOverlaps) { //бонус пересекается с ведром
                goldenFeverActivated = true; //бонус активирован
                iterGoldenFeverBonus.remove(); //удаляем из массива
            }
        }
        if (goldenFeverActivated) { //пока активен бонус
            AssetLoader.goldenFeverMusic.play(); //звук золотой лихорадки
            if (runTime - lastGoldenTime > 0.5f) { //создаем много золотых капель
                spawnFallObject(random(0, 480 - 45), 800, 45, 64, goldenDrops);
                lastGoldenTime = runTime; //фиксируем ремя создания последней золотой капли
            }
        }
        if (runTime - lastGoldenFeverTime > 10f) { //если продолжительность лихорадки 10с
            goldenFeverActivated = false; //то отключаем лихорадку
            AssetLoader.goldenFeverMusic.stop(); //останавливаем музыку
        }
    }

    //падение бонуса водопроводного шланга
    public void moveWaterHoseBonus () {
        Iterator<Rectangle> iterWaterHostBonus = bonusWaterHostDrops.iterator();
        while (iterWaterHostBonus.hasNext()) {
            Rectangle bonusDrop = iterWaterHostBonus.next();

            bonusDrop.y -= 550 * Gdx.graphics.getDeltaTime(); //вниз 550 пикселей в секунду

            //если бонус за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления из-за частоты кадров
            if (bonusDrop.y < 0) { //внизу экрана
                iterWaterHostBonus.remove();
                isOverlaps = false; //не может больше пересекаться
            }

            if (bonusDrop.overlaps(bucket) && isOverlaps) { //бонус пересекается с ведром
                waterHoseBonusActivated = true; //бонус активирован
                iterWaterHostBonus.remove(); //удаляем из массива
            }
        }
        if (waterHoseBonusActivated)
            AssetLoader.waterHoseBonusMusic.play(); //звук бонуса

        //бонус шданг закончился через 15 секунд
        if (runTime - lastWaterHoseTime > 15f) {
            waterHoseBonusActivated = false; //бонус деактивирован
            AssetLoader.waterHoseBonusMusic.stop(); //останавливаем звук
        }
    }

    //падение бонуса стандартной скорости
    public void moveDefaultSpeedBonus () {
        Iterator<Rectangle> iterDefaultSpeedBonus = bonusDefaultSpeedDrops.iterator();
        while (iterDefaultSpeedBonus.hasNext()) {
            Rectangle bonusDrop = iterDefaultSpeedBonus.next();

            bonusDrop.y -= 550 * Gdx.graphics.getDeltaTime(); //вниз 550 пикселей в секунду

            //если бонус за экраном удаляем из массива
            boolean isOverlaps = true; //для исключения двойного удаления из-за частоты кадров
            if (bonusDrop.y < 0) { //внизу экрана
                iterDefaultSpeedBonus.remove();
                isOverlaps = false; //не может больше пересекаться
            }

            if (bonusDrop.overlaps(bucket) && isOverlaps) { //бонус пересекается с ведром
                speed = 0; //изменяем скорость капель на стандартную
                AssetLoader.bonusCollectSound.play();
                iterDefaultSpeedBonus.remove(); //удаляем из массива
            }
        }

        //изменяем тип сосуда и размер капли
        changeBucketType ();

        //игра проиграна
        if (lifes == 0 || score < 0) {
            currentGameState = GameState.GAMEOVER;
            AssetLoader.gameoverMusic.play(); //звук геймовера
        }
    }

	@Override
	public void render (float delta) {
        // Добавим лимит для нашей delta, так что если игра начнет тормозить
        // при обновлении, мы не нарушим нашу логику определения колизии
        if (delta > .15f) delta = .15f;

        camera.update(); //обновляем камеру
        batch.setProjectionMatrix(camera.combined); //использование матрицы камеры
        batch.begin(); //начинаем рисование

        //состояние говотновсти
        if (isReady()) ready(delta);

        //любое состояние игры кроме готовности
        if (!isReady()) batch.draw(AssetLoader.gameBackgroundImage, 0, 0); //задний фон

        //игра в состоянии запуенна или пауза, но не геймовер и не готовность
        if (isRunning() || isPause()) {
            drawBucket(); //отрисовываем сосуд в зависимости от очков
            drawDrop(); //рисуем каплю
            drawLife(); //рисуем жизнь
            drawGoldenDrop(); //рисуем золотую каплю
            drawTrash(); //рисуем мусор
            drawPipes(); //рисуем трубы
            drawWaterHose(); //рисуем водопроводный шланг
            drawFallingBonuses(); //рисуем падающие бонусы
            drawCloud(); //рисуем туман и облако
            drawWind(); //рисуем ветер
            drawRainstorm(); //рисуем шторм и грозу

            //выводим пользовательский интерфейс. Очки, жизни и шкалу наполнения
            drawGameUI();
        }
        batch.end(); //заканчиваем рисование

		//игра в состоянии запущенна
		if(isRunning()) {
            runTime += delta; //фиксируем как долго шла игра
            runOrContinue(); //устанавливаем состояние запущенна
            spawnAllFallObjects(); //порождаем все падающие объекты

            //двигаем объекты
            moveBucket(); //перемещаем сосуд
            moveDrops(); //падение капель
            moveTrash(); //падение мусора
            moveLife(); //падение сердца
            moveGoldenDrop(); //падение золотых капель
            movePipeBonus(); //падение бонуса трубы
            moveGoldenFeverBonus(); //падение бонуса золотой лихорадки
            moveWaterHoseBonus(); //падение бонуса водопроводного шланга
            moveDefaultSpeedBonus(); //падение бонуса стандартной скорости

            drawPauseButton();
            drawTransition(delta);
        }

        //состояние паузы или геймовера
        if (isGameover()) gameOver();
        if (isPause()) pause();

    }

    @Override
	public void resize(int width, int height) {

	}

	//пауза, которая срабатывает при нажатии на кнопку паузу, назад, домой или диспетчер приложения на смартфоне
    @Override
	public void pause() {
        //проверяем какое состояние игры установить при разворачивании игры из трея
        if (isReady()) { //состояние готовности и падение капель с крыши
            currentGameState = GameState.READY;
        }
        else if (isGameover()) { //состояние геймовера
            gameOver();
        }
        else { //меню паузы с кнопками
            currentGameState = GameState.PAUSE;
            drawPauseMenu();

            AssetLoader.rainMusic.stop(); //отключаем мелодию дождя
            AssetLoader.windMusic.stop(); //отключаем мелодию ветра
            AssetLoader.rainstormMusic.stop(); //отключаем мелодию шторма
            AssetLoader.goldenFeverMusic.stop(); //отключаем мелодию бонуса
            AssetLoader.pipeBonusMusic.stop(); //отключаем мелодию бонуса
            AssetLoader.waterHoseBonusMusic.stop(); //отключаем мелодию бонуса
        }
    }

    public void ready(float delta) {
        //рисуем элементы
        batch.draw(AssetLoader.menuBackgroundImage, 0, 0);
        drawRainstorm(); //рисуем молнию и гром
        AssetLoader.font.getData().setScale(1.7f);
        AssetLoader.font.setColor(Color.BLACK); //тень текста
        AssetLoader.font.draw(batch, "GET READY!", 10 + 3, 750 - 4);
        AssetLoader.font.setColor(Color.ORANGE); //основной текст
        AssetLoader.font.draw(batch, "GET READY!", 10, 750);
        drawDrop();//рисуем капли

        //падение капель с крыши
        Iterator<Rectangle> iterDrop = rainDrops.iterator();
        while (iterDrop.hasNext()) {
            Rectangle rainDrop = iterDrop.next();
            rainDrop.y -= 250 * Gdx.graphics.getDeltaTime(); //вниз 250 пикселей в секунду

            //вспышки грома
            if (rainDrop.y >= 450 && rainDrop.y <= 460) {
                prepareTransition(255, 255, 255, .3f);
                drawTransition(delta);
            }

            //переход в состояние запущенна
            if (rainDrop.y < 100) {
                prepareTransition(0, 0, 0, 2f); //плавный переход
                rainDrops.clear(); //очищаем массив
                runOrContinue(); //переводим в состояние игры
            }
        }
    }

	public void runOrContinue() {
        currentGameState = GameState.RUNNING;
        AssetLoader.rainMusic.play();
    }

	public void restart() {
        currentGameState = GameState.RUNNING;
        runTime = 0;
        score = 0;
        lifes = 3;
        secondOverForSpeed = 0;
        speed = 0;
        bucket.x = 480 / 2 - 100 / 2; //возвращаем в центр

        //удаляем все падающие объекты
        rainDrops.clear();
        trashDrops.clear();
        lifeDrops.clear();
        goldenDrops.clear();
        bonusPipeDrops.clear();
        bonusGoldenFeverDrops.clear();
        bonusWaterHostDrops.clear();
        bonusDefaultSpeedDrops.clear();

        //обнуляем все счетчики создания новых объектов
        lastDropTime = 0;
        lastTrashTime = 0;
        lastLifeTime = 0;
        lastGoldenTime = 0;

        lastCloudTime = 0;
        newCloudTime = 30f;

        lastWindTime = 0;
        newWindTime = 60f;
        windDirectionTime = 0;

        lastRainstormTime = 0;
        newRainstormTime = 90f;

        lastPipesTime = 0;
        lastGoldenFeverTime = 0;
        lastWaterHoseTime = 0;
        lastDefaultSpeedTime = 0;

        //отключаем бонусы
        goldenFeverActivated = false;
        pipeActivated = false;
        waterHoseBonusActivated = false;

        //отключаем музыку и звуки
        AssetLoader.rainMusic.stop();
        AssetLoader.windMusic.stop();
        AssetLoader.rainstormMusic.stop();
        AssetLoader.goldenFeverMusic.stop();
        AssetLoader.pipeBonusMusic.stop();
        AssetLoader.waterHoseBonusMusic.stop();
        AssetLoader.gameoverMusic.stop();
	}

	private void gameOver() {
        currentGameState = GameState.GAMEOVER;
        drawGameoverMenu();

        rainDrops.clear(); //удаляем капли
        trashDrops.clear(); //удаляем мусор
        lifeDrops.clear(); //удаляем падающие жизни
        goldenDrops.clear(); //удаляем золотые капли
        bonusPipeDrops.clear(); //удаляем падающий бонус трубу
        bonusGoldenFeverDrops.clear(); //удаляем падающий бонус золотую лихорадку
        bonusWaterHostDrops.clear(); //удаляем падающий бонус шланг
        bonusDefaultSpeedDrops.clear(); //удаляем падающий бонус стандартной скорости
        goldenFeverActivated = false; //отключаем бонус
        pipeActivated = false; //отключаем бонус
        waterHoseBonusActivated = false; //отключаем бонус

        AssetLoader.rainMusic.stop(); //отключаем мелодию дождя
        AssetLoader.windMusic.stop(); //отключаем мелодию ветра
        AssetLoader.rainstormMusic.stop(); //отключаем мелодию шторма
        AssetLoader.goldenFeverMusic.stop(); //отключаем мелодию бонуса
        AssetLoader.pipeBonusMusic.stop(); //отключаем мелодию бонуса
        AssetLoader.waterHoseBonusMusic.stop(); //отключаем мелодию бонуса

        //устанавливаем новый рекорд
        if (score > AssetLoader.getHighScore()) AssetLoader.setHighScore(score);
    }

    public boolean  isReady() { return currentGameState == GameState.READY; }

    public boolean  isRunning() { return currentGameState == GameState.RUNNING; }

    public boolean  isPause() { return currentGameState == GameState.PAUSE; }

    public boolean  isGameover() { return currentGameState == GameState.GAMEOVER; }

    //сворачивание
	@Override
	public void hide() {
        pause();
    }

    //открыть после сворачивания
    @Override
    public void resume() {
        pause();
    }

	@Override
	public void dispose () {
        batch.dispose();
        shapeRenderer.dispose();
	}

	@Override
	public void show() {
    }

    //переход или вспышка
    private void prepareTransition(int r, int g, int b, float duration) {
        transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
        alpha.setValue(1);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, duration).target(0)
                .ease(TweenEquations.easeOutQuad).start(manager);
    }

    //отображение перехода или вспышки
    private void drawTransition(float delta) {
        if (alpha.getValue() > 0) {
            manager.update(delta);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(transitionColor.r, transitionColor.g,
                    transitionColor.b, alpha.getValue());
            shapeRenderer.rect(0, 0, 480, 800);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

        }
    }
}