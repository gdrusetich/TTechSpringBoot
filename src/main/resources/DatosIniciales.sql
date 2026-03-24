# 1. Esto borra y crea las tablas vacías
spring.jpa.hibernate.ddl-auto=create

# 2. Desactivamos la carga del archivo SQL
spring.sql.init.mode=never

# 3. Mantenemos esto para que Hibernate mande en la estructura
spring.jpa.defer-datasource-initialization=false