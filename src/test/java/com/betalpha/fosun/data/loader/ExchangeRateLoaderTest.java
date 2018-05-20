package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.ExchangeRate;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * created by huzongpeng on 2018/5/10
 */
@Slf4j
public class ExchangeRateLoaderTest {

    @Test
    public void testLoad() {
        ExchangeRateLoader.load();
    }
}