-- ML Lab - Data initialization
-- Este script se ejecuta automaticamente al iniciar la app

-- Agregar columna role si no existe (para migracion de BD existente)
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20);

-- Asignar role USER a usuarios existentes que no tengan rol
UPDATE users SET role = 'USER' WHERE role IS NULL;
