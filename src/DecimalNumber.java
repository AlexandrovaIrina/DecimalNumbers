import javafx.util.Pair;

import static java.lang.Math.*;

public final class DecimalNumber {
    private final long fracPart;
    private final long intPart;
    private final int sign;

    /**
     * Число, использующеечя для определения цифры по коду данного символа
     */
    private final int codeChar = 48;

    /**
     * Вычисление длины числа.
     * Особенности: если заданное число 0 - возращает 0.
     *
     * @param number число
     * @return длина заданного числа{@code number}
     */
    private int length(long number) {
        if (number == 0) return 0;
        if (number == 1) return 1;
        return (int) ceil(log10(number + 0.5));
    }

    public DecimalNumber(long number) {
        intPart = abs(number);
        fracPart = 0;
        sign = (int) (number / intPart);

    }

    /**
     * Добавляет цифры в конец числа из заданной строки
     *
     * @param str  - строка, из которой происходит добавление цифр в число
     * @param num  - число, в которое добавляются цифры в конце
     * @param from - индекс первой цифры для добавления в строке
     * @param to   - индекс до которого происходит добавление цифр(не включая)
     * @return получившееся из строки число
     */
    private long addNumsFromStr(String str, long num, int from, int to) {
        for (int i = from; i < to; i++) {
            num *= 10;
            num += str.charAt(i) - codeChar;
        }
        return num;
    }

    public DecimalNumber(String number) {
        String[] parts = number.split(",");
        long intAns = 0;
        long fracAns = 0;
        sign = parts[0].charAt(0) == '-' ? -1 : 1;
        if (sign == 1) {
            intAns = addNumsFromStr(parts[0], intAns, 0, 1);
        }
        intAns = addNumsFromStr(parts[0], intAns, 1, parts[0].length());
        if (parts.length > 1) {
            fracAns = addNumsFromStr(parts[1], fracAns, 0, parts[1].length());
        }
        intPart = intAns;
        fracPart = fracAns;
    }

    public DecimalNumber(int number) {
        intPart = abs(number);
        sign = (int) (number / intPart);
        fracPart = 0;
    }

    public DecimalNumber(float number) {
        final double floorPart = floor(number);
        sign = number > 0 ? 1 : -1;
        intPart = abs((long) floorPart);
        double frac = abs(number - floorPart);
        while (frac != floor(frac)) {
            frac *= 10;
        }
        fracPart = (long) frac;
    }

    public DecimalNumber(double number) {
        final double floorPart = floor(number);
        intPart = abs((long) floorPart);
        sign = number > 0 ? 1 : -1;
        double frac = abs(number - floorPart);
        while (frac != floor(frac)) {
            frac *= 10;
        }
        fracPart = (long) frac;
    }

    /**
     * Приписывает недостающие нули в конце дробной части числа
     *
     * @param frac   - дробная часть числа
     * @param length - изначальная длина дробной чатсти(@code frac)
     * @param to     - нужная длина дробной части
     */

    private long moveFracPart(long frac, int length, int to) {
        while (length < to) {
            frac *= 10;
            length++;
        }
        return frac;
    }

    /**
     * Для заданных дробных частей чисел добавляет в конце нули тому,
     * которое меньше по длине
     *
     * @param firstFrac  - дробная часть первого числа
     * @param secondFrac - дробная часть второго числа
     * @return измененные дробные части
     */
    private Pair<Long, Long> stabilizesFracParts(long firstFrac, long secondFrac) {
        int numLength = length(firstFrac);
        int thisLength = length(secondFrac);
        int fracLength = max(thisLength, numLength);
        secondFrac = moveFracPart(secondFrac, thisLength, fracLength);
        firstFrac = moveFracPart(firstFrac, numLength, fracLength);
        return new Pair(firstFrac, secondFrac);
    }

    private DecimalNumber(long intP, long fracP, int newSign) {
        intPart = intP;
        fracPart = fracP;
        sign = newSign;
    }

    /**
     * Сложение двух десятичных дробных чисел
     *
     * @param number - объект класса DecimalNumber
     * @return число - сумма чисел this и аргумента{@code number} -
     * объект класса DecimalNumber
     */
    public DecimalNumber plus(DecimalNumber number) {
        long intAns = 0;
        long thisFrac = fracPart;
        long numFrac = number.fracPart;
        intAns += intPart + number.intPart;
        long fracAns = 0;
        Pair<Long, Long> ans = stabilizesFracParts(numFrac, thisFrac);
        numFrac = ans.getKey();
        thisFrac = ans.getValue();
        int fracLength = length(max(numFrac, thisFrac));
        if (sign == number.sign) {
            fracAns = thisFrac + numFrac;
            if (length(fracAns) != fracLength) {
                intAns += 1;
                fracAns %= pow(10, fracLength);
            }
            while (fracAns % 10 == 0 && fracAns != 0) {
                fracAns /= 10;
            }
        } else if (sign == -1) {
            return new DecimalNumber(intPart, fracPart, -sign).minus(number);
        } else return this.minus(new DecimalNumber(number.intPart, number.fracPart, -number.sign));
        return new DecimalNumber(intAns, fracAns, sign);
    }

