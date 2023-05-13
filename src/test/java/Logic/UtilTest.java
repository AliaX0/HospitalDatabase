package Logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Date;
import java.time.LocalDate;

public class UtilTest {

    @BeforeAll
    public static void setUp(){

    }

    @Test
    public void testParseDateString1()
    {
        Util.dateFromSQLiteString("Tue Mar 23 00:00:00 GMT 2021");
    }

    @Test
    public void testParseDateString2()
    {
        Util.dateFromSQLiteString("Tue Mar 23 00:00:00 EET 2021");
    }

    @Test
    public void testDateMatchesExpected1()
    {
        // Year Month Day    Time
        // 2021 - 3 - 28 - 00:00:00
        LocalDate knownDate = LocalDate.of(2021, 3, 28);
        Date sqlDate = Util.dateFromSQLiteString("Sun Mar 28 00:00:00 EET 2021");
        LocalDate comparisonDate = sqlDate.toLocalDate();

        assertEquals(true, knownDate.isEqual(comparisonDate));
    }
}
