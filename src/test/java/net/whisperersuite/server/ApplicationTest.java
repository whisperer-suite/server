package net.whisperersuite.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class ApplicationTest {
    @Test
    public void main() throws Exception {
        PowerMockito.mockStatic(SpringApplication.class);
        PowerMockito.when(SpringApplication.run(Application.class)).thenReturn(null);

        Application.main(new String[] {});

        PowerMockito.verifyStatic();
        SpringApplication.run(eq(Application.class));
    }
}
