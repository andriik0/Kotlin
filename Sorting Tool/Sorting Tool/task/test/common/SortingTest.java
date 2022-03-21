package common;

import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testing.Settings;

public abstract class SortingTest<T> extends StageTest<T> {
    static {
        Settings.allowOutOfInput = true;
    }
}
