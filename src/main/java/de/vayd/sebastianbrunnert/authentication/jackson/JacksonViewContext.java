package de.vayd.sebastianbrunnert.authentication.jackson;

public class JacksonViewContext {

    private static final ThreadLocal<Class<?>> currentView = new ThreadLocal<>();

    static void setCurrentView(Class<?> view) {
        currentView.set(view);
    }

    static Class<?> getCurrentView() {
        return currentView.get();
    }

    static void clear() {
        currentView.remove();
    }

    public static boolean isActive(Class<?>... views) {
        Class<?> current = getCurrentView();
        if (current == null) {
            return false;
        }
        for (Class<?> view : views) {
            if (view.equals(current)) {
                return true;
            }
        }
        return false;
    }

}
