package git.hui.fix.test.ognl.core;

import ognl.*;
import ognl.enhance.UnsupportedCompilationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author yihui in 17:50 19/11/28.
 */
public class ASTBean extends SimpleNode {

    private static Class DEFAULT_MAP_CLASS;
    private String className;

    static {
        /* Try to get LinkedHashMap; if older JDK than 1.4 use HashMap */
        try {
            DEFAULT_MAP_CLASS = Class.forName("java.util.LinkedHashMap");
        } catch (ClassNotFoundException ex) {
            DEFAULT_MAP_CLASS = HashMap.class;
        }
    }

    public ASTBean(int id) {
        super(id);
    }

    public ASTBean(OgnlParser p, int id) {
        super(p, id);
    }

    protected void setClassName(String value) {
        className = value;
    }

    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Map answer;

        if (className == null) {
            try {
                answer = (Map) DEFAULT_MAP_CLASS.newInstance();
            } catch (Exception ex) {
                /* This should never happen */
                throw new OgnlException("Default Map class '" + DEFAULT_MAP_CLASS.getName() + "' instantiation error",
                        ex);
            }
        } else {
            try {
                answer = (Map) OgnlRuntime.classForName(context, className).newInstance();
            } catch (Exception ex) {
                throw new OgnlException("Map implementor '" + className + "' not found", ex);
            }
        }

        for (int i = 0; i < jjtGetNumChildren(); ++i) {
            ASTKeyValue kv = (ASTKeyValue) _children[i];
            Node k = kv.getKey(), v = kv.getValue();

            answer.put(k.getValue(context, source), (v == null) ? null : v.getValue(context, source));
        }

        return answer;
    }

    public String toString() {
        String result = "#";

        if (className != null) {
            result = result + "@" + className + "@";
        }

        result = result + "{ ";
        for (int i = 0; i < jjtGetNumChildren(); ++i) {
            ASTKeyValue kv = (ASTKeyValue) _children[i];

            if (i > 0) {
                result = result + ", ";
            }
            result = result + kv.getKey() + " : " + kv.getValue();
        }
        return result + " }";
    }

    public String toGetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Map expressions not supported as native java yet.");
    }

    public String toSetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Map expressions not supported as native java yet.");
    }

    private static class ASTKeyValue extends SimpleNode {
        public ASTKeyValue(int id) {
            super(id);
        }

        public ASTKeyValue(OgnlParser p, int id) {
            super(p, id);
        }

        protected Node getKey() {
            return _children[0];
        }

        protected Node getValue() {
            return (jjtGetNumChildren() > 1) ? _children[1] : null;
        }

        /**
         * Returns null because this is a parser construct and does not evaluate
         */
        protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
            return null;
        }

        public String toString() {
            return getKey() + " -> " + getValue();
        }
    }

}

