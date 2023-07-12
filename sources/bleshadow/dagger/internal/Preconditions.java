package bleshadow.dagger.internal;

/* loaded from: classes.dex */
public final class Preconditions {
    public static <T> T checkNotNull(T reference) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException(errorMessage);
    }

    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object errorMessageArg) {
        String valueOf;
        if (reference == null) {
            if (!errorMessageTemplate.contains("%s")) {
                throw new IllegalArgumentException("errorMessageTemplate has no format specifiers");
            }
            if (errorMessageTemplate.indexOf("%s") != errorMessageTemplate.lastIndexOf("%s")) {
                throw new IllegalArgumentException("errorMessageTemplate has more than one format specifier");
            }
            if (errorMessageArg instanceof Class) {
                valueOf = ((Class) errorMessageArg).getCanonicalName();
            } else {
                valueOf = String.valueOf(errorMessageArg);
            }
            throw new NullPointerException(errorMessageTemplate.replace("%s", valueOf));
        }
        return reference;
    }

    private Preconditions() {
    }
}
