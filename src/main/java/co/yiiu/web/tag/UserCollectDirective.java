package co.yiiu.web.tag;

import java.io.IOException;
import java.util.Map;
import co.yiiu.config.SiteConfig;
import co.yiiu.module.collect.model.Collect;
import co.yiiu.module.collect.service.CollectService;
import co.yiiu.module.user.model.User;
import co.yiiu.module.user.service.UserService;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Component
public class UserCollectDirective implements TemplateDirectiveModel {

    @Autowired
    private UserService userService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private SiteConfig siteConfig;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        String username = map.get("username").toString();
        int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());

        User currentUser = userService.findByUsername(username);
        Page<Collect> page = collectService.findByUser(p, siteConfig.getPageSize(), currentUser);

        environment.setVariable("page", builder.build().wrap(page));
        environment.setVariable("currentUser", builder.build().wrap(currentUser));
        templateDirectiveBody.render(environment.getOut());
    }
}
