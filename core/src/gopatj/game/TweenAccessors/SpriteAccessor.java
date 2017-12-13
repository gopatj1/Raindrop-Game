package gopatj.game.TweenAccessors;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int ALPHA = 1; //непрозначность

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA: //если в качесиве праментра ALPHA
                returnValues[0] = target.getColor().a; //возвращаем значение прозрачности
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA: //если в качесиве праментра ALPHA
                target.setColor(1, 1, 1, newValues[0]); //устанавливаем новое значение
                break;
        }
    }

}