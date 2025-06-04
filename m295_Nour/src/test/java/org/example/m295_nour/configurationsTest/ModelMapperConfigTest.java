package org.example.m295_nour.configurationsTest;

import org.example.m295_nour.configurations.ModelMapperConfig;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperConfigTest {

    @Test
    void testModelMapperBean() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper mapper = config.modelMapper();

        assertThat(mapper).isNotNull();
    }
}
