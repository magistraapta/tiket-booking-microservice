# **📘 System Requirements Document (SRD)**

### *Ticket Booking Microservice System*

---

# **1. Introduction**

## **1.1 Purpose**

The purpose of this system is to provide a scalable, reliable, event-driven ticket booking platform that allows users to browse events, reserve tickets, make payments, and manage orders.

## **1.2 Scope**

The system consists of the following components:

* **API Gateway**
* **Order Service**
* **Event Service** (event + ticket management)
* **Payment Service**
* **Message Broker / Event Bus**
* Front-end user interface (optional for full system)

---

# **2. System Overview**

The system uses **Event-Driven Architecture (EDA)**:

* Order Service publishes `OrderCreated` events.
* Event Service handles ticket availability and publishes `TicketReserved` or `TicketReservationFailed`.
* Payment Service processes payments and publishes `PaymentCompleted` or `PaymentFailed`.
* Order Service updates the final order status.

All communication between microservices occurs through a **single message broker**.

---

# **3. Functional Requirements**

## **3.1 API Gateway**

### **3.1.1 Requirements**

* FR-1: Route client requests to appropriate backend services.
* FR-2: Enforce authentication and authorization (optional).
* FR-3: Provide a unified entry point for all API calls.
* FR-4: Apply rate limiting and request validation.

---

## **3.2 Event Service (Event + Ticket Management)**

### **3.2.1 Event Management**

* FR-5: Create events (name, date, venue, capacity, seat types).
* FR-6: Update or delete events.
* FR-7: Retrieve event details and ticket availability.

### **3.2.2 Ticket Management**

* FR-8: Maintain ticket inventory per event.
* FR-9: Check availability upon receiving `OrderCreated`.
* FR-10: Reserve tickets when available.
* FR-11: Release tickets upon order cancellation or payment failure.
* FR-12: Publish events:

  * `TicketReserved`
  * `TicketReservationFailed`

---

## **3.3 Order Service**

### **General**

* FR-13: Create new orders for event tickets.
* FR-14: Validate order request (e.g., ticket count).
* FR-15: Publish `OrderCreated` to the message broker.

### **Event Consumption**

* FR-16: Consume `TicketReserved` → update order status to *Pending Payment*.
* FR-17: Consume `TicketReservationFailed` → update order status to *Failed*.
* FR-18: Consume `PaymentCompleted` → update order to *Confirmed*.
* FR-19: Consume `PaymentFailed` → update order to *Cancelled*, publish `OrderCancelled`.

---

## **3.4 Payment Service**

* FR-20: Consume `TicketReserved` events.
* FR-21: Process payment (mock or real integration).
* FR-22: Publish:

  * `PaymentCompleted`
  * `PaymentFailed`

---

## **3.5 Message Broker / Event Bus**

* FR-23: Support durable storage of events.
* FR-24: Deliver events at least once.
* FR-25: Support topics/queues for:

  * `order-created`
  * `ticket-reserved`
  * `ticket-reservation-failed`
  * `payment-completed`
  * `payment-failed`
  * `order-cancelled`
* FR-26: Retry failed message deliveries.

---

# **4. Non-Functional Requirements**

## **4.1 Performance**

* NFR-1: System should handle at least **500 orders per minute**.
* NFR-2: API Gateway response time < **200 ms** for non-blocking actions.

## **4.2 Scalability**

* NFR-3: Microservices must scale independently.
* NFR-4: Message broker must handle bursts in event traffic.

## **4.3 Reliability**

* NFR-5: No single service failure should bring down the entire system.
* NFR-6: Orders should be processed even if Event or Payment services temporarily go offline.

## **4.4 Fault Tolerance**

* NFR-7: Implement retry mechanisms for event consumers.
* NFR-8: Eventual consistency guaranteed across services.

## **4.5 Security**

* NFR-9: API Gateway must enforce authentication (JWT or OAuth2).
* NFR-10: Sensitive data (payment info) must be encrypted in transit.

## **4.6 Observability**

* NFR-11: Provide logs for all major events.
* NFR-12: Implement distributed tracing for microservices.
* NFR-13: Collect metrics (latency, throughput, failures).

---

# **5. System Architecture Requirements**

* SR-1: System must use **single message broker** (Kafka, RabbitMQ, or NATS).
* SR-2: Each service must be independently deployable.
* SR-3: Database per microservice (no shared DB).
* SR-4: Services communicate only through the message broker (no direct REST calls between services, except API gateway → service).
* SR-5: Support containerized deployment (Docker + Kubernetes recommended).

---

# **6. Data Requirements**

## **6.1 Order Data**

* orderId
* eventId
* userId
* quantity
* orderStatus
* createdAt / updatedAt

## **6.2 Event Data**

* eventId
* name
* venue
* date/time
* totalTickets
* availableTickets

## **6.3 Ticket Reservation Data**

* reservationId
* eventId
* orderId
* quantity
* status

---

# **7. Event Definitions**

## **7.1 OrderCreated**

* orderId
* eventId
* quantity

## **7.2 TicketReserved**

* orderId
* eventId
* quantity

## **7.3 TicketReservationFailed**

* orderId
* reason

## **7.4 PaymentCompleted**

* orderId
* amount

## **7.5 PaymentFailed**

* orderId
* reason

## **7.6 OrderCancelled**

* orderId
* reason

---

# **8. Constraints**

* Must avoid strong synchronous dependencies between services.
* Must maintain idempotency for event handling.
* Must ensure eventual consistency.

---

# **9. Future Enhancements (Optional)**

* Notification Service (email/SMS updates)
* Seat-level reservation (specific seats)
* Dynamic pricing based on demand
* Admin dashboard
* User profile and history service

