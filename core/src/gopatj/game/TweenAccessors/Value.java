package gopatj.game.TweenAccessors;

public class Value {

    /*
    будет оберткой для float переменных. Будем использовать для этого класс, потому что только объекты могут быть использованы
    в Tween Engine (с примитивами ничего не получится). Так что, чтобы изменить float нужен для этого класс
     */

    private float val = 1;

    public float getValue() {
        return val;
    }

    public void setValue(float newVal) {
        val = newVal;
    }

}
