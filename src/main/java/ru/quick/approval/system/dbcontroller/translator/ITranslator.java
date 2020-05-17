package ru.quick.approval.system.dbcontroller.translator;

/**
 * Интерфейс описывает классы для перевода из одного типа данных в другой и наоборот
 * @author Kirill Mikheev
 * @version 1.0
 */

/**
 * @param <S> - начальные данные
 * @param <R> - результат
 */
public interface ITranslator<S , R> {

    /**
     * Прямой перевод
     */
    R translate(S source);

    /**
     * Обратный перевод
     */
    S reverseTranslate(R source);

}
