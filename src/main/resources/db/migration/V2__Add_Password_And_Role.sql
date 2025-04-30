ALTER TABLE skillnettest."user" ADD COLUMN IF NOT EXISTS password varchar(255);
ALTER TABLE skillnettest."user" ADD COLUMN IF NOT EXISTS role varchar(50);
ALTER TABLE skillnettest."user" ADD COLUMN IF NOT EXISTS enabled boolean DEFAULT true;