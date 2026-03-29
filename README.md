# Offer Simulation

Müşterilerin ürün seçip teklif talep edebildiği, admin paneli üzerinden fiyatlandırılıp otomatik mail gönderilen tam kapsamlı bir teklif yönetim sistemi.

---

## Teknoloji Stack

| Katman | Teknoloji |
|--------|-----------|
| Frontend | Next.js 15 (App Router), Tailwind CSS |
| Backend | Spring Boot 3.5, Java 21 |
| Veritabanı | PostgreSQL 15 |
| Excel | Apache POI 5.2.3 |
| Mail | Spring Mail (Gmail SMTP) |
| CI/CD | GitHub Actions |

---

## Kurulum (Local)

### Gereksinimler
- Java 21+
- Node.js 20+
- PostgreSQL 15+
- Maven 3.9+

---

### 1. Veritabanı

PostgreSQL'de veritabanı oluştur:

```sql
CREATE DATABASE offer_db;
```

---

### 2. Backend

#### Ortam Değişkenleri

Aşağıdaki environment variable'ları sisteme tanımla:

| Değişken | Açıklama |
|----------|----------|
| `DB_PASSWORD_PROJE1` | PostgreSQL şifresi |
| `GMAIL_APP_CODE` | Gmail uygulama şifresi ([Nasıl alınır?](https://support.google.com/accounts/answer/185833)) |

> ⚠️ **Önemli:** `EmailService.java` dosyasındaki `message.setFrom(...)` satırını ve `application.properties` dosyasındaki `spring.mail.username` alanını **kendi Gmail adresinizle** güncellemeniz gerekmektedir.

**Windows (PowerShell):**
```powershell
$env:DB_PASSWORD_PROJE1="sifren"
$env:GMAIL_APP_CODE="gmail_app_sifren"
```

**Linux/macOS:**
```bash
export DB_PASSWORD_PROJE1=sifren
export GMAIL_APP_CODE=gmail_app_sifren
```

#### Başlatma

```bash
cd backend/offer-system
mvn clean package -DskipTests
mvn spring-boot:run
```

Backend `http://localhost:8080` adresinde çalışır.

> `spring.jpa.hibernate.ddl-auto=update` olduğu için tablolar otomatik oluşur. Seed data için aşağıdaki SQL'i çalıştır:

```sql
INSERT INTO products (name, last_quoted_price, last_quotation_date) VALUES
('HMI', 0, NULL),
('Led Panel', 0, NULL),
('LCD', 0, NULL);
```

---

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend `http://localhost:3000` adresinde çalışır.

---

## Kullanım

### Müşteri Akışı

1. `http://localhost:3000` adresine git
2. Müşteri e-posta adresini gir
3. İstenen ürünleri sepete ekle
4. **"Teklif Dosyasını Hazırla (XLSX)"** butonuna tıkla
5. Excel dosyası otomatik indirilir, teklif sisteme kaydedilir

### Admin Akışı

1. `http://localhost:3000/admin` adresine git
2. Bekleyen talepler listelenir — ilgili talebi seç
3. İndirilen Excel dosyasını aç, **"New Price"** sütununa fiyatları gir
4. Dosyayı kaydet ve admin paneline yükle
5. **"GÖNDER"** butonuna bas
6. Sistem otomatik olarak:
   - Ürün fiyatlarını veritabanında günceller
   - Müşteriye teklif mailini gönderir
   - Talebi `PRICED` olarak işaretler

---

## API Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/products` | Tüm ürünleri listele |
| `GET` | `/api/quotations` | Tüm teklif taleplerini listele |
| `GET` | `/api/quotations/{id}` | Tekil teklif detayı |
| `GET` | `/api/quotations/export-products?email=&productIds=` | Excel export + teklif oluştur |
| `POST` | `/api/quotations/import` | Fiyatlı Excel'i yükle, mail gönder |
| `PUT` | `/api/quotations/{id}/approve` | Teklifi onayla |
| `GET` | `/api/quotations/products/{productId}/history` | Ürün fiyat geçmişi |

---

## CI/CD

Her `push` ve `pull_request` işleminde GitHub Actions otomatik olarak:

1. **Backend:** Maven ile build alır (`mvn clean package`)
2. **Frontend:** `npm ci` + `npm run build` çalıştırır
3. PostgreSQL servisi ayağa kaldırılır, gerçek DB bağlantısı test edilir

Workflow dosyası: `.github/workflows/ci.yml`

---

## Proje Yapısı

```
offer-simulation/
├── .github/
│   └── workflows/
│       └── ci.yml              # GitHub Actions CI/CD
├── backend/
│   └── offer-system/
│       ├── src/main/java/...
│       │   ├── controller/     # REST Controller'lar
│       │   ├── service/        # İş mantığı
│       │   ├── entity/         # JPA Entity'leri
│       │   ├── dto/            # Data Transfer Object'ler
│       │   ├── repository/     # Spring Data JPA
│       │   └── mapper/         # Entity ↔ DTO dönüşüm
│       └── resources/
│           └── application.properties
└── frontend/
    ├── app/
    │   ├── page.tsx            # Müşteri sayfası
    │   └── admin/page.tsx      # Admin paneli
    └── services/
        └── api.ts              # API çağrıları
```
