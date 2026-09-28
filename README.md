# FarmStock
Run: `mvn spring-boot:run` (MySQL on localhost:3306, edit application.properties). Swagger: /swagger-ui.html

## Postman test flow
1. POST /api/crops     {"name":"Tomato","unit":"kg"}
2. POST /api/harvests  {"cropId":1,"quantity":100,"harvestDate":"2026-09-20"}
3. POST /api/harvests  {"cropId":1,"quantity":50,"harvestDate":"2026-09-25"}
4. POST /api/sales     {"cropId":1,"quantity":120,"pricePerUnit":30,"saleDate":"2026-09-26"}  -> 201
5. GET  /api/stock/1   -> stock 30
6. POST /api/sales     {"cropId":1,"quantity":31,"pricePerUnit":30}  -> 409 Insufficient stock
7. POST /api/sales     {"cropId":1,"quantity":-5,"pricePerUnit":30}  -> 400 validation
8. POST /api/sales     {"cropId":99,"quantity":1,"pricePerUnit":30}  -> 404
9. GET  /api/revenue?from=2026-09-01&to=2026-09-30 -> Tomato 3600
