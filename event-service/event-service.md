# Event Service
Event service responsibility is to handle Event Management and Ticket management.

## Entity
**Event**
```markdown
Event {
    id
    name
    description
    start_date
    end_date
    location
    Tickets
}
```

**Tickets**
```markdown
Event {
    id
    seatNumber
    status
    price
}
```