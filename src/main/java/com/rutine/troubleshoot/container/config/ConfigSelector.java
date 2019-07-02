package com.rutine.troubleshoot.container.config;

import com.rutine.troubleshoot.container.bean.MyImportSelector;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author rutine
 * @date 2019/7/2 17:49
 */
@Configuration
@ConfigSelect(ConfigA.class)
@Import(MyImportSelector.class)
public class ConfigSelector {

}
