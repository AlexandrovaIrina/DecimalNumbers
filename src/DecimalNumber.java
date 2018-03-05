import static java.lang.Math.*;

public final class DecimalNumber {
    private final long intPart;
    private final long fracPart;

    /**
     * Вычисление длины числа
     *
     * @param number
     * @return int
     */
    private int length(long number) {
        if (number == 0) return 0;
        if (number == 1) return 1;
        return (int) ceil(log10(number + 0.5));
    }

    /**
     * Из известной целой дробной части числа строит строку,
     * состоящую из десятичного дробного числа
     *
     * @param intP
     * @param fracP
     * @return String
     */
    private String numStr(long intP, long fracP) {
        StringBuilder ans = new StringBuilder();
        ans.append(intP);
        if (fracP != 0) {
            ans.append(',');
            ans.append(abs(fracP));
        }
        return ans.toString();
    }

    /**
     * Constructor
     *
     * @param number
     */
    public DecimalNumber(long number) {
        intPart = number;
        fracPart = 0;
    }

    /**
     * Constructor
     *
     * @param number
     */
    public DecimalNumber(String number) {
        String[] parts = number.split(",");
        long intAns = 0;
        long fracAns = 0;
        int sign = 1;
        if (parts[0].charAt(0) == '-') {
            sign = -1;
        } else {
            intAns *= 10;
            intAns += sign * (parts[0].charAt(0) - 48);
        }
        for (int i = 1; i < parts[0].length(); i++) {
            intAns *= 10;
            intAns += sign * (parts[0].charAt(i) - 48);
        }
        if (parts.length > 1) {
            for (int i = 0; i < parts[1].length(); i++) {
                fracAns *= 10;
                fracAns += (parts[1].charAt(i) - 48);
            }
        }
        intPart = intAns;
        fracPart = fracAns;
    }

    /**
     * Constructor
     *
     * @param number
     */
    public DecimalNumber(int number) {
        intPart = number;
        fracPart = 0;
    }

    /**
     * Constructor
     *
     * @param number
     */
    public DecimalNumber(float number) {
        intPart = (long) floor(number);
        double frac = number - floor(number);
        while (frac != floor(frac)) {
            frac *= 10;
        }
        fracPart = (long) frac;
    }

    /**
     * Constructor
     *
     * @param number
     */
    public DecimalNumber(double number) {
        intPart = (long) floor(number);
        double frac = number - floor(number);
        while (frac != floor(frac)) {
            frac *= 10;
        }
        fracPart = (long) frac;
    }


    /**
     * Сложение двух десятичных дроных чисел
     *
     * @param number
     * @return DecimalNumber
     */
    public DecimalNumber plus(DecimalNumber number) {
        long intAns = 0;
        long thisFrac = fracPart;
        long numFrac = number.fracPart;
        int numLength = length(numFrac);
        int thisLength = length(thisFrac);
        int fracLength = max(thisLength, numLength);
        while (thisLength < fracLength) {
            thisFrac *= 10;
            thisLength++;
        }
        while (numLength < fracLength) {
            numFrac *= 10;
            numLength++;
        }
        long fracAns = thisFrac + numFrac;
        numLength = length(fracAns);
        if (numLength != fracLength) {
            intAns += 1;
            fracAns %= pow(10, fracLength);
        }
        while (fracAns % 10 == 0 && fracAns != 0) {
            fracAns /= 10;
        }
        intAns += intPart + number.intPart;
        return new DecimalNumber(numStr(intAns, fracAns));
    }

    /**
     * Меняет знак десятичного дробного числа
     *
     * @return DecimalNumber
     */
    private DecimalNumber unaryMinus() {
        return new DecimalNumber(numStr(-intPart, fracPart));
    }

    /**
     * Вычитает из одного десятичного дробного числа другое
     *
     * @param number
     * @return DecimalNumber
     */
    public DecimalNumber minus(DecimalNumber number) {
        return this.plus(number.unaryMinus());
    }

    /**
     * Умножает одно десятичное число на другое
     *
     * @param number
     * @return DecimalNumber
     */
    public DecimalNumber times(DecimalNumber number) {
        int thisLength = length(fracPart);
        int numLength = length(number.fracPart);
        int fracLength = thisLength + numLength;
        long thisFrac = fracPart;
        long thisNumber = intPart;
        long thisPower = (long) pow(10, thisLength - 1);
        long numPower = (long) pow(10, numLength - 1);
        while (thisPower > 0) {
            thisNumber *= 10;
            thisNumber += thisFrac / thisPower;
            thisFrac %= thisPower;
            thisPower /= 10;
        }
        long otherFrac = number.fracPart;
        long otherNumber = number.intPart;
        while (numPower > 0) {
            otherNumber *= 10;
            otherNumber += otherFrac / numPower;
            otherFrac %= numPower;
            numPower /= 10;
        }
        long ans = otherNumber * thisNumber;
        return new DecimalNumber(numStr(ans / (long) pow(10, fracLength),
                ans % (long) pow(10, fracLength)));
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
            return number.intPart == intPart && number.fracPart == fracPart;
        }
        return false;
    }


    @Override
    public String toString() {
        return numStr(intPart, fracPart);
    }
}

