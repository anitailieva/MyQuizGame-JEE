import com.iliani14.pg6100.GameApplication;
import com.iliani14.pg6100.GameConfiguration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;

/**
 * Created by anitailieva on 24/11/2016.
 */
public class GameApplicationTest extends GameApplicationTestBase{

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class);
}
