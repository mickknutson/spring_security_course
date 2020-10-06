package io.baselogic.springsecurity.audit;

import org.springframework.util.Assert;

public class GlobalAuditContextHolderStrategy implements AuditContextHolderStrategy{

//    private static final ThreadLocal<AuditContext> contextHolder = new ThreadLocal<>();
//    private static final ThreadLocal<AuditContext> contextHolder = new InheritableThreadLocal<>();
    private static AuditContext contextHolder;


    @Override
    public void clearContext() {
        contextHolder = null;
    }

    /**
     * Obtains the current context.
     *
     * @return a context (never <code>null</code> - create a default implementation if
     * necessary)
     */
    @Override
    public AuditContext getContext() {
        if (contextHolder == null) {
            contextHolder = new AuditContext();
        }

        return contextHolder;
    }

    /**
     * Sets the current context.
     *
     * @param context to the new argument (should never be <code>null</code>, although
     * implementations must check if <code>null</code> has been passed and throw an
     * <code>IllegalArgumentException</code> in such cases)
     */
    @Override
    public void setContext(AuditContext context) {
        Assert.notNull(context, "Only non-null AuditContext instances are permitted");
        this.contextHolder = context;
    }

    /**
     * Creates a new, empty context implementation, for use by
     * <tt>AuditContextRepository</tt> implementations, when creating a new context for
     * the first time.
     *
     * @return the empty context.
     */
    @Override
    public AuditContext createEmptyContext() {
        return new AuditContext();
    }

} // The End...
