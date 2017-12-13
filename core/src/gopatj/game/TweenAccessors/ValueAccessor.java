package gopatj.game.TweenAccessors;

import aurelienribon.tweenengine.TweenAccessor;

public class ValueAccessor implements TweenAccessor<Value> {

    /*
    ValueAccessor будет использоваться когда захотим интерполировать float переменную.
    Например, если хотим сделать вспышку на экране изменяя прозрачность у квадрата, мы создадим новый обхект типа Value и
    передадим в обработку нашему ValueAccessor. Фактично, мы используем эту логику чтобы плавно перейти от SpalshScreen к следующему экрану
     */

    @Override
    public int getValues(Value target, int tweenType, float[] returnValues) {
        returnValues[0] = target.getValue();
        return 1;
    }

    @Override
    public void setValues(Value target, int tweenType, float[] newValues) {
        target.setValue(newValues[0]);
    }

}
