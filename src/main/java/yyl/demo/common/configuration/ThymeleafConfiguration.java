package yyl.demo.common.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.attr.AbstractConditionalVisibilityAttrProcessor;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;

import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.plug.security.Securitys;

import yyl.demo.common.BaseConstants.Ids;

@Configuration
public class ThymeleafConfiguration {

    private static class NAMES {
        static final String DIALECT_PREFIX = "__";
        static final String HAS_PERMISSION = "hasPermission";
        static final String TEXT = "text";
        static final String USER_NAME = "user.name";
    }

    @Autowired
    private Securitys securitys;

    private ContextTools tools = new ContextTools();

    @Bean
    public CustomThymeleafDialect shiroDialect() {
        return new CustomThymeleafDialect();
    }

    private class CustomThymeleafDialect extends AbstractDialect {

        @Override
        public String getPrefix() {
            return NAMES.DIALECT_PREFIX;
        }

        public Map<String, Object> getExecutionAttributes() {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put(NAMES.DIALECT_PREFIX, tools);
            return attributes;
        }

        @Override
        public Set<IProcessor> getProcessors() {
            final Set<IProcessor> processors = new HashSet<>();
            processors.add(new SecurityElementProcessor());
            processors.add(new TextModifierAttrProcessor());
            return processors;
        }
    }

    // <div _:hasPermission="10000">
    public class SecurityElementProcessor extends AbstractConditionalVisibilityAttrProcessor {

        protected SecurityElementProcessor() {
            super(NAMES.HAS_PERMISSION);
        }

        @Override
        protected boolean isVisible(Arguments arguments, Element element, String attributeName) {
            return tools.hasPermission(element.getAttributeValue(attributeName));
        }

        @Override
        public int getPrecedence() {
            return 300;
        }

    }

    // <div my:text="user.name">
    private class TextModifierAttrProcessor extends AbstractTextChildModifierAttrProcessor {

        protected TextModifierAttrProcessor() {
            super(NAMES.TEXT);
        }

        @Override
        protected String getText(Arguments arguments, Element element, String attributeName) {
            return tools.text(element.getAttributeValue(attributeName));
        }

        @Override
        public int getPrecedence() {
            return 300;
        }
    }

    private class ContextTools {
        public String text(String name) {
            if (NAMES.USER_NAME.equals(name)) {
                return securitys.getPrincipal().getName();
            }
            return StringUtils.EMPTY;
        }

        public boolean hasPermission(String permission) {
            Principal principal = securitys.getPrincipal();
            String userId = principal.getUserId();
            if (Ids.ADMIN_ID.equals(userId)) {
                return true;
            }
            String[] permissionIds = principal.getPermissionIds();
            return ArrayUtils.contains(permissionIds, permission);
        }
    }
}
