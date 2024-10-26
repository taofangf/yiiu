package co.yiiu.web.tag;

import java.io.IOException;
import java.util.Map;
import co.yiiu.config.SiteConfig;
import co.yiiu.module.node.model.Node;
import co.yiiu.module.node.service.NodeService;
import co.yiiu.module.topic.model.Topic;
import co.yiiu.module.topic.service.TopicService;
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
public class NodeTopicsDirective implements TemplateDirectiveModel {

    @Autowired
    private TopicService topicService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private SiteConfig siteConfig;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        String value = map.get("value").toString();

        int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());
        Node node = nodeService.findByValue(value);
        Page<Topic> page = topicService.findByNode(node, p, siteConfig.getPageSize());

        environment.setVariable("page", builder.build().wrap(page));
        templateDirectiveBody.render(environment.getOut());
    }
}
