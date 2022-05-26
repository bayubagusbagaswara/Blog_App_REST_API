# Category

# Create or Add Category

- hanya bisa dilakukan oleh User yang memiliki ROLE_USER
- Method : POST
- URL : `/api/categories`
- Request Body :

```json
{
  "name": "string"
}
```

- Response Body :

```json
{
  "id": "number",
  "name": "string",
  "post": []
}
```

# Get All Categories

- Method : GET
- URL : `/api/categories`
- RequestParam : page & size
- Response Body :

```json
{
  "content": [
    {
      "id": "number",
      "name": "string",
      "post": []
    },
    {
      "id": "number",
      "name": "string",
      "post": []
    }
  ],
  "page": "number",
  "size": "number",
  "totalElement": "number",
  "totalPages": "number",
  "last": "boolean"
}
```

# Get Category By ID

- Method : GET
- URL : `/api/categories/{id}`
- Response Body :

```json
{
  "id": "number",
  "name": "string",
  "post": []
}
```

# Update Category 

- hanya bisa diakses User yang memiliki ROLE_USER atau ROLE_ADMIN
- Method : PUT
- URL : `/api/categories/{id}`
- Request Body :
```json
{
  
}
```
- Response Body