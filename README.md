# link_backend
spring backend for link
this backend needs:
- a minio Server to store the images
- a mongoDB as the database for users and products
## start minio und mongo db server in MacOS:
- `MINIO_ROOT_USER=minioadmin MINIO_ROOT_PASSWORD=minioadmin minio server ~/minio_data --console-address ":9001"`
- `brew services start mongodb-community@5.0`
  ${minio.access.name}