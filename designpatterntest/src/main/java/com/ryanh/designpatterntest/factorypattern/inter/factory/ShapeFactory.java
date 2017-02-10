package com.ryanh.designpatterntest.factorypattern.inter.factory;

import com.ryanh.designpatterntest.factorypattern.inter.Circle;
import com.ryanh.designpatterntest.factorypattern.inter.Rectangle;
import com.ryanh.designpatterntest.factorypattern.inter.Square;
import com.ryanh.designpatterntest.factorypattern.inter.base.Shape;

/**
 * Author:胡仲俊
 * Date: 2017 - 02 - 10
 * Des:
 * FIXME
 * TODO
 */

public class ShapeFactory {

    //使用 getShape 方法获取形状类型的对象
    public Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle();
        } else if (shapeType.equalsIgnoreCase("RECTANGLE")) {
            return new Rectangle();
        } else if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square();
        }
        return null;
    }
}
