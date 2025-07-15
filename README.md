# ☕ Cafe Management System - Backend

Sistem backend untuk Cafe Management System menggunakan Java Spring Boot.

## 🚀 Teknologi
- Java 17
- Spring Boot
- PostgreSQL
- Lombok
- Hibernate (JPA)
- Jakarta Validation
- Spring Security (basic)
- Postman (API testing)

## 📁 Struktur Direktori
```
src/  
├── config/  
├── controller/  
├── model/ (dto, entity, enum)  
├── repository/  
├── security/  
├── service/  
└── utils/  
```

## ⚙️ Konfigurasi `.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cafe  
spring.datasource.username="YOUR USERNAME"  
spring.datasource.password="YOUR PASSWORD"  
spring.jpa.hibernate.ddl-auto=update  
```

## ▶️ Cara Menjalankan
```bash
./mvnw spring-boot:run
```

## 📦 API Utama
| Method | Endpoint                          | Deskripsi                        |
|--------|-----------------------------------|----------------------------------|
| POST   | /api/v1/customers                 | Menambahkan customer baru        |
| POST   | /api/v1/menus                     | Menambahkan menu (form-data)     |
| GET    | /api/v1/menus                     | Mendapatkan seluruh menu         |
| POST   | /api/v1/orders                    | Membuat pesanan                  |
| GET    | /api/v1/orders/customer/{id}     | Mendapatkan order customer       |
| PUT    | /api/v1/orders/{id}/status       | Tandai order sebagai PAID        |
| POST   | /api/v1/images/upload            | Upload gambar menu               |

## ✅ Validasi DTO
```java
@NotBlank String customerId;
@NotEmpty List<OrderDetailRequest> orderDetails;
```

## 📫 Kontribusi
Silakan fork repositori ini, lalu pull request dengan perubahan Anda.
