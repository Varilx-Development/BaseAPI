package de.varilx.test.config;

import de.varilx.config.Configuration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigTest {

    @Test
    public void testList() {
        String yml = """
                list:
                  - "1"
                  - "2"
                  - "3"
                """;
        Configuration config = new Configuration(yml);

        assertThat(config).isNotNull();
        assertThat(config.getString("list")).isEqualTo("[1, 2, 3]");
        assertThat(config.getStringList("list")).isEqualTo(List.of("1", "2", "3"));
    }

    @Test
    public void testNested() {
        String yml = """
                nested: # Comment
                  2:
                    true: test
                """;
        Configuration config = new Configuration(yml);

        assertThat(config).isNotNull();
        assertThat(config.getString("nested.2.true")).isEqualTo("test");
    }

}
