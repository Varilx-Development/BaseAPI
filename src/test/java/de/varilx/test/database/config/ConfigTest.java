package de.varilx.test.database.config;

import de.varilx.configuration.VaxConfiguration;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class ConfigTest {

    @Test
    public void testRawString() {
        VaxConfiguration configuration = new VaxConfiguration("""
                test:
                 test: 
                  1: abc
                """);
        assertThat(configuration.getString("test.test.1")).isEqualTo("abc");
    }

    @Test
    public void testFile() throws IOException {
        File config = new File(".", "test.yml");
        config.createNewFile();

        new FileOutputStream(config).write("""
                test:
                 test: 
                  1: abc
                """.getBytes(StandardCharsets.UTF_8));

        VaxConfiguration configuration = new VaxConfiguration(new File("."), "test.yml");

        assertThat(configuration.getString("test.test.1")).isEqualTo("abc");
    }

    @Test
    public void testDefaults() {
        VaxConfiguration configuration = new VaxConfiguration(new File("."), "test.yml");
        assertThat(configuration.getString("abc.test.1")).isEqualTo("true");
        assertThat(new File(".", "test.yml").exists()).isTrue();

    }

    @AfterEach
    @BeforeEach
    public void delete() throws IOException {
        File config = new File(".", "test.yml");
        FileUtils.forceDelete(config);
    }

}
