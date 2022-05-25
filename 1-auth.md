# Authentication

- endpoint pertama kali yang akan diakses oleh User
- User pertama yang Register/SignUp akan mendaptkan ROLE_ADMIN dan ROLE_USER
- User kedua yang Register/SignUp akan mendapatkan ROLE_USER
- Jadi hanya user pertama yang bisa memberi/menambahkan role baru ke user kedua

# Register / SignUp

- Method : POST
- URL : `/api/auth/signup`
- Request Body :

```json
{
  "firstName": "Albert",
  "lastName": "Einstein",
  "username": "albert",
  "password": "albert123",
  "email": "albert@gmail.com"
}
```
- Response Body :
```json
{
  "success": "boolean",
  "message": "User registered successfully"
}
```

# Login / SignIn

- Method : POST
- URL : `/api/auth/signin`
- Request Body :

```json
{
  "usernameOrEmail": "albert",
  "password": "albert123"
}
```
- Response Body :

```json
{
  "accessToken": "string"
}
```
