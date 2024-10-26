package co.yiiu.web.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import co.yiiu.module.node.service.NodeService;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Component
public class NodesDirective implements TemplateDirectiveModel {

    @Autowired
    private NodeService nodeService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        List<Map<String, Object>> nodes = nodeService.findAll(false);

        environment.setVariable("nodes", builder.build().wrap(nodes));
        templateDirectiveBody.render(environment.getOut());
    }
}