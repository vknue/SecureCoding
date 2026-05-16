# GlowLog

A personal skincare product tracker and routine journal built as a Spring Boot 4 project with an MVC interface and a REST API.

## Technologies

| Component | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring MVC / Spring Security | 7.x |
| Thymeleaf | 3.x |
| Spring Data JPA / Hibernate | 7.x |
| H2 (in-memory) | runtime |
| Auth0 Java JWT | 4.4.0 |
| springdoc-openapi | 3.0.0 |

## Running the Project in IntelliJ IDEA

1. **Open the project:** `File → Open` → select the `glowlog` folder
2. **SDK:** `File → Project Structure → SDK` → set to **Java 25**
3. **Maven:** IntelliJ will automatically download dependencies; if not, run **Reload Maven Project**
4. **Run:** `GlowlogApplication.java` → right-click → *Run*
5. **Access:** [http://localhost:8080](http://localhost:8080)

## Default User Accounts (in-memory H2)

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN — full management |
| `user` | `user123` | USER — read and search only |

## MVC Interface

| URL | Description | Access |
|---|---|---|
| `/` | Redirect to `/products` | — |
| `/auth/login` | Login form | Public |
| `/auth/register` | Registration form | Public |
| `/products` | Browse and search skincare shelf | USER + ADMIN |
| `/products/{id}` | Product details with full breakdown | USER + ADMIN |
| `/products/new` | Add new product form | ADMIN |
| `/products/edit/{id}` | Edit product form | ADMIN |
| `/products/delete/{id}` | Delete product (POST) | ADMIN |

## REST API

Base URL: `/api`

### Authentication

| Method | URL | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Login — returns access + refresh token |
| `POST` | `/api/auth/register` | Register a new user account |
| `POST` | `/api/auth/refresh` | Obtain a new access token |
| `POST` | `/api/auth/logout` | Revoke the refresh token |

### Products (requires Bearer token)

| Method | URL | Description | Role |
|---|---|---|---|
| `GET` | `/api/products` | All products | USER + ADMIN |
| `GET` | `/api/products/{id}` | Single product | USER + ADMIN |
| `GET` | `/api/products/search` | Search (`query`, `category`, `skinType`, `status`) | USER + ADMIN |
| `POST` | `/api/products` | Add new product | ADMIN |
| `PUT` | `/api/products/{id}` | Update product | ADMIN |
| `DELETE` | `/api/products/{id}` | Delete product | ADMIN |

### Example: Login and API Usage

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Fetch products using the access token
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer <access_token>"
```

## Swagger UI

Available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

1. Call `POST /api/auth/login` with `admin` / `admin123`
2. Copy the `accessToken` from the response
3. Click **Authorize** (top right) → paste the token
4. Use any of the secured endpoints

## H2 Console

URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:glowlogdb` |
| Username | `sa` |
| Password | *(leave blank)* |

## Key Features

- **Holy Grail crown badge** — products marked as holy grail get a gold crown badge floating above the card, with a special gradient border that sets them apart from the rest of the shelf
- **Empty count tracker** — track how many bottles of the same product you've gone through in your lifetime (e.g. "Empty #3" pill on cards, repurchase hero card on detail page with contextual messaging like "This is the definition of a repurchase")
- **PAO (Period After Opening) tracking** — capture the 12M / 6M / 24M symbol that all skincare uses for shelf life after opening
- **Four-dimensional product scoring** — independently rate each product on Effectiveness, Texture/Feel, Scent, and Value (1-10) with animated gradient quality bars
- **Pink-to-purple routine pills** — AM_ONLY routines display in soft pink, PM_ONLY in dusty purple, AM_AND_PM in a beautiful gradient blend — visually communicating sun vs moon usage
- **Skincare timeline cards** — visual cards for 🛒 Purchased, 🔓 Opened, ⏱ Expires, ✓ Finished
- **5 product statuses** — ACTIVE, FINISHED, ABANDONED, WANT_TO_TRY, REPURCHASE — each with color-coded stripes
- **12 product categories** — Cleanser, Toner, Essence, Serum, Moisturizer, Sunscreen, Exfoliant, Mask, Eye Cream, Facial Oil, Lip Care, Spot Treatment
- **Key ingredients in monospace** — formulation transparency rendered in code-font for that lab-precision feel
- **Reaction notes** — separate field for tracking skin reactions, breakouts, tingling, allergic responses
- **Three boolean flags** — Cruelty Free 🐰, Fragrance Free 🚫, Would Repurchase ♻
- **"Get That Glow" custom CTA** — themed login button

## Project Structure

```
src/main/java/hr/algebra/glowlog/
├── GlowlogApplication.java
├── config/
│   ├── DataInitializer.java          # 10 sample products on startup
│   ├── OpenApiConfig.java            # Swagger / OpenAPI configuration
│   └── SecurityConfig.java           # Two filter chains (API + MVC)
├── controller/
│   ├── mvc/
│   │   ├── AuthMvcController.java
│   │   ├── HomeController.java
│   │   └── ProductMvcController.java
│   └── rest/
│       ├── AuthRestController.java
│       └── ProductRestController.java
├── dto/
│   ├── Dto.java                      # Login/Register/Token records
│   └── ProductDto.java               # Java record
├── entity/
│   ├── Product.java
│   ├── RefreshToken.java
│   └── User.java                     # Implements UserDetails
├── enums/
│   ├── ProductCategory.java
│   ├── ProductStatus.java
│   ├── Role.java
│   ├── RoutineSlot.java
│   ├── SkinConcern.java
│   └── SkinType.java
├── repository/
│   ├── ProductRepository.java
│   ├── RefreshTokenRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── ProductService.java
    └── RefreshTokenService.java
```
