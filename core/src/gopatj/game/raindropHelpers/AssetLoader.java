package gopatj.game.raindropHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

    //изображения
    public static Texture texture, logoTexture; //используем одну общую png-картинку с всеми изображениями в игре
    public static TextureRegion dropImage; //текстура капли
    public static TextureRegion smallDropImage; //текстура маленькой капли
    public static TextureRegion lifeImage; //текстура сердца, сетчика жизней
    public static TextureRegion goldenImage; //текстура золотой капли
    public static TextureRegion basinImage; //текстура тазика
    public static TextureRegion bucketImage; //текстура ведра
    public static TextureRegion collerImage; //текстура куллера
    public static TextureRegion glassImage; //текстура стакана
    public static TextureRegion waterBottleImage; //текстура пластиковой бутылки
    public static TextureRegion handsImage; //текстура ладоней
    public static TextureRegion stormImage; //текстура грозы
    public static TextureRegion windImage; //текстура ветра
    public static TextureRegion cloudImage; //текстура тумана
    public static TextureRegion bombImage; //текстура бомбы
    public static TextureRegion orangeImage; //текстура мандарина
    public static TextureRegion iceImage; //текстура сосульки
    public static TextureRegion alcoImage; //текстура шампанского
    public static TextureRegion gameBackgroundImage; //текстура заднего фона игры
    public static TextureRegion menuBackgroundImage; //текстура заднего фона меню
    public static TextureRegion scoreBoardImage; //текстура таблицы рекордов
    public static TextureRegion redCrossImage; //текстура красного крестика для таблицы рекордов
    public static TextureRegion pipeTopImage; //текстура водосточной трубы (верхнаяя часть)
    public static TextureRegion pipeBottomImage; //текстура водосточной трубы (нижняя часть)
    public static TextureRegion pipeBonusImage; //текстура падающего бонуса трубы
    public static TextureRegion goldenFeverBonusImage; //текстура падающего бонуса золотой лихорадки
    public static TextureRegion waterHoseBonusImage; //текстура падающего бонуса шланга
    public static TextureRegion waterHoseImage; //текстура водопроводного шланга
    public static TextureRegion defaultSpeedBonusImage; //текстура падающего бонуса стандартной скорости капель
    public static TextureRegion buttonUp, buttonDown, closeButtonUp, closeButtonDown, difficultButtonUnChecked, difficultButtonChecked; //кнопки
    public static TextureRegion touchIcon, keysIcon, accelrometrIcon; //иконки сложности и управления
    public static TextureRegion touchButtonLeft, touchButtonRight; //сенсорные кнопки влево/вправо
    public static TextureRegion logo; //логотип приветствия при запуске
    public static TextureRegion emptyTexture; //пустая текстура

    //анимация
    public static TextureRegion dropQuakeLeft; //текстура капли для дрожания на крыше
    public static TextureRegion dropQuakeRight; //текстура капли для дрожания на крыше
    public static Animation dropQuakeAnimation; //анимация дрожания капли на крыше


    //звуки
    public static Sound dropCollectSound; //звук пойманной капли
    public static Sound iceCollectSound; //звук пойманной сосульки
    public static Sound bombCollectSound; //звук пойманной петарды
    public static Sound bottleCollectSound; //звук пойманного шампанского
    public static Sound orangeCollectSound; //звук пойманного мандарина
    public static Sound bonusCollectSound; //звук пойманного бонуса
    public static Sound lostDropSound; //звук пропущенной капли
    public static Sound trashOnPipeSound; //звук мусора упавшего на трубу
    public static Sound clickSound; //звук нажатия на кнопку
    public static Music gameoverMusic; //звук окончания игры
    public static Music rainMusic; //звук дождя
    public static Music rainstormMusic; //звук грома
    public static Music windMusic; //звук вьюги
    public static Music pipeBonusMusic; //звук капель в трубе
    public static Music goldenFeverMusic; //звук золотой лихорадки
    public static Music waterHoseBonusMusic; //звук бонуса шланга
    public static Music mainMenuMusic; //звук главного меню

    //шрифты
    public static BitmapFont font;

    //переменная файла Preferences, запоминающая настройки и рекорд
    public static Preferences prefs;

    public static void load() { //загружаем ассеты

        //логотип приветствия при запуске
        logoTexture = new Texture(Gdx.files.internal("image/logo.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, 2226, 3958);

        texture = new Texture(Gdx.files.internal("image/texture.png")); //создание тектур
        //фильтры уменьшения и увеличения. Каждый пиксель сохранит свою форму, а не будет размытым
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        emptyTexture = new TextureRegion(texture, 500, 100, 1, 1); //пустая текстура

        //статические объекты
        gameBackgroundImage = new TextureRegion(texture, 0, 418, 480, 800); //текстура заднего фона игры
        menuBackgroundImage = new TextureRegion(texture, 0, 0, 480, 800); //текстура заднего фона меню
        stormImage = new TextureRegion(texture, 747, 637, 206, 93); //текстура грозы
        windImage = new TextureRegion(texture, 597, 635, 150, 90); //текстура ветра
        cloudImage = new TextureRegion(texture, 598, 452, 354, 184); //текстура тумана
        pipeTopImage = new TextureRegion(texture, 482, 0, 472, 25); //текстура водосточной трубы (верхнаяя часть)
        pipeBottomImage = new TextureRegion(texture, 528, 28, 424, 111); //текстура водосточной трубы (нижняя часть)
        scoreBoardImage = new TextureRegion(texture, 483, 140, 447, 311); //текстура таблицы рекордов
        redCrossImage = new TextureRegion(texture, 534, 1060, 70, 70); //текстура красного крестика для таблицы рекордов


        //динамические объекты
        dropImage = new TextureRegion(texture, 483, 690, 42, 64); //текстура капли
        smallDropImage = new TextureRegion(texture, 486, 766, 27, 45); //текстура капли
        goldenImage = new TextureRegion(texture, 535, 690, 45, 64); //текстура золотой капли
        lifeImage = new TextureRegion(texture, 532, 766, 52, 43); //текстура падающего сердца и иконки сердца, сетчика жизней
        waterHoseImage = new TextureRegion(texture, 589, 732, 101, 201); //текстура водопроводного шланга

        //сосуды
        basinImage = new TextureRegion(texture, 489, 467, 99, 50); //текстура тазика
        bucketImage = new TextureRegion(texture, 484, 524, 64, 64); //текстура ведра
        collerImage = new TextureRegion(texture, 485, 599, 47, 80); //текстура куллера
        glassImage = new TextureRegion(texture, 541, 626, 44, 50); //текстура стакана
        waterBottleImage = new TextureRegion(texture, 554, 520, 35, 100); //текстура пластиковой бутылки
        handsImage = new TextureRegion(texture, 556, 946, 88, 100); //текстура ладоней

        //падающий мусор
        iceImage = new TextureRegion(texture, 487, 1008, 44, 83); //текстура льда
        bombImage = new TextureRegion(texture, 484, 821, 44, 99); //текстура петарды
        alcoImage = new TextureRegion(texture, 549, 820, 30, 100); //текстура шампанского
        orangeImage = new TextureRegion(texture, 482, 929, 74, 68); //текстура мандарина

        //падающие бонусы
        pipeBonusImage = new TextureRegion(texture, 485, 1107, 49, 50); //текстура падающего бонуса трубы
        goldenFeverBonusImage = new TextureRegion(texture, 541, 1144, 69, 74); //текстура падающего бонуса золотой лихорадки
        defaultSpeedBonusImage = new TextureRegion(texture, 485, 1163, 50, 56); //текстура падающего бонуса стандартной скорости капель
        waterHoseBonusImage = new TextureRegion(texture, 615, 1147, 78, 71); //текстура падающего бонуса шланга

        //кнопки
        buttonUp = new TextureRegion(texture, 708, 733, 180, 60); //отпущенная длинная кнопка
        buttonDown = new TextureRegion(texture, 708, 803, 180, 60); //нажатая длинная кнопка
        closeButtonUp = new TextureRegion(texture, 894, 733, 60, 60); //отпущенная кнопка закрыть
        closeButtonDown = new TextureRegion(texture, 895, 799, 60, 60); //нажатая кнопка закрыть
        difficultButtonChecked = new TextureRegion(texture, 826, 865, 120, 120); //выбранная кнопка сложности
        difficultButtonUnChecked = new TextureRegion(texture, 696, 865, 120, 120); //невыбранная кнопка сложности
        touchIcon = new TextureRegion(texture, 700, 990, 73, 96); //иконка использования прикосновения
        keysIcon = new TextureRegion(texture, 783, 990, 45, 90); //иконка использования кнопок
        accelrometrIcon = new TextureRegion(texture, 844, 995, 110, 79); //иконка использования акселерометра
        touchButtonLeft = new TextureRegion(texture, 700, 1106, 119, 101); //сенсорная кнопка влево
        touchButtonRight = new TextureRegion(texture, 819, 1106, 123, 101); //сенсорная кнопка вправо

        //анимация
        dropQuakeLeft = new TextureRegion(texture, 645, 940, 50, 64); //текстура капли для анимации дрожания
        dropQuakeRight = new TextureRegion(texture, 645, 1005, 50, 64); //текстура капли для анимации дрожания
        TextureRegion[] dropQuake = { dropQuakeLeft, dropQuakeRight }; // создаем массив из объектов TextureRegion
        dropQuakeAnimation = new Animation(0.06f, dropQuake); // Создаем новый объект типа Animation в котором каждый фрейм длиться 0.04 секунд, используя созданный массив.
        dropQuakeAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG); // Выставляем режим проигрывания типа ping pong, анимация будет проигрываться вперед-назад.

        //короткие звуки
        dropCollectSound = Gdx.audio.newSound(Gdx.files.internal("sound/waterdrop.wav"));
        iceCollectSound = Gdx.audio.newSound(Gdx.files.internal("sound/ice.mp3"));
        bombCollectSound = Gdx.audio.newSound(Gdx.files.internal("sound/bomb.mp3"));
        bottleCollectSound = Gdx.audio.newSound(Gdx.files.internal("sound/bottle.mp3"));
        orangeCollectSound = Gdx.audio.newSound(Gdx.files.internal("sound/orange.mp3"));
        bonusCollectSound = Gdx.audio.newSound(Gdx.files.internal("sound/bonus.mp3"));
        lostDropSound = Gdx.audio.newSound(Gdx.files.internal("sound/error.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sound/click.mp3"));
        trashOnPipeSound = Gdx.audio.newSound(Gdx.files.internal("sound/trashOnPipe.mp3"));

        //длинные мелодии
        windMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/wind.mp3"));
        rainstormMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/rainstorm.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/undertreeinrain.mp3"));
        gameoverMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/fail.mp3"));
        pipeBonusMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/dropInPipe.mp3"));
        goldenFeverMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/goldenFever.mp3"));
        waterHoseBonusMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/subwayBonus.mp3"));
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/menuMusic.mp3"));

        //шрифты
        font = new BitmapFont(Gdx.files.internal("font/font.fnt"));

        //создаем (или получаем ранее созданный) файл preferences
        prefs = Gdx.app.getPreferences("Drop");
        //создадим переменую для хранения лучшего счета со значением по умолчанию 0, если раньше ее не существовало
        if (!prefs.contains("highscoreEasy")) { //рекорд на легком уровне сложности
            prefs.putInteger("highscoreEasy", 0).flush(); //получает новое значение и сохраняет
        }
        if (!prefs.contains("highscoreMedium")) { //рекорд на среднем уровне сложности
            prefs.putInteger("highscoreMedium", 0).flush(); //получает новое значение и сохраняет
        }
        if (!prefs.contains("highscoreHard")) { //рекорд на тяжелом уровне сложности
            prefs.putInteger("highscoreHard", 0).flush(); //получает новое значение и сохраняет
        }
        //создадим переменую для хранения выбранной сложности со значением по умолчанию 2, если раньше ее не существовало
        if (!prefs.contains("difficult")) {
            prefs.putInteger("difficult", 2).flush(); //получает новое значение и сохраняет (сложный уровень)
        }

    }

    //возвращает текущее значение highScore в зависимости от уровня сложности
    public static int getHighScore() {
        if (getDifficult() == 1)
            return prefs.getInteger("highscoreEasy");
        if (getDifficult() == 2)
            return prefs.getInteger("highscoreMedium");
        if (getDifficult() == 3)
            return prefs.getInteger("highscoreHard");
        return 1;
    }

    //возвращает текущее значение difficult
    public static int getDifficult() {
        return prefs.getInteger("difficult");
    }

    //получает на вход и сохраняет новое значение для highScore в зависимости от уровня сложности
    public static void setHighScore(int val) {
        if (getDifficult() == 1)
            prefs.putInteger("highscoreEasy", val).flush();
        if (getDifficult() == 2)
            prefs.putInteger("highscoreMedium", val).flush();
        if (getDifficult() == 3)
            prefs.putInteger("highscoreHard", val).flush();
    }

    //получает на вход новое значение для difficult и сохраняет
    public static void setDifficult(int val) { prefs.putInteger("difficult", val).flush(); }

    //избавляемся от ресурсов
    public static void dispose() {
        texture.dispose();
        logoTexture.dispose();
        dropCollectSound.dispose();
        iceCollectSound.dispose();
        bombCollectSound.dispose();
        bottleCollectSound.dispose();
        orangeCollectSound.dispose();
        bonusCollectSound.dispose();
        lostDropSound.dispose();
        clickSound.dispose();
        rainMusic.dispose();
        gameoverMusic.dispose();
        rainstormMusic.dispose();
        windMusic.dispose();
        pipeBonusMusic.dispose();
        trashOnPipeSound.dispose();
        goldenFeverMusic.dispose();
        waterHoseBonusMusic.dispose();
        mainMenuMusic.dispose();
        font.dispose();
    }
}