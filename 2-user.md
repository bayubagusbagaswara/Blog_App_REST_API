# Users

- ada endpoint yang bisa diakses tanpa autentikasi (tanpa login username dan password)
- ada juga endpoint yang bisa diakses ole User yang memiliki ROLE_USER
- dan juga endpoint yang hanya bisa diakses oleh User yang memiliki ROLE_ADMIN
- untuk method GET kita ijinkan semua untuk diakses, jadi tanpa login pun bisa

# Get Current User

- Method : GET
- URL : `/api/users/me`
- Response Body :

```json
{
  "id": "number",
  "username": "string",
  "firstName": "string",
  "lastName": "string"
}
```

# Check Username Availability

- Method : GET
- URL : `/api/users/checkUsernameAvailability`
- Response Body :

```json
{
  "available": "boolean"
}
```

# Check Email Availability

- Method : GET
- URL : `/api/users/checkEmailAvailability`
- Response Body :

```json
{
  "available": "boolean"
}
```

# Get User Profile

- Method : GET
- URL : `/api/users/{username}/profile`
- Response Body :

```json
{
  "id": "number",
  "username": "string",
  "firstName": "string",
  "lastName": "string",
  "joinedAt": "date",
  "email": "string",
  "address": [],
  "phone": "string",
  "postCount": "number"
}
```

# Add User

- Method : POST
- URL : `/api/users/`
- hanya bisa diakses User ROLE_ADMIN
- Request Body :

```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "password": "string",
  "email": "string",
  "address": {
    "street": "string",
    "suite": "string",
    "city": "string",
    "zipcode": "number"
  },
  "phone": "number"
}
```
- Response Body :

```json
{
  "id": "number",
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "password": "string",
  "email": "string",
  "address": {
    "street": "string",
    "suite": "string",
    "city": "string",
    "zipcode": "number"
  },
  "phone": "number",
  "roles": []
}
```

# Update User

- bisa diakses oleh user ROLE_USER dan ROLE_ADMIN
- Method : PUT
- URL : `/api/users/{username}`
- Request Body :

```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "password": "string",
  "email": "string",
  "address": {
    "street": "string",
    "suite": "string",
    "city": "string",
    "zipcode": "number"
  },
  "phone": "number"
}
```

- Response Body :

```json
{
  "id": "number",
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "password": "string",
  "email": "string",
  "address": {
    "street": "string",
    "suite": "string",
    "city": "string",
    "zipcode": "number"
  },
  "phone": "number",
  "roles": []
}
```

# Delete User

- bisa diakses oleh User ROLE_USER dan ROLE_ADMIN
- Method : DELETE
- URL : `/api/users/{username}`
- Response Body :

```json
{
  "success": "boolean",
  "message": "string",
  "status": "string"
}
```

# Give Admin

- bisa diakses oleh User ROLE_ADMIN
- Method : PUT
- URL : `/api/users/{username}/giveAdmin`
- Response Body :

```json
{
  "success": "boolean",
  "message": "string",
  "status": "string"
}
```

# Remove Admin

- hanya bisa diakses oleh User ROLE_ADMIN
- Method : PUT
- URL : `/api/users/{username}/removeAdmin`
- Response Body :

```json
{
  "success": "boolean",
  "message": "string",
  "status": "string"
}
```

# Set Address

- bisa diakses oleh User ROLE_USER atau ROLE_ADMIN
- Method : PUT
- URL : `/api/users/setOrUpdateInfo`
- Request Body

```json
{
  "street": "string",
  "suite": "string",
  "city": "string",
  "zipcode": "string",
  "phone": "string"
}
```

- Response Body

```json
{
  "id": "number",
  "username": "string",
  "firstName": "string",
  "lastName": "string",
  "joinedAt": "date",
  "email": "string",
  "address": [],
  "phone": "string",
  "postCount": "number"
}
```