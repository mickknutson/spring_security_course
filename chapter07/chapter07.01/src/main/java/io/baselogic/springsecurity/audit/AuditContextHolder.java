package io.baselogic.springsecurity.audit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditContextHolder {

    private static AuditContextHolderStrategy strategy;

    private static int initializeCount = 0;

    static {
        initialize();
    }

    /**
     * Explicitly clears the context value from the current thread.
     */
    public static void clearContext() {
        strategy.clearContext();
    }

    /**
     * Obtain the current <code>SecurityContext</code>.
     *
     * @return the security context (never <code>null</code>)
     */
    public static AuditContext getContext() {
        return strategy.getContext();
    }

    /**
     * Primarily for troubleshooting purposes, this method shows how many times the class
     * has re-initialized its <code>SecurityContextHolderStrategy</code>.
     *
     * @return the count (should be one unless you've called
     */
    public static int getInitializeCount() {
        return initializeCount;
    }

    private static void initialize() {
        strategy = new GlobalAuditContextHolderStrategy();

        initializeCount++;
    }

    /**
     * Associates a new <code>SecurityContext</code> with the current thread of execution.
     *
     * @param context the new <code>SecurityContext</code> (may not be <code>null</code>)
     */
    public static void setContext(AuditContext context) {
        strategy.setContext(context);
    }


    /**
     * Allows retrieval of the context strategy. See SEC-1188.
     *
     * @return the configured strategy for storing the security context.
     */
    public static AuditContextHolderStrategy getContextHolderStrategy() {
        return strategy;
    }

    /**
     * Delegates the creation of a new, empty context to the configured strategy.
     */
    public static AuditContext createEmptyContext() {
        return strategy.createEmptyContext();
    }

    @Override
    public String toString() {
        return "AuditContextHolder[initializeCount="+ initializeCount + "]";
    }

} // The End...
