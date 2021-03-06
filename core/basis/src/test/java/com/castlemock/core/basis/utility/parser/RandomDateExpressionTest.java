package com.castlemock.core.basis.utility.parser;

import com.castlemock.core.basis.utility.parser.expression.ExpressionInput;
import com.castlemock.core.basis.utility.parser.expression.RandomDateExpression;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RandomDateExpressionTest {

    @Test
    public void testTransform(){
        final RandomDateExpression expression = new RandomDateExpression();
        final ExpressionInput expressionInput = new ExpressionInput(RandomDateExpression.IDENTIFIER);
        final String result = expression.transform(expressionInput);
        assertNotNull(result);
    }

}
