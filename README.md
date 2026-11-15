# Ophile

Minecraft 1.21.4 Fabric server-side mod project.

## Build
Run:

```bash
./gradlew build
```

The output JAR will be in `build/libs/`.

## Notes
- This is a server-side mod scaffold.
- Administrative commands must be registered on the server and protected by server-side operator/permission checks.
- `src/main/java/dev/ophile/commands/OpCommand.java` is intentionally empty for you to implement.
