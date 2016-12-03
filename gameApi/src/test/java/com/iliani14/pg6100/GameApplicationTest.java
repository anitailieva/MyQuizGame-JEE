package com.iliani14.pg6100;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;


public class GameApplicationTest extends GameApplicationTestBase{

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class);
}
