package io.baselogic.springsecurity.audit;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

public class ThreadLocalAuditContextHolderStrategy implements AuditContextHolderStrategy{

//    private static final ThreadLocal<AuditContext> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<AuditContext> contextHolder = new InheritableThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    /**
     * Obtains the current context.
     *
     * @return a context (never <code>null</code> - create a default implementation if
     * necessary)
     */
    @Override
    public AuditContext getContext() {
        AuditContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
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
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
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
