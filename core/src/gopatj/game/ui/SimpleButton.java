package gopatj.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import gopatj.game.raindropHelpers.AssetLoader;

public class SimpleButton {

    private float x, y, width, height;

    private TextureRegion buttonUp; //тектура отпущенной кнопки
    private TextureRegion buttonDown; //тектура нажатой кнопки

    private Rectangle bounds; //технический прямоугольник кнопки

    private boolean isPressed = false; //не нажата
    private boolean playClickSound = true; //воспроизвести звук нажатия

    //конструктор кнопки
    public SimpleButton(float x, float y, float width, float height,
                        TextureRegion buttonUp, TextureRegion buttonDown,
                        boolean playClickSound) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonUp = buttonUp; //кнопка отпущена
        this.buttonDown = buttonDown; //кнопка нажата
        this.playClickSound = playClickSound;

        //технический прямоугольник кнопки, (800 - y - height) для эффекта У-вверх координаты.
        //хотя почему-то используется у bounds У-вниз Чтоб техническая кнопка не была под изображением кнопки
        bounds = new Rectangle(x, 800 - y - height, width, height);

    }

    //нажатие кнокпи. Проверяет, содержит ли этот прямоугольник точку в указанном месте
    public boolean isClicked(int screenX, int screenY) {
        return bounds.contains(screenX, screenY);
    }

    //рисуем кнопку
    public void draw(SpriteBatch batcher) {
        if (isPressed) { //если нажата, y - 5 для эффекта нажатия
            batcher.draw(buttonDown, x, y - 2, width, height);
        } else { //отпущена
            batcher.draw(buttonUp, x, y, width, height);
        }
    }

    //пока палец на кнопке она нажата
    public boolean isTouchDown(int screenX, int screenY) {

        if (bounds.contains(screenX, screenY)) {
            isPressed = true;
            if (this.playClickSound)
                AssetLoader.clickSound.play();
            return true;
        }

        return false;
    }

    //кнопка отпущена
    public boolean isTouchUp(int screenX, int screenY) {

        // Мы будем учитывать только touchUp в нажатом состоянии
        if (bounds.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            return true;
        }

        // Когда палец с кнопки уберут, мы очистим флаг, что кнопка нажата
        isPressed = false;
        return false;
    }

}