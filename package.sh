#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

mvn clean package

# Gera imagem de aplicativo Linux e pacote .deb quando disponível
jpackage \
  --input target \
  --name lanhousesystem \
  --main-jar lanhousesystem-1.0.0.jar \
  --main-class com.lanhouse.Main \
  --type app-image \
  --app-version 1.0.0 \
  --dest dist/linux

if command -v dpkg-deb >/dev/null 2>&1; then
  jpackage \
    --input target \
    --name lanhousesystem \
    --main-jar lanhousesystem-1.0.0.jar \
    --main-class com.lanhouse.Main \
    --type deb \
    --app-version 1.0.0 \
    --dest dist/linux
fi
