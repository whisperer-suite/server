package net.whisperersuite.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class ApplicationTest {
    @Test
    public void main() throws Exception {
        PowerMockito.mockStatic(SpringApplication.class);
        PowerMockito.when(SpringApplication.run(Application.class)).thenReturn(null);

        Application.main(new String[] {});

        ArgumentCaptor<Class> argumentCaptor = ArgumentCaptor.forClass(Class.class);
        PowerMockito.verifyStatic();
        SpringApplication.run(argumentCaptor.capture());

        assertEquals(Application.class, argumentCaptor.getValue());
    }
}
