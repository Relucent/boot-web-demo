package yyl.demo.common.thymeleaf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.attr.AbstractConditionalVisibilityAttrProcessor;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;

import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.plug.security.Securitys;

import yyl.demo.common.BaseConstants.Ids;

 

/**
 * 自定义_Thymeleaf_标签
 * @author YYL
 */
public class CustomThymeleafDialect extends AbstractDialect implements IDialect, IExpressionEnhancingDialect {

    private static class NAMES {
        static final String DIALECT_PREFIX = "__";
        static final String HAS_PERM = "hasPerm";
        static final String TEXT = "text";
        static final String USER_NAME = "user.name";
    }

    private final CustomTools tools;

    public CustomThymeleafDialect(@Autowired Securitys securitys) {
        this.tools = new CustomTools(securitys);
    }

    @Override
    public String getPrefix() {
        return NAMES.DIALECT_PREFIX;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new HasPermElementProcessor());
        processors.add(new TextModifierAttrProcessor());
        return processors;
    }


    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext context) {
        Map<String, Object> expressions = new HashMap<>();
        expressions.put(NAMES.DIALECT_PREFIX, tools);
        return expressions;
    }

    // <div __:hasPerm="1000">
    public class HasPermElementProcessor extends AbstractConditionalVisibilityAttrProcessor {

        protected HasPermElementProcessor() {
            super(NAMES.HAS_PERM);
        }

        @Override
        protected boolean isVisible(Arguments arguments, Element element, String attributeName) {
            return tools.hasPerm(element.getAttributeValue(attributeName));
        }

        @Override
        public int getPrecedence() {
            return 300;
        }
    }


    // <div __:text="user.name">
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
            return 200;
        }
    }

    public static class CustomTools {
        private Securitys securitys;

        private CustomTools(Securitys securitys) {
            this.securitys = securitys;
        }

        /** ${#__.text(name)} */
        public String text(String name) {
            if (NAMES.USER_NAME.equals(name)) {
                return securitys.getPrincipal().getName();
            }
            return "";
        }

        /** ${#__.hasPerm(10000)} */
        public boolean hasPerm(String permission) {
            Principal principal = securitys.getPrincipal();
            if (Ids.ADMIN_ID.equals(principal.getUserId())) {
                return true;
            }
            if (ArrayUtils.contains(principal.getPermissionIds(), permission)) {
                return true;
            }
            return false;
        }
    }
}
