package co.yiiu.web.tag;

import java.io.IOException;
import java.util.Map;
import co.yiiu.module.user.service.UserService;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Component
public class UserDirective implements TemplateDirectiveModel {

    @Autowired
    private UserService userService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        String username;
        if (map.containsKey("username") && !StringUtils.isEmpty(map.get("username").toString())) {
            username = map.get("username").toString();
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            username = ((User) authentication.getPrincipal()).getUsername();
        }

        co.yiiu.module.user.model.User user = userService.findByUsername(username);

        environment.setVariable("user", builder.build().wrap(user));
        templateDirectiveBody.render(environment.getOut());
    }
}