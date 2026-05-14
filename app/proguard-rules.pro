# Proguard rules — keep rules for the Namma Platform app.
# No external libraries; minimal config needed.

# Keep all public API classes
-keep public class * {
    public protected *;
}
