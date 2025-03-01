package co.yiiu.web.tag;

import java.io.IOException;
import java.util.Map;
import co.yiiu.module.reply.model.Reply;
import co.yiiu.module.reply.service.ReplyService;
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
public class UserReplyDirective implements TemplateDirectiveModel {

    @Autowired
    private UserService userService;

    @Autowired
    private ReplyService replyService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        String username = map.get("username").toString();
        int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());
        int limit = Integer.parseInt(map.get("limit").toString());

        User currentUser = userService.findByUsername(username);
        Page<Reply> page = replyService.findByUser(p, limit, currentUser);

        environment.setVariable("page", builder.build().wrap(page));
        environment.setVariable("currentUser", builder.build().wrap(currentUser));
        templateDirectiveBody.render(environment.getOut());
    }
}
