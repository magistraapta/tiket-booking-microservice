# Order Service
Order service handle CRUD operation for the order entity
every transaction is recorded in order-service database.
This service also implemented DDD Hexagonal Architecture to achieve loosely-coupled code.

# Main Features
- [x] Handle CRUD for order

# Entity
```java
class Order {
    private Long id;
    private Long userId;
    private String status;
    private float totalPrice;
}
```

# API Design
- **Get Order By Id**
```markdown
GET /order/{id}
```
- **Create Order**
```markdown
POST /order
request body:
{
    "user_id": 1,
    "event_id": 1,
}
```
- **Update Order**
```markdown
PUT /order/{id}
```
- **Delete Order**
```markdown
DEL /order/{id}
```