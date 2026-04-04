#!/bin/bash

# 停止开发环境

echo "🛑 Stopping development environment..."
docker-compose down

echo "✅ All services stopped"
echo ""
echo "To remove volumes (clean slate):"
echo "  docker-compose down -v"
