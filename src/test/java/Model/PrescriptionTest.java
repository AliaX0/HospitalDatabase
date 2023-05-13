package Model;

import Database.DatabaseContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrescriptionTest {
    private DatabaseContext context;

    @BeforeAll
    public void setUp() {

    }
}
