import org.junit.Test;

import static org.junit.Assert.*;

public class DecimalNumberTest {
    @Test
    public void plus() throws Exception {
        assertEquals((new DecimalNumber(14)),
                (new DecimalNumber(32)).plus(new DecimalNumber(-18)));
        assertEquals("24,92",
                (new DecimalNumber("23,12")).plus(new DecimalNumber("1,8")).toString());
        assertEquals((new DecimalNumber("-21,34")).toString(),
                (new DecimalNumber("-18,34").plus(new DecimalNumber("-3"))).toString());
        assertEquals(new DecimalNumber("2"),
                (new DecimalNumber("1,9").plus(new DecimalNumber("0,1"))));
    }

    @Test
    public void minus() throws Exception {
        assertEquals((new DecimalNumber("-31,22").toString()),
                (new DecimalNumber("0")).minus(new DecimalNumber("31,22")).toString());
        assertEquals((new DecimalNumber(-950)),
                (new DecimalNumber(-50).minus(new DecimalNumber(900))));
    }

    @Test
    public void times() throws Exception {
        assertEquals((new DecimalNumber("31,372")),
                (new DecimalNumber("1,1").times(new DecimalNumber("28,52"))));
        assertEquals(new DecimalNumber(3),
                (new DecimalNumber("1,5")).times(new DecimalNumber(2)));
        assertEquals(new DecimalNumber("-54"),
                (new DecimalNumber(-2)).times(new DecimalNumber("27")));
        assertEquals(new DecimalNumber("-52,2"),
                (new DecimalNumber(-2)).times(new DecimalNumber("26,1")));
    }

    @Test
    public void round() throws Exception {
        assertEquals(new DecimalNumber("3,2"), (new DecimalNumber("3,245")).round(3));
        assertEquals(new DecimalNumber("-1,9"), (new DecimalNumber("-1,999").round(3)));
        assertEquals(new DecimalNumber("10"), (new DecimalNumber("10,12").round(3)));
    }

}