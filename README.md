# HoroloFi RWA - Core Backend

**HoroloFi RWA** adalah backend untuk platform investasi dan pasar jam tangan mewah yang difraksionalisasi (Fractionalized luxury watch marketplace). Proyek ini dibangun menggunakan Java dan Spring Boot.

## 🛠 Teknologi yang Digunakan

Proyek ini menggunakan teknologi dan library berikut:

*   **Bahasa:** [Java 21](https://openjdk.org/projects/jdk/21/)
*   **Framework:** Spring Boot
*   **Build Tool:** Maven
*   **Database:** PostgreSQL
*   **Keamanan:** Spring Security
*   **ORM:** Spring Data JPA (Hibernate)
*   **Utilitas:** Lombok

## 📋 Prasyarat

Sebelum menjalankan aplikasi, pastikan Anda telah menginstal:

1.  **Java Development Kit (JDK) 21**
2.  **PostgreSQL Database**
3.  **Maven** (Opsional, karena proyek ini menyertakan Maven Wrapper)

## 🚀 Cara Menjalankan Aplikasi

### 1. Konfigurasi Database

Sebelum menjalankan aplikasi, Anda perlu mengonfigurasi koneksi database PostgreSQL. Buka file [`src/main/resources/application.properties`](src/main/resources/application.properties) dan tambahkan konfigurasi berikut (sesuaikan dengan kredensial lokal Anda):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/horolofi_db
spring.datasource.username=postgres
spring.datasource.password=password_anda
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 2. Build dan Run

Anda dapat menjalankan aplikasi menggunakan Maven Wrapper yang sudah disediakan:

**Menggunakan Command Prompt (Windows):**
```cmd
mvnw.cmd spring-boot:run
```

**Menggunakan Terminal (Linux/Mac):**
```bash
./mvnw spring-boot:run
```

Aplikasi akan berjalan pada `http://localhost:8080`.

## 🧪 Menjalankan Test

Untuk menjalankan unit test dan integration test:

```bash
./mvnw test
```

## 📂 Struktur Proyek

*   [`src/main/java`](src/main/java): Source code aplikasi (Controller, Service, Repository, Entity).
*   [`src/main/resources`](src/main/resources): File konfigurasi (`application.properties`) dan resource statis.
*   [`src/test/java`](src/test/java): Unit test (JUnit 5).
*   [`pom.xml`](pom.xml): Definisi dependensi proyek Maven.

## 🔒 Keamanan

Proyek ini menggunakan `spring-boot-starter-security`. Secara default, endpoint akan dilindungi. Konfigurasi keamanan lebih lanjut dapat ditemukan atau ditambahkan di paket `com.horolofi.rwa.config` (jika/saat dibuat).

## 👥 Kontribusi

1.  Fork repositori ini
2.  Buat branch fitur (`git checkout -b fitur-baru`)
3.  Commit perubahan Anda (`git commit -m 'Men// filepath: README.md
# HoroloFi RWA - Core Backend

**HoroloFi RWA** adalah backend untuk platform investasi dan pasar jam tangan mewah yang difraksionalisasi (Fractionalized luxury watch marketplace). Proyek ini dibangun menggunakan Java dan Spring Boot.

## 🛠 Teknologi yang Digunakan

Proyek ini menggunakan teknologi dan library berikut:

*   **Bahasa:** [Java 21](https://openjdk.org/projects/jdk/21/)
*   **Framework:** Spring Boot
*   **Build Tool:** Maven
*   **Database:** PostgreSQL
*   **Keamanan:** Spring Security
*   **ORM:** Spring Data JPA (Hibernate)
*   **Utilitas:** Lombok

## 📋 Prasyarat

Sebelum menjalankan aplikasi, pastikan Anda telah menginstal:

1.  **Java Development Kit (JDK) 21**
2.  **PostgreSQL Database**
3.  **Maven** (Opsional, karena proyek ini menyertakan Maven Wrapper)

## 🚀 Cara Menjalankan Aplikasi

### 1. Konfigurasi Database

Sebelum menjalankan aplikasi, Anda perlu mengonfigurasi koneksi database PostgreSQL. Buka file [`src/main/resources/application.properties`](src/main/resources/application.properties) dan tambahkan konfigurasi berikut (sesuaikan dengan kredensial lokal Anda):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/horolofi_db
spring.datasource.username=postgres
spring.datasource.password=password_anda
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 2. Build dan Run

Anda dapat menjalankan aplikasi menggunakan Maven Wrapper yang sudah disediakan:

**Menggunakan Command Prompt (Windows):**
```cmd
mvnw.cmd spring-boot:run
```

**Menggunakan Terminal (Linux/Mac):**
```bash
./mvnw spring-boot:run
```

Aplikasi akan berjalan pada `http://localhost:8080`.

## 🧪 Menjalankan Test

Untuk menjalankan unit test dan integration test:

```bash
./mvnw test
```

## 📂 Struktur Proyek

*   [`src/main/java`](src/main/java): Source code aplikasi (Controller, Service, Repository, Entity).
*   [`src/main/resources`](src/main/resources): File konfigurasi (`application.properties`) dan resource statis.
*   [`src/test/java`](src/test/java): Unit test (JUnit 5).
*   [`pom.xml`](pom.xml): Definisi dependensi proyek Maven.

## 🔒 Keamanan

Proyek ini menggunakan `spring-boot-starter-security`. Secara default, endpoint akan dilindungi. Konfigurasi keamanan lebih lanjut dapat ditemukan atau ditambahkan di paket `com.horolofi.rwa.config` (jika/saat dibuat).

## 👥 Kontribusi

1.  Fork repositori ini
2.  Buat branch fitur (`git checkout -b fitur-baru`)
3.  Commit perubahan Anda (`git commit -m 'Men