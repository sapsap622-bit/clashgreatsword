# Clash Greatsword (NeoForge 1.21.1)

Ataque cargado estilo Clash! (jsburg) + reverse grip para `alloyed:steel_sword`.

## Uso
- Mantener clic derecho (sin mirar un bloque) para cargar; soltar para golpear con más alcance.
- Carga completa = golpe crítico. Te mueves más lento mientras cargas.

## Compilar
Copiar esta carpeta sobre el MDK oficial de NeoForge 1.21.1 (o usar `gradle wrapper` aquí) y ejecutar `./gradlew build`.
El jar queda en `build/libs/`. Ajusta `neo_version` en gradle.properties a la de tu modpack.

## Config
- `clashgreatsword-common.toml`: arma, ticks de carga, alcance extra, multiplicador crítico, cooldown.
- `clashgreatsword-client.toml`: reverse grip (rotación y pivote) en primera y tercera persona.
  Si la espada se ve mal, ajusta `pivotX/pivotY` y `rotZ` hasta que quede bien.