    /**
     * Сравнивает данное десятичное дробное число с заданным.
     * Особенности: возвращает 1, если данное число больше
     * аргумента(@code number)
     * 0, если данное число равно
     * аргументу(@code number)
     * -1, если данное число меньше
     * аргумента(@code number)
     *
     * @param number - число, с которым происходит сравнение данного числа
     * @return 1, 0 или -1, в зависимости от того больше, равно или меньше
     * аргумента данное число
     */
    private int compareTo(DecimalNumber number) {
        if (this.equals(number)) return 0;
        if (intPart > number.intPart) return 1;
        if (intPart == number.intPart &&
                fracPart > number.fracPart) return 1;
        return -1;
    }

    /**
     * Меняет знак десятичного дробного числа
     *
     * @return число противоположного знака данному -
     * объект класса DecimalNumber.
     */
    private DecimalNumber unaryMinus() {
        return new DecimalNumber(intPart, fracPart, -sign);
    }

    /**
     * Вычитает из одного десятичного дробного числа другое
     *
     * @param number - вычитаемое - объект класса DecimalNumber
     * @return число - разность чисел this и аргумента{@code number} -
     * объект класса DecimalNumber
     */
    public DecimalNumber minus(DecimalNumber number) {
        if (sign == number.sign) {
            switch (this.compareTo(number)) {
                case 0: {
                    return new DecimalNumber(0, 0, 1);
                }
                case -1: {
                    return number.minus(this).unaryMinus();
                }
            }
            long intAns = intPart - number.intPart;
            long thisFrac = fracPart;
            long numFrac = number.fracPart;
            Pair<Long, Long> ans = stabilizesFracParts(numFrac, thisFrac);
            numFrac = ans.getKey();
            thisFrac = ans.getValue();
            long fracLength = length(max(numFrac, thisFrac));
            long fracAns = thisFrac - numFrac;
            if (fracAns < 0) {
                fracAns += pow(10, fracLength + 1);
                intAns--;
            }
            while (fracAns % 10 == 0 && fracAns != 0) {
                fracAns /= 10;
            }
            return new DecimalNumber(intAns, fracAns, sign);
        } else {
            return this.plus(number.unaryMinus());
        }
    }

    /**
     * Переобразовывает число из дробного в целое, отбрасыванием запятой
     *
     * @return число, полученное из целой и дробной части, отбрасыванием запятой
     */
    private long stabilizeNumber() {
        long thisNumber = intPart;
        int thisLength = length(fracPart);
        long thisFrac = fracPart;
        long thisPower = (long) pow(10, thisLength - 1);
        while (thisPower > 0) {
            thisNumber *= 10;
            thisNumber += thisFrac / thisPower;
            thisFrac %= thisPower;
            thisPower /= 10;
        }
        return thisNumber;
    }

    /**
     * Умножает одно десятичное число на другое
     *
     * @param number - множитель - объект класса DecimalNumber
     * @return число - произведение чисел this и аргумента{@code  number}-
     * объект класса DecimalNumber
     */
    public DecimalNumber times(DecimalNumber number) {
        int fracLength = length(fracPart) + length(number.fracPart);
        long ans = this.stabilizeNumber() * number.stabilizeNumber();
        return new DecimalNumber(ans / (long) pow(10, fracLength),
                ans % (long) pow(10, fracLength), sign * number.sign);
    }

    /**
     * Округление части числа до заданной позиции
     *
     * @param part - часть числа(целая или дробная)
     * @param pos  - позиция, до которой производится округление
     * @return часть числа, для которой производится округление
     * до заданной позиции(@code pos)
     */
    private long roundPart(long part, int pos) {
        if (pos > 2) part /= pow(10, pos - 2);
        if (part % 10 >= 5 && part / 10 % 10 != 9) {
            part += 10;
        }
        part /= 10;
        return part;
    }

    /**
     * Округление числа по математическим правилам до заданной позиции.
     *
     * @param pos - позиция, до которой производится округление
     * @return число, полученнное округлением заданного до позиции(@code pos)
     */
    public DecimalNumber round(int pos) {
        int fracLength = length(fracPart);
        int intLength = length(intPart);
        long ansFrac = fracPart;
        long ansInt = intPart;
        if (pos <= fracLength) {
            ansFrac = roundPart(ansFrac, pos);
        } else {
            ansInt = roundPart(ansInt * 10 + fracPart / (long) pow(10, fracLength - 1),
                    pos - fracLength + 1);
            ansFrac = 0;
        }
        return new DecimalNumber(ansInt, ansFrac, sign);
    }

    @Override
    public int hashCode() {
        int result = (int) (intPart ^ (intPart >>> 32));
        result = 31 * result + (int) (fracPart ^ (fracPart >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof DecimalNumber) {
            DecimalNumber number = (DecimalNumber) obj;
            return number.intPart == intPart && number.fracPart == fracPart
                    && sign == number.sign;
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        if (sign == -1) {
            ans.append('-');
        }
        ans.append(intPart);
        if (fracPart != 0) {
            ans.append(',');
            ans.append(abs(fracPart));
        }
        return ans.toString();
    }
}

