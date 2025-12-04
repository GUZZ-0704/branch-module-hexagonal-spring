#!/bin/bash
# =============================================
# Script para configurar Docker para el proyecto
# =============================================

echo "=== Configurando entorno Docker para Sucursales ==="

# 1. Crear la red si no existe
echo ""
echo "[1/3] Verificando red Docker..."
if ! docker network inspect sucursales-net >/dev/null 2>&1; then
    echo "Creando red 'sucursales-net'..."
    docker network create sucursales-net --driver bridge
else
    echo "Red 'sucursales-net' ya existe."
fi

# 2. Construir imagen del servicio de inventario
echo ""
echo "[2/3] Construyendo imagen de inventario..."
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../inventory" || exit 1
docker build -t inventory-service:latest .

# 3. Mostrar estado
echo ""
echo "[3/3] Estado actual:"
echo ""
echo "Redes Docker:"
docker network ls | grep sucursales-net
echo ""
echo "Imágenes disponibles:"
docker images | grep inventory-service
echo ""
echo "Contenedores de inventario:"
docker ps -a | grep inv-
echo ""

echo "=== Configuración completada ==="
