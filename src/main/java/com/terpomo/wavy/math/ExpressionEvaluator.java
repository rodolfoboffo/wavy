package com.terpomo.wavy.math;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionEvaluator {

    public static final String GREEK_PI = "π";
    public static final String PI = "pi";

    public static float evaluate(String expr) {
        expr = expr.replace(GREEK_PI, String.valueOf(MathConstants.PI));
        expr = expr.replace(PI, String.valueOf(MathConstants.PI));
        Expression expression = new ExpressionBuilder(expr).build();
        float result = (float) expression.evaluate();
        return result;
    }

}
