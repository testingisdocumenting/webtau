package com.twosigma.webtau.data.table.render;

/**
 * header
 * <pre>
 *  midLeft     ColumnName1   midMid columnName2      midRight
 *  lowLeft lowFill lowFill   lowMid lowFill lowFill  lowRight
 *</pre>
 *
 * body
 * <pre>
 *   midLeft   value1         midMid  value2         midRight
 *   lowLeft lowFill lowFill  lowMid lowFill lowFill lowRight
 * </pre>
 */
public interface TableRenderStyle {
    String headerMidLeft();
    String headerMidMid();
    String headerMidRight();

    String headerBotLeft();
    String headerBotMid();
    String headerBotRight();
    String headerBotFill();

    String bodyMidLeft();
    String bodyMidMid();
    String bodyMidRight();
    String bodyBotLeft();
    String bodyBotMid();
    String bodyBotRight();
    String bodyBotFill();
}
