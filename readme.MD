# Scaffold SkillNet Java Devops Clean AArchitecture

## Instalación y configuración de la base de datos
1. Instala Docker en tu sistema. (Recomiendo Docker Rancher)
2. Descarga la imagen de postgres
   ``` bash
   docker pull postgres
   ```
3. Ejecuta el contenedor de PostgreSQL:
   ```bash
   docker run -d --name skillnet-postgres -p 5432:5432 -e POSTGRES_PASSWORD=root postgres
   ```
4. Ingresa al contenedor:
   ```bash
   docker exec -it skillnet-postgres psql -U postgres
   ```
5. Crea el usuario en la base de datos
```sql
 CREATE USER skillnettest WITH PASSWORD 'root';
```
6. Dentro de psql, crea el esquema y el usuario:
   ```sql
   CREATE DATABASE skillnetworkshop;
   GRANT ALL PRIVILEGES ON DATABASE skillnetworkshop TO skillnettest;
   ```
7. Te recomiendo crear un nuevo schema, distinto al public
   ```sql
   CREATE SCHEMA skillnet_test;
   GRANT ALL PRIVILEGES ON SCHEMA skillnet_test TO skillnettest; 
   ```
8. Descargar la imagen docker de rabbit

    ```bash
    docker pull rabbitmq:3-management
    ```
   
9. Ejecutar el contenedor de rabbitmq
   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```
   
9. Ejecuta el proyecto y prueba con postman los endpoints



## DEPLOYMENT in AWS

1. Debes tener creado un ECR previamente, si no lo tienes creado, puedes seguir este tutorial: https://docs.aws.amazon.com/AmazonECR/latest/userguide/docker-push-ecr-image.html


2.  Buold docker
```bash
docker build -t hexagonal-webflux:latest .
```


3. si tienes sso debes ejecutar el siguiente comando para autenticarte en AWS CLI
```bash
aws ecr get-login-password --region <AWS_REGION> --profile <AWS_PROFILE> | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com
```
   - Reemplaza `<AWS_REGION>` con tu región de AWS, por ejemplo `us-east-1`.
   - Reemplaza `<AWS_PROFILE>` con el nombre de tu perfil de AWS CLI.
   - Reemplaza `<AWS_ACCOUNT_ID>` con tu ID de cuenta de AWS.


4. Etiquetar la imagen
```bash
docker tag hexagonal-webflux:latest <AWS_REGION>.dkr.ecr.<AWS_REGION>.amazonaws.com/hexagonal-webflux:latest
```
   - Asegúrate de reemplazar `AWS_ACCOUNT_ID` con tu ID de cuenta de AWS y `AWS_REGION` con la región donde está tu ECR.


5. subir la imagen al ecr
```bash
docker push <AWS_REGION>.dkr.ecr.<AWS_REGION>.amazonaws.com/hexagonal-webflux:latest
```
   - Asegúrate de reemplazar `AWS_ACCOUNT_ID` con tu ID de cuenta de AWS y `AWS_REGION` con la región donde está tu ECR.


6. verificar la imagen en el ecr
```bash
aws ecr describe-images --repository-name hexagonal-webflux --profile <AWS_PROFILE>
```
   - Reemplaza `<AWS_PROFILE>` con el nombre de tu perfil de AWS CLI.